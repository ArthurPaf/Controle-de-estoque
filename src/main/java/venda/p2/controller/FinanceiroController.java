package venda.p2.controller;

import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import venda.p2.model.FormaPagamento;
import java.util.List;

public class FinanceiroController {

    private GenericDAO<Financeiro> financeiroDAO;

    public FinanceiroController() {
        // Agora só precisamos do DAO do Financeiro, pois ele gerencia as parcelas em cascata
        this.financeiroDAO = new GenericDAO<>(Financeiro.class);
    }

    // Regra de Negócio: Lança a conta e gera as parcelas de forma atômica via Cascata
    public void lancarContaComParcelas(Financeiro financeiro, FormaPagamento fp) throws Exception {
        try {
            // 1. Calcula a quantidade e valores das parcelas
            int qtdParcelas = fp.getQtde_parcela() > 0 ? fp.getQtde_parcela() : 1;
            double valorPorParcela = financeiro.getValor_total() / qtdParcelas;
            
            // Multiplicação de dias convertidos para milissegundos
            long prazoEmMilissegundos = fp.getPrazo() * 24L * 60L * 60L * 1000L;
            java.util.Date dataVencimentoAtual = new java.util.Date();

            // 2. Cria as estruturas das parcelas na memória e vincula ao objeto pai
            for (int i = 1; i <= qtdParcelas; i++) {
                FinanceiroParcela parcela = new FinanceiroParcela();
                parcela.setN_parcela(i);
                parcela.setValor_original(valorPorParcela);
                parcela.setValor_final(valorPorParcela);
                parcela.setStatus(1); // 1 = Aberto
                
                if (i > 1) {
                    dataVencimentoAtual = new java.util.Date(dataVencimentoAtual.getTime() + prazoEmMilissegundos);
                }
                parcela.setData_vencimento(dataVencimentoAtual);

                // Método auxiliar que adiciona à lista interna e faz o 'parcela.setFinanceiro(financeiro)' automaticamente
                financeiro.adicionarParcela(parcela);
            }

            // 3. Salva o registro mestre. O CascadeType.ALL se encarrega de realizar o INSERT de todas as parcelas!
            financeiroDAO.salvar(financeiro);

        } catch (Exception e) {
            System.err.println("[!] Erro ao processar o lançamento em cascata: " + e.getMessage());
            throw e;
        }
    }

    public List<Financeiro> listarTodasContas() {
        return financeiroDAO.listarTodos();
    }
}