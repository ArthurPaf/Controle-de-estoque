package venda.p2.controller;

import venda.p2.dao.GenericDAO;
import venda.p2.model.Categoria;
import java.util.List;

public class CategoriaController {
    // Trocado o DAO específico pelo GenericDAO
    private GenericDAO<Categoria> categoriaDAO = new GenericDAO<>(Categoria.class);

    public String salvar(Categoria categoria) {
        
        // esse trim n deixa digitar espaço em branco (mantido idêntico)
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            return "Erro: O nome da categoria é obrigatório.";
        }

        try {
            // O Hibernate faz o papel de salvar ou atualizar de forma limpa
            categoriaDAO.salvar(categoria);
            return "Categoria '" + categoria.getNome() + "' cadastrada com sucesso!";
        } catch (Exception e) {
            return "Erro ao salvar a categoria no banco de dados: " + e.getMessage();
        }
    }

    public String excluir(int id) {
        try {
            categoriaDAO.excluir(id);
            return "Categoria excluída com sucesso!";
        } catch (Exception e) {
            // Mantida a sua regra de aviso para o usuário sobre chave estrangeira
            return "Erro ao excluir: verifique se existem produtos vinculados a esta categoria.";
        }
    }

    // Adicionado este método caso sua tabela gráfica precise listar as categorias
    public List<Categoria> listarTodos() {
        return categoriaDAO.listarTodos();
    }
}