package venda.p2.dao;

import jakarta.persistence.EntityManager;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import java.util.List;

public class CompraDAO {

    private GenericDAO<Compra> genericDAO;
    private GenericDAO<CompraProduto> itemDAO;

    public CompraDAO() {
        this.genericDAO = new GenericDAO<>(Compra.class);
        this.itemDAO = new GenericDAO<>(CompraProduto.class);
    }

    // --- ESSES SÃO OS MÉTODOS QUE O SEU CONTROLLER ESTÁ RECLAMANDO QUE NÃO SEI ONDE ESTÃO ---

    public Compra salvarCompra(Compra compra) throws Exception {
        return genericDAO.salvarERetornar(compra);
    }

    public void salvarItem(CompraProduto item) throws Exception {
        itemDAO.salvar(item);
    }

    public List<Compra> listarTodasCompras() throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Compra c", Compra.class).getResultList();
        } finally {
            em.close();
        }
    }

    public List<CompraProduto> listarItensPorCompra(int idCompra) throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            return em.createQuery("SELECT cp FROM CompraProduto cp JOIN FETCH cp.produto WHERE cp.compra.id = :idCompra", CompraProduto.class)
                     .setParameter("idCompra", idCompra)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}