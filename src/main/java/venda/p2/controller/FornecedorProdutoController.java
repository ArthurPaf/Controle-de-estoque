package venda.p2.controller;

import venda.p2.dao.FornecedorProdutoDAO;
import venda.p2.model.FornecedorProduto;

public class FornecedorProdutoController {

    private FornecedorProdutoDAO dao;

    public FornecedorProdutoController() {
        this.dao = new FornecedorProdutoDAO();
    }

    public void salvar(FornecedorProduto fp) {
        if (fp != null && fp.getFornecedor() != null && fp.getProduto() != null) {
            dao.salvar(fp);
        } else {
            System.err.println("Erro: Vínculo inválido (Fornecedor ou Produto nulo).");
        }
    }
}