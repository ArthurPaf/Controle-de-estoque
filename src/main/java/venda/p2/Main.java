package venda.p2;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import venda.p2.controller.ProdutoController;
import venda.p2.controller.VendaController;
import venda.p2.model.Categoria;
import venda.p2.model.Cliente;
import venda.p2.model.Fornecedor;
import venda.p2.model.FornecedorProduto;
import venda.p2.model.Produto;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class Main {
    public static void main(String[] args) {

        VendaController vendaController = new VendaController();
        ProdutoController produtoController = new ProdutoController();

        Categoria categoria1 = new Categoria(1, "Eletrônicos");
        Categoria categoria2 = new Categoria(2, "Eletrodomésticos");
        Categoria categoria3 = new Categoria(3, "Móveis");

        Cliente cliente1 = new Cliente(1, "João Silva", "123.456.789-00", "MG-12.345.678", "Rua A, 123",
                "(11) 98765-4321");
        Cliente cliente2 = new Cliente(2, "Maria Souza", "987.654.321-00", "SP-87.654.321", "Avenida B, 456",
                "(21) 91234-5678");
        Cliente cliente3 = new Cliente(3, "Carlos Oliveira", "456.789.123-00", "RJ-45.678.912", "Praça C, 789",
                "(31) 99876-5432");

        Fornecedor fornecedor1 = new Fornecedor(1, "Tech Supplies", "", "12.345.678/0001-99");
        Fornecedor fornecedor2 = new Fornecedor(2, "Gadget World", "", "98.765.432/0001-88");
        Fornecedor fornecedor3 = new Fornecedor(3, "ElectroMart", "", "56.789.012/0001-77");

        Produto produto1 = new Produto(1, "Smartphone", 1500.00, 2.0, categoria1);
        Produto produto2 = new Produto(2, "Notebook", 3500.00, 1.0, categoria1);
        Produto produto3 = new Produto(3, "Tablet", 1200.00, 3.0, categoria1);

        FornecedorProduto fp1 = new FornecedorProduto(1, fornecedor1, produto1);
        FornecedorProduto fp2 = new FornecedorProduto(2, fornecedor1, produto2);
        FornecedorProduto fp3 = new FornecedorProduto(3, fornecedor1, produto3);
        FornecedorProduto fp4 = new FornecedorProduto(4, fornecedor2, produto1);
        FornecedorProduto fp5 = new FornecedorProduto(5, fornecedor2, produto2);
        FornecedorProduto fp6 = new FornecedorProduto(6, fornecedor2, produto3);
        FornecedorProduto fp7 = new FornecedorProduto(7, fornecedor3, produto1);

        Venda venda1 = new Venda(1, cliente1, LocalDate.now(), 0.0);

        VendaProduto vp1 = new VendaProduto(1, venda1, produto1, 2, 1500.00);
        if (produtoController.verificaEstoqueExistente(vp1.getProduto())) {
            System.out.println("Produto 1 disponível em estoque.");
        } else {
            System.out.println("Produto 1 indisponível em estoque.");
        }

        VendaProduto vp2 = new VendaProduto(2, venda1, produto2, 1, 3500.00);
        if (produtoController.verificaEstoqueExistente(vp2.getProduto())) {
            System.out.println("Produto 2 disponível em estoque.");
        } else {
            System.out.println("Produto 2 indisponível em estoque.");
        }

        VendaProduto vp3 = new VendaProduto(3, venda1, produto3, 3, 1200.00);
        if (produtoController.verificaEstoqueExistente(vp3.getProduto())) {
            System.out.println("Produto 3 disponível em estoque.");
        } else {
            System.out.println("Produto 3 indisponível em estoque.");
        }

        List<VendaProduto> vendaProdutos = new ArrayList<>();
        vendaProdutos.add(vp1);
        vendaProdutos.add(vp2);
        vendaProdutos.add(vp3);

        venda1.setVendaProdutos(vendaProdutos);
        vendaController.salvar(venda1);

    }
}