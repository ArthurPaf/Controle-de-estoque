package venda.p2;

import venda.p2.controller.*;
import venda.p2.model.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // 1. Instanciar os Controllers
        CategoriaController catCtrl = new CategoriaController();
        ProdutoController prodCtrl = new ProdutoController();
        ClienteController cliCtrl = new ClienteController();
        FornecedorController fornCtrl = new FornecedorController();
        CompraController compraCtrl = new CompraController();
        VendaController vendaCtrl = new VendaController();

        System.out.println("--- INICIANDO TESTES DO SISTEMA (TCC/PROVA) ---");

        // 2. CADASTROS BÁSICOS
        Categoria cat = new Categoria(1, "Eletrônicos");
        System.out.println("Categoria: " + catCtrl.salvar(cat));

        Produto prod = new Produto(1, "Mouse Gamer", 0.0, 0.0, cat); 
        // Note: estoque inicial 0.0 para testar RNF003 depois
        System.out.println("Produto: " + prodCtrl.salvar(prod));

        Cliente cli = new Cliente(1, "João Silva", "123.456.789-00", "MG-12.345.678", "Rua A, 123", "(31) 99999-9999");
        System.out.println("Cliente: " + cliCtrl.salvar(cli));
        Cliente cli2 = new Cliente(2, "Maria Oliveira", "987.654.321-00", "MG-87.654.321", "Rua B, 456", "(31) 98888-8888");
        System.out.println("Cliente: " + cliCtrl.salvar(cli2));

        Fornecedor forn = new Fornecedor(1, "Tech Supplies", "Hardware LTDA", "12345678000199");
        System.out.println("Fornecedor: " + fornCtrl.salvar(forn));

        System.out.println("\n--- TESTANDO RNF002, RNF006 e RNF007 (COMPRA) ---");
        // Simular uma compra para abastecer o estoque e gerar Preço Médio
        Compra c1 = new Compra(1, LocalDate.now(), 100.0, forn);
        CompraProduto itemC1 = new CompraProduto(1, c1, prod, 10.0, 10.0); // 10 unidades a R$ 10,00
        c1.setCompraProdutos(new ArrayList<>());
        c1.getCompraProdutos().add(itemC1);
        
        System.out.println("Realizando Compra: " + compraCtrl.realizarCompra(c1));

        System.out.println("\n--- TESTANDO RNF001, RNF003 e RNF005 (VENDA) ---");
        // Criar uma venda válida
        Venda v1 = new Venda(1, cli, LocalDate.now(), 50.0);
        Venda v2 = new Venda(2, cli2, LocalDate.now(), 30.0);
        VendaProduto itemV1 = new VendaProduto(1, v1, prod, 2.0, 25.0); // Vendendo 2 unidades
        v1.setVendaProdutos(new ArrayList<>());
        v1.getVendaProdutos().add(itemV1);
        VendaProduto itemV2 = new VendaProduto(2, v2, prod, 1.0, 30.0); // Vendendo 1 unidade
        v2.setVendaProdutos(new ArrayList<>());
        v2.getVendaProdutos().add(itemV2);

        System.out.println("Venda 1 (Sucesso): " + vendaCtrl.realizarVenda(v1));

        // PROVA REAL: Buscar o produto do banco para ver se os campos atualizaram
        Produto pAtualizado = prodCtrl.pesquisar(1); // Pesquisa o Mouse Gamer (ID 1)
        System.out.println("\n--- VERIFICAÇÃO DE REQUISITOS NO PRODUTO ---");
        System.out.println("Preço Médio (RNF007): " + pAtualizado.getPreco());
        System.out.println("Última Venda (RNF005): " + pAtualizado.getValor_ultima_venda());
        System.out.println("Última Compra (RNF006): " + pAtualizado.getValor_ultima_compra());
        System.out.println("Estoque Atual (RNF001): " + pAtualizado.getQuantidade());

        System.out.println("\n--- TESTANDO RNF004 (LIMITE DE 3 VENDAS NO MÊS) ---");
        // Já fizemos 1 venda (v1). Vamos tentar fazer mais 3 para estourar o limite.
        for (int i = 2; i <= 5; i++) {
            Venda vx = new Venda(i, cli, LocalDate.now(), 10.0);
            vx.setVendaProdutos(new ArrayList<>());
            vx.getVendaProdutos().add(new VendaProduto(i, vx, prod, 1.0, 10.0)); // Vendendo 1 unidade a R$ 10,00
            
            System.out.println("Tentativa de Venda " + i + ": " + vendaCtrl.realizarVenda(vx));
        }

        System.out.println("\n--- TESTANDO RNF003 (ESTOQUE INFERIOR A 1) ---");
        // Vamos criar um produto novo com estoque zero e tentar vender
        Produto prodSemEstoque = new Produto(2, "Teclado Quebrado", 0.0, 0.0, cat);
        prodCtrl.salvar(prodSemEstoque);
        
        Venda vSemEstoque = new Venda(10, cli2, LocalDate.now(), 0.0);
        vSemEstoque.setVendaProdutos(new ArrayList<>());
        vSemEstoque.getVendaProdutos().add(new VendaProduto(10, vSemEstoque, prodSemEstoque, 1.0, 50.0));
        
        System.out.println("Venda Produto Sem Estoque: " + vendaCtrl.realizarVenda(vSemEstoque));

        System.out.println("\n--- TESTES FINALIZADOS ---");
    }
}