package venda.p2.dao;

import venda.p2.model.Cliente;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class ClienteDAO {

    private GenericDAO<Cliente> genericDAO;

    public ClienteDAO() {
        this.genericDAO = new GenericDAO<>(Cliente.class);
    }

    public void salvar(Cliente cliente) throws Exception {
        genericDAO.salvar(cliente);
    }

    public void excluir(int id) throws Exception {
        genericDAO.excluir(id);
    }

    public Cliente buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<Cliente> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }

    public List<Cliente> buscarPorNome(String nome) {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            String jpql = "SELECT c FROM Cliente c WHERE LOWER(TRIM(c.nome)) LIKE LOWER(:nome)";
            TypedQuery<Cliente> query = em.createQuery(jpql, Cliente.class);
            query.setParameter("nome", "%" + nome.trim() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}