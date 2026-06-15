package venda.p2.controller;

import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import venda.p2.model.FormaPagamento;
import java.util.List;
import java.util.ArrayList;

public class FinanceiroController {

    private GenericDAO<Financeiro> financeiroDAO;
    private GenericDAO<FinanceiroParcela> parcelaDAO;

    public FinanceiroController() {
        // O Controller inicializa os DAOs genéricos que ele precisa usar
        this.financeiroDAO = new GenericDAO<>(Financeiro.class);
        this.parcelaDAO = new GenericDAO<>(FinanceiroParcela.class);
    }

    // Regra de Negócio: Lança a conta e já quebra em parcelas de forma segura
    public void lancarContaComParcelas(Financeiro financeiro, FormaPagamento fp) throws Exception {
        // 1. Salva o registro mestre
        financeiroDAO.salvar(financeiro);

        // 2. Calcula e gera as parcelas automáticas
        int qtdParcelas = fp.getQtde_parcela() > 0 ? fp.getQtde_parcela() : 1;
        double valorPorParcela = financeiro.getValor_total() / qtdParcelas;
        long prazoEmMilissegundos = fp.getPrazo() * 24L * 60L * 60L * 1000L;

        java.util.Date dataVencimentoAtual = new java.util.Date();

        for (int i = 1; i <= qtdParcelas; i++) {
            FinanceiroParcela parcela = new FinanceiroParcela();
            parcela.setFinanceiro(financeiro);
            parcela.setN_parcela(i);
            parcela.setValor_original(valorPorParcela);
            parcela.setValor_final(valorPorParcela);
            parcela.setStatus(1); // 1 = Aberto
            
            if (i > 1) {
                dataVencimentoAtual = new java.util.Date(dataVencimentoAtual.getTime() + prazoEmMilissegundos);
            }
            parcela.setData_vencimento(dataVencimentoAtual);

            // Salva o detalhe
            parcelaDAO.salvar(parcela);
        }
    }

    public List<Financeiro> listarTodasContas() {
        return financeiroDAO.listarTodos();
    }
}