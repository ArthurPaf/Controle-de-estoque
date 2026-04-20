package venda.p2.controller;

import venda.p2.dao.CompraDAO;
import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Produto;

public class CompraController {

    private CompraDAO compraDAO = new CompraDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public String realizarCompra(Compra compra) {
    for (CompraProduto item : compra.getCompraProdutos()) {
        Produto p = produtoDAO.pesquisar(item.getProduto().getId());

        // RNF007: CÁLCULO DO PREÇO MÉDIO
        // (Soma do valor total em estoque antigo + valor da compra nova) / (quantidade total nova)
        double valorEstoqueAtual = p.getQuantidade() * p.getPreco();
        double valorNovaCompra = item.getQuantidade() * item.getValorUnitario();
        double novaQuantidadeTotal = p.getQuantidade() + item.getQuantidade();
        
        double novoPrecoMedio = (valorEstoqueAtual + valorNovaCompra) / novaQuantidadeTotal;

        // Atualiza o objeto produto com os novos valores decididos pelo Controller
        p.setPreco(novoPrecoMedio); // Novo preço médio calculado
        p.setQuantidade(novaQuantidadeTotal); // RNF002: Soma do estoque
        p.setValor_ultima_compra(item.getValorUnitario()); // RNF006

        // Manda o DAO salvar o produto atualizado
        produtoDAO.alterar(p);
    }

    return compraDAO.salvar(compra) ? "Compra registrada!" : "Erro ao salvar.";
}
}