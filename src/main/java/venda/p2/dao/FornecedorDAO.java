package venda.p2.dao;

import venda.p2.model.Fornecedor;
import java.util.List;

public class FornecedorDAO {

    private GenericDAO<Fornecedor> genericDAO;

    public FornecedorDAO() {
        this.genericDAO = new GenericDAO<>(Fornecedor.class);
    }

    public void salvar(Fornecedor f) throws Exception {
        genericDAO.salvar(f);
    }

    public void excluir(int id) throws Exception {
        genericDAO.excluir(id);
    }

    public Fornecedor buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<Fornecedor> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }
}