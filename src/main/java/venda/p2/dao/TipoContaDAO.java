package venda.p2.dao;

import venda.p2.model.TipoConta;
import java.util.List;

public class TipoContaDAO {

    private GenericDAO<TipoConta> genericDAO;

    public TipoContaDAO() {
        this.genericDAO = new GenericDAO<>(TipoConta.class);
    }

    public void salvar(TipoConta tc) throws Exception {
        genericDAO.salvar(tc);
    }

    public void excluir(int id) throws Exception {
        genericDAO.excluir(id);
    }

    public TipoConta buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<TipoConta> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }
}