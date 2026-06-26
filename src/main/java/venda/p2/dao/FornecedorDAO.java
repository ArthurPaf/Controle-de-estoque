package venda.p2.dao;

import venda.p2.model.Fornecedor;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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

    public List<Fornecedor> buscarPorNome(String nome) {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            String jpql = "SELECT f FROM Fornecedor f WHERE LOWER(TRIM(f.nome_fantasia)) LIKE LOWER(:nome)";
            TypedQuery<Fornecedor> query = em.createQuery(jpql, Fornecedor.class);
            query.setParameter("nome", "%" + nome.trim() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}