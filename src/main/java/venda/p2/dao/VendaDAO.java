package venda.p2.dao;

import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;
import java.util.List;

import jakarta.persistence.EntityManager;

public class VendaDAO {

    private GenericDAO<Venda> genericDAO;
    private GenericDAO<VendaProduto> itemDAO;

    public VendaDAO() {
        this.genericDAO = new GenericDAO<>(Venda.class);
        this.itemDAO = new GenericDAO<>(VendaProduto.class);
    }

    public Venda salvarVenda(Venda venda) throws Exception {
    // Altere de genericDAO.salvar(venda) para salvarERetornar
    return genericDAO.salvarERetornar(venda); 
}

    public void salvarItem(VendaProduto item) throws Exception {
        itemDAO.salvar(item);
    }

    public List<Venda> listarTodasVendas() throws Exception {
    EntityManager em = GenericDAO.getEntityManager();
    try {
        // O JOIN FETCH traz os dados do cliente na mesma consulta
        return em.createQuery("SELECT v FROM Venda v JOIN FETCH v.cliente", Venda.class).getResultList();
    } finally {
        em.close();
    }
}

    public List<VendaProduto> listarItensPorVenda(int idVenda) throws Exception {
    EntityManager em = GenericDAO.getEntityManager();
    try {
        // Consulta para trazer os itens de uma venda específica, junto com os dados do produto
        return em.createQuery(
            "SELECT vp FROM VendaProduto vp JOIN FETCH vp.produto WHERE vp.venda.id = :idVenda", 
            VendaProduto.class)
            .setParameter("idVenda", idVenda)
            .getResultList();
    } finally {
        em.close();
    }
}

    public List<Venda> listarVendasPorFiltros(java.time.LocalDate dataInicio, java.time.LocalDate dataFim, Integer idCliente) throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            StringBuilder hql = new StringBuilder("SELECT v FROM Venda v JOIN FETCH v.cliente WHERE 1=1 ");
            
            // Filtro usando o atributo da classe Java (dataVenda)
            if (dataInicio != null && dataFim != null) {
                if (dataInicio.equals(dataFim)) {
                    hql.append("AND v.dataVenda = :dataInicio ");
                } else {
                    hql.append("AND v.dataVenda >= :dataInicio AND v.dataVenda <= :dataFim ");
                }
            }
            
            if (idCliente != null && idCliente > 0) {
                hql.append("AND v.cliente.id = :idCliente ");
            }
            // Ajustado também aqui na ordenação
            hql.append("ORDER BY v.dataVenda DESC");

            var query = em.createQuery(hql.toString(), Venda.class);
            
            if (dataInicio != null && dataFim != null) {
                query.setParameter("dataInicio", dataInicio);
                if (!dataInicio.equals(dataFim)) {
                    query.setParameter("dataFim", dataFim);
                }
            }
            if (idCliente != null && idCliente > 0) {
                query.setParameter("idCliente", idCliente);
            }
            
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}