package venda.p2.dao;

import venda.p2.model.TipoConta;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

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

    public List<TipoConta> buscarPorDescricao(String descricao) {
        EntityManager em = GenericDAO.getEntityManager();
        try {
           
            String jpql = "SELECT f FROM TipoConta f WHERE LOWER(TRIM(f.descricao)) LIKE LOWER(:descricao)";
            TypedQuery<TipoConta> query = em.createQuery(jpql, TipoConta.class);
            query.setParameter("descricao", "%" + descricao.trim() + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}