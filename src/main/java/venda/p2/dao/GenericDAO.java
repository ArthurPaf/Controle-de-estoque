package venda.p2.dao;


import venda.p2.util.JPAUtil; // Ajuste o pacote do seu JPAUtil se necessário
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import java.util.List;

public class GenericDAO<T> {

    private final Class<T> classe;

    public GenericDAO(Class<T> classe) {
        this.classe = classe;
    }

    // Método salvar que serve tanto para Inserir quanto para Atualizar (Merge)
    public void salvar(T entidade) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(entidade); // Salva ou atualiza automaticamente
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    // Método para buscar por ID
    public T buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(classe, id);
        } finally {
            em.close();
        }
    }

    // Método para listar todos os registros de uma tabela
    public List<T> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Cria uma query dinâmica simples: "FROM Categoria", "FROM Produto", etc.
            return em.createQuery("from " + classe.getSimpleName(), classe).getResultList();
        } finally {
            em.close();
        }
    }

    // Método para remover um registro do banco
    public void excluir(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            T entidade = em.find(classe, id);
            if (entidade != null) {
                em.remove(entidade);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}