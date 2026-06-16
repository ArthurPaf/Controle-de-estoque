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
}