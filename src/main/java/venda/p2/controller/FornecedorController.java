package venda.p2.controller;

import venda.p2.dao.FornecedorDAO;
import venda.p2.model.Fornecedor;

public class FornecedorController {
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public void salvar(Fornecedor fornecedor) {
        fornecedorDAO.salvar(fornecedor);
    }
}