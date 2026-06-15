package venda.p2.controller;

import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Produto;

public class ProdutoController {
    // Trocado o DAO específico pelo GenericDAO
    private GenericDAO<Produto> produtoDAO = new GenericDAO<>(Produto.class);

    public String salvar(Produto produto) {
        
        if (produto.getCategoria() == null || produto.getCategoria().getId() <= 0) {
            return "Erro: O produto deve estar vinculado a uma categoria.";
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            return "Erro: O nome do produto é obrigatório.";
        }

        try {
            // O Hibernate salva ou atualiza de acordo com o estado do objeto
            produtoDAO.salvar(produto);
            return "Produto '" + produto.getNome() + "' salvo com sucesso!";
        } catch (Exception e) {
            return "Erro ao salvar o produto no banco de dados: " + e.getMessage();
        }
    }

    // Sistema não pode realizar venda se estoque for inferior a 1 (Mantida a regra intacta)
    public boolean temEstoqueParaVenda(int produtoId) {
        Produto p = produtoDAO.buscarPorId(produtoId);
       
        return p != null && p.getQuantidade() >= 1;
    }

    public String excluir(int id) {
        try {
            produtoDAO.excluir(id);
            return "Produto excluído!";
        } catch (Exception e) {
            // Mantida a mensagem para caso de violação de chave estrangeira (Venda/Compra)
            return "Erro ao excluir: o produto pode estar vinculado a uma venda/compra.";
        }
    }

    public Produto pesquisar(int id) {
        return produtoDAO.buscarPorId(id);
    }

    // Adicionado o método para preencher a sua JTable de consulta de produtos na View
    public List<Produto> listarTodos() {
        return produtoDAO.listarTodos();
    }
}