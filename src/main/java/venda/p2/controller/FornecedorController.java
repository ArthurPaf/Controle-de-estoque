package venda.p2.controller;

import venda.p2.dao.FornecedorDAO;
import venda.p2.model.Fornecedor;
import java.util.List;

public class FornecedorController {

    private FornecedorDAO fornecedorDAO;

    public FornecedorController() {
        this.fornecedorDAO = new FornecedorDAO();
    }

    public List<Fornecedor> listarTodos() throws Exception {
        return fornecedorDAO.listarTodos();
    }

    public Fornecedor buscarPorId(int id) throws Exception {
        return fornecedorDAO.buscarPorId(id);
    }

    public void salvarFornecedor(String nomeFantasia, String razaoSocial, String cnpj) throws Exception {
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty() || cnpj == null || cnpj.trim().isEmpty()) {
            throw new Exception("Nome Fantasia e CNPJ são obrigatórios!");
        }

        Fornecedor f = new Fornecedor();
        f.setNomeFantasia(nomeFantasia.trim());
        f.setRazaoSocial(razaoSocial.trim());
        f.setCnpj(cnpj.trim());

        fornecedorDAO.salvar(f);
    }

    public void atualizarFornecedor(Fornecedor f, String nomeFantasia, String razaoSocial, String cnpj) throws Exception {
        if (f == null) throw new Exception("Nenhum fornecedor selecionado.");
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty() || cnpj == null || cnpj.trim().isEmpty()) {
            throw new Exception("Nome Fantasia e CNPJ são obrigatórios!");
        }

        f.setNomeFantasia(nomeFantasia.trim());
        f.setRazaoSocial(razaoSocial.trim());
        f.setCnpj(cnpj.trim());

        fornecedorDAO.salvar(f);
    }

    public void excluirFornecedor(int id) throws Exception {
        fornecedorDAO.excluir(id);
    }
}