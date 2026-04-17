package venda.p2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import venda.p2.controller.ProdutoController;
import venda.p2.controller.VendaController;
import venda.p2.controller.CategoriaController; // Importe seus outros controllers
import venda.p2.controller.ClienteController;
import venda.p2.controller.FornecedorController;

import venda.p2.model.Categoria;
import venda.p2.model.Cliente;
import venda.p2.model.Fornecedor;
import venda.p2.model.Produto;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class Main {
    public static void main(String[] args) {

        // 1. Instanciar os Controllers
        VendaController vendaController = new VendaController();
        ProdutoController produtoController = new ProdutoController();
        CategoriaController categoriaController = new CategoriaController();
        ClienteController clienteController = new ClienteController();
        FornecedorController fornecedorController = new FornecedorController();

        // 2. Criar e SALVAR Categorias (Obrigatório para o Produto existir)
        Categoria categoria1 = new Categoria(1, "Eletrônicos");
        Categoria categoria2 = new Categoria(2, "Eletrodomésticos");
        Categoria categoria3 = new Categoria(3, "Móveis");
        
        categoriaController.salvar(categoria1);
        categoriaController.salvar(categoria2);
        categoriaController.salvar(categoria3);

        // 3. Criar e SALVAR Clientes
        Cliente cliente1 = new Cliente(1, "João Silva", "123.456.789-00", "MG-12.345.678", "Rua A, 123", "(11) 98765-4321");
        Cliente cliente2 = new Cliente(2, "Maria Souza", "987.654.321-00", "SP-87.654.321", "Avenida B, 456", "(21) 91234-5678");
        
        clienteController.salvar(cliente1);
        clienteController.salvar(cliente2);

        // 4. Criar e SALVAR Fornecedores
        Fornecedor fornecedor1 = new Fornecedor(1, "Tech Supplies", "Tech LTDA", "12.345.678/0001-99");
        fornecedorController.salvar(fornecedor1);

        // 5. Criar e SALVAR Produtos (Agora a Categoria 1 já existe no banco!)
        Produto produto1 = new Produto(1, "Smartphone", 1500.00, 10.0, categoria1);
        Produto produto2 = new Produto(2, "Notebook", 3500.00, 5.0, categoria1);
        Produto produto3 = new Produto(3, "Tablet", 1200.00, 8.0, categoria1);

        produtoController.salvar(produto1);
        produtoController.salvar(produto2);
        produtoController.salvar(produto3);

        // 6. Processo de Venda
        System.out.println("--- Iniciando Processo de Venda ---");

        Venda venda1 = new Venda(1, cliente1, LocalDate.now(), 0.0);
        List<VendaProduto> vendaProdutos = new ArrayList<>();

        // Item 1
        if (produtoController.verificaEstoqueExistente(produto1)) {
            System.out.println("Produto 1 disponível em estoque.");
            vendaProdutos.add(new VendaProduto(1, venda1, produto1, 2, 1500.00));
        }

        // Item 2
        if (produtoController.verificaEstoqueExistente(produto2)) {
            System.out.println("Produto 2 disponível em estoque.");
            vendaProdutos.add(new VendaProduto(2, venda1, produto2, 1, 3500.00));
        }

        // 7. Salvar a Venda Completa
        if (!vendaProdutos.isEmpty()) {
            venda1.setVendaProdutos(vendaProdutos);
            vendaController.salvar(venda1);
            System.out.println("Venda finalizada e salva no banco!");
        } else {
            System.out.println("Venda não realizada: Nenhum produto disponível.");
        }
    }
}