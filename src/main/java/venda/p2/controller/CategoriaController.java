package venda.p2.controller;

import venda.p2.dao.CategoriaDAO;
import venda.p2.model.Categoria;
import java.util.List;

public class CategoriaController {

    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO(); // Instancia o DAO focado em Categoria
    }

    public List<Categoria> listarCategorias() throws Exception {
        return categoriaDAO.listarTodos();
    }

    public Categoria buscarPorId(int id) throws Exception {
        return categoriaDAO.buscarPorId(id);
    }

    public void salvarCategoria(Categoria categoria) throws Exception {
        // Validação de regra de negócio antes de mandar pro banco
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new Exception("Nome inválido!");
        }
        categoriaDAO.salvar(categoria);
    }

    public void excluirCategoria(int id) throws Exception {
        categoriaDAO.excluir(id);
    }
}