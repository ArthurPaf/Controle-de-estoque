package venda.p2.controller;

import venda.p2.dao.CategoriaDAO;
import venda.p2.model.Categoria;

public class CategoriaController {
    private CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void salvar(Categoria categoria) {
        categoriaDAO.salvar(categoria);
    }
}