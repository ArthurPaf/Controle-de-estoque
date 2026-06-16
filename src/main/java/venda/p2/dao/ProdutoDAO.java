package venda.p2.dao;

import venda.p2.model.Produto;
import java.util.List;

public class ProdutoDAO {

    private GenericDAO<Produto> genericDAO;

    public ProdutoDAO() {
        this.genericDAO = new GenericDAO<>(Produto.class);
    }

    public void salvar(Produto p) throws Exception {
        genericDAO.salvar(p);
    }

    public void excluir(int id) throws Exception {
        genericDAO.excluir(id);
    }

    public Produto buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<Produto> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }
}