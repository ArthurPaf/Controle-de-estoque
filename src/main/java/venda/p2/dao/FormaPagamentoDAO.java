package venda.p2.dao;

import venda.p2.model.FormaPagamento;
import java.util.List;

public class FormaPagamentoDAO {

    private GenericDAO<FormaPagamento> genericDAO;

    public FormaPagamentoDAO() {
        this.genericDAO = new GenericDAO<>(FormaPagamento.class);
    }

    public void salvar(FormaPagamento f) throws Exception {
        genericDAO.salvar(f);
    }

    public void excluir(int id) throws Exception {
        genericDAO.excluir(id);
    }

    public FormaPagamento buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<FormaPagamento> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }
}