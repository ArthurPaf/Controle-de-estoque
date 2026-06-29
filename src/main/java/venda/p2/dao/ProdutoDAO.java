package venda.p2.dao;

import venda.p2.model.Produto;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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

    public List<Produto> buscarPorNome(String nome) {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            
            String jpql = "SELECT p FROM Produto p WHERE LOWER(TRIM(p.nome)) LIKE LOWER(:nome)";
            TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
            
            
            query.setParameter("nome", "%" + nome.trim() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}