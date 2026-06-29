package venda.p2.dao;

import jakarta.persistence.EntityManager;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import java.time.LocalDate;
import java.util.List;

public class CompraDAO {

    private GenericDAO<Compra> genericDAO;
    private GenericDAO<CompraProduto> itemDAO;

    public CompraDAO() {
        this.genericDAO = new GenericDAO<>(Compra.class);
        this.itemDAO = new GenericDAO<>(CompraProduto.class);
    }

    public Compra salvarCompra(Compra compra) throws Exception {
        return genericDAO.salvarERetornar(compra);
    }

    public void salvarItem(CompraProduto item) throws Exception {
        itemDAO.salvar(item);
    }

    public List<Compra> listarTodasCompras() throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            return em.createQuery("SELECT c FROM Compra c JOIN FETCH c.fornecedor ORDER BY c.dataCompra DESC", Compra.class).getResultList();
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

    
    public List<Compra> consultarComprasComFiltros(LocalDate dataInicio, LocalDate dataFim, Integer idFornecedor) throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            StringBuilder hql = new StringBuilder("SELECT c FROM Compra c JOIN FETCH c.fornecedor WHERE 1=1 ");

            if (dataInicio != null) {
                hql.append("AND c.dataCompra >= :dataInicio ");
            }
            if (dataFim != null) {
                hql.append("AND c.dataCompra <= :dataFim ");
            }
            if (idFornecedor != null) {
                hql.append("AND c.fornecedor.id = :idFornecedor ");
            }

            
            hql.append("ORDER BY c.dataCompra DESC");

            var query = em.createQuery(hql.toString(), Compra.class);

            if (dataInicio != null) {
                query.setParameter("dataInicio", dataInicio);
            }
            if (dataFim != null) {
                query.setParameter("dataFim", dataFim);
            }
            if (idFornecedor != null) {
                query.setParameter("idFornecedor", idFornecedor);
            }

            return query.getResultList();
        } finally {
            em.close();
        }
    }
}