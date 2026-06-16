package venda.p2.dao;

import jakarta.persistence.EntityManager;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import java.util.List;

public class FinanceiroDAO {

    private GenericDAO<Financeiro> financeiroGeneric;
    private GenericDAO<FinanceiroParcela> parcelaGeneric;

    public FinanceiroDAO() {
        this.financeiroGeneric = new GenericDAO<>(Financeiro.class);
        this.parcelaGeneric = new GenericDAO<>(FinanceiroParcela.class);
    }

    public Financeiro salvarERetornar(Financeiro f) throws Exception {
        return financeiroGeneric.salvarERetornar(f);
    }

    public void salvar(Financeiro f) throws Exception {
        financeiroGeneric.salvar(f);
    }

    public void excluir(int id) throws Exception {
        financeiroGeneric.excluir(id);
    }

    public Financeiro buscarPorId(int id) throws Exception {
        return financeiroGeneric.buscarPorId(id);
    }

    public List<Financeiro> listarTodos() throws Exception {
        return financeiroGeneric.listarTodos();
    }

    public void salvarParcelas(List<FinanceiroParcela> parcelas) throws Exception {
        for (FinanceiroParcela p : parcelas) {
            parcelaGeneric.salvar(p);
        }
    }

    // =========================================================================
    // MÉTODOS ADICIONADOS PARA FAZER A TELA DE PARCELAS FUNCIONAR
    // =========================================================================

    /**
     * Busca todas as parcelas associadas a um Lançamento Financeiro específico.
     */
    public List<FinanceiroParcela> listarParcelasPorLancamento(int idFinanceiro) throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            // Cria a consulta baseada exatamente na propriedade 'financeiro' da sua classe FinanceiroParcela
            return em.createQuery("SELECT p FROM FinanceiroParcela p WHERE p.financeiro.id = :idFin", FinanceiroParcela.class)
                     .setParameter("idFin", idFinanceiro)
                     .getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Atualiza/Salva uma única parcela alterada na hora de dar baixa.
     */
    public void atualizarParcela(FinanceiroParcela parcela) throws Exception {
        parcelaGeneric.salvar(parcela);
    }
}