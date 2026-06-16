package venda.p2.dao;

import venda.p2.model.FinanceiroParcela;
import java.util.List;

public class FinanceiroParcelaDAO {

    private GenericDAO<FinanceiroParcela> genericDAO;

    public FinanceiroParcelaDAO() {
        this.genericDAO = new GenericDAO<>(FinanceiroParcela.class);
    }

    public void salvar(FinanceiroParcela p) throws Exception {
        genericDAO.salvar(p);
    }

    public FinanceiroParcela buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<FinanceiroParcela> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }
}