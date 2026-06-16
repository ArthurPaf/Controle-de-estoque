package venda.p2.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import venda.p2.model.Financeiro;

import java.util.List;

public class GenericDAO<T> {

    private final Class<T> classe;
    // Substitua "venda_p2_PU" pelo nome da sua Persistence Unit que está no seu persistence.xml
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("siscom-pu");

    public GenericDAO(Class<T> classe) {
        this.classe = classe;
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void salvar(T entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // O merge serve tanto para INSERT quanto para UPDATE no JPA
            em.merge(entidade);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public T buscarPorId(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(classe, id);
        } finally {
            em.close();
        }
    }

    public List<T> listarTodos() {
        EntityManager em = getEntityManager();
        try {
            return em.createQuery("from " + classe.getName(), classe).getResultList();
        } finally {
            em.close();
        }
    }

    public void excluir(int id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T entidade = em.find(classe, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public T salvarERetornar(T entidade) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            T merged = em.merge(entidade);
            em.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}