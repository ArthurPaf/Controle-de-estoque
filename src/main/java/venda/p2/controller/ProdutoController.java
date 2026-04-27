package venda.p2.controller;

import java.util.List;
import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Produto;

public class ProdutoController {
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public String salvar(Produto produto) {
        
        if (produto.getCategoria() == null || produto.getCategoria().getId() <= 0) {
            return "Erro: O produto deve estar vinculado a uma categoria (RF005).";
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            return "Erro: O nome do produto é obrigatório.";
        }

       
        if (produtoDAO.salvar(produto)) {
            return "Produto '" + produto.getNome() + "' salvo com sucesso!";
        } else {
            return "Erro ao salvar o produto no banco de dados.";
        }
    }

    // Sistema não pode realizar venda se estoque for inferior a 1
    public boolean temEstoqueParaVenda(int produtoId) {
        Produto p = produtoDAO.pesquisar(produtoId);
        // Se o produto não existir ou a quantidade for menor que 1, retorna false
        return p != null && p.getQuantidade() >= 1;
    }

   

    public String excluir(int id) {
        // Antes de excluir, no mundo real, você verificaria se o produto 
        // já não está em alguma venda
        if (produtoDAO.excluir(id)) {
            return "Produto excluído!";
        } else {
            return "Erro ao excluir: o produto pode estar vinculado a uma venda/compra.";
        }
    }

    // Mantemos os métodos de busca para as telas
    public Produto pesquisar(int id) {
        return produtoDAO.pesquisar(id);
    }
}