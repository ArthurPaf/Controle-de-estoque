package venda.p2.controller;

import java.util.List;

import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Produto;

public class ProdutoController {
    ProdutoDAO produtoDAO = new ProdutoDAO();

    public boolean salvar(Produto produto) {
        return produtoDAO.salvar(produto);
    }

    public boolean alterar(Produto produto) {
        return produtoDAO.alterar(produto);
    }

    public boolean excluir(int id) {
        return produtoDAO.excluir(id);
    }

    public Produto pesquisar(int id) {
        return produtoDAO.pesquisar(id);
    }

    public boolean atualizarEstoque(Produto produto, int quantidade) {
        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());
        if (produtoExistente == null) {
            return false;
        }

        return produtoDAO.atualizarEstoque(produto, quantidade);
    }

    public boolean verificaEstoqueExistente(Produto produto) {
        Produto produtoExistente = produtoDAO.pesquisar(produto.getId());
        if (produtoExistente == null) {
            return false;
        }

        if (produtoExistente.getQuantidade() >= 1) {
            return true;
        } else {
            return false;
        }
    }

}
