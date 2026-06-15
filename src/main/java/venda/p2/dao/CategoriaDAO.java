package venda.p2.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import venda.p2.model.Categoria;
import java.util.List;

public class CategoriaDAO {

    private GenericDAO<Categoria> genericDAO;

    public CategoriaDAO() {
        // Reutiliza a estrutura do seu GenericDAO que já funciona
        this.genericDAO = new GenericDAO<>(Categoria.class);
    }

    public void salvar(Categoria categoria) throws Exception {
        genericDAO.salvar(categoria);
    }

    public void excluir(int id) throws Exception {
        genericDAO.excluir(id);
    }

    public Categoria buscarPorId(int id) throws Exception {
        return genericDAO.buscarPorId(id);
    }

    public List<Categoria> listarTodos() throws Exception {
        return genericDAO.listarTodos();
    }

    // Exemplo de método customizado que SÓ a Categoria precisa (O GenericDAO não tem)
    public List<Categoria> buscarPorNome(String nome) {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            String jpql = "SELECT c FROM Categoria c WHERE LOWER(c.nome) LIKE LOWER(:nome)";
            TypedQuery<Categoria> query = em.createQuery(jpql, Categoria.class);
            query.setParameter("nome", "%" + nome + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}