package venda.p2.controller;

import venda.p2.dao.FinanceiroDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import venda.p2.model.FormaPagamento;
import venda.p2.model.TipoConta;
import java.util.ArrayList;
import java.util.List;

public class FinanceiroController {

    private FinanceiroDAO financeiroDAO;
    private GenericDAO<TipoConta> tipoContaDAO;
    private GenericDAO<FormaPagamento> formaPagamentoDAO;

    public FinanceiroController() {
        this.financeiroDAO = new FinanceiroDAO();
        this.tipoContaDAO = new GenericDAO<>(TipoConta.class);
        this.formaPagamentoDAO = new GenericDAO<>(FormaPagamento.class);
    }

    public List<TipoConta> listarTiposConta() throws Exception {
        return tipoContaDAO.listarTodos();
    }

    public List<FormaPagamento> listarFormasPagamento() throws Exception {
        return formaPagamentoDAO.listarTodos();
    }

    public List<Financeiro> listarLancamentos() throws Exception {
        return financeiroDAO.listarTodos();
    }

    public Financeiro buscarPorId(int id) throws Exception {
        return financeiroDAO.buscarPorId(id);
    }

    public List<FinanceiroParcela> obterParcelas(int idFinanceiro) throws Exception {
    return financeiroDAO.listarParcelasPorLancamento(idFinanceiro);
    }

    public void gerenciarNovoLancamento(int tipoMovimentacao, TipoConta tc, FormaPagamento fp, String valorStr) throws Exception {
        if (valorStr == null || valorStr.trim().isEmpty()) {
            throw new Exception("Defina o valor do lançamento.");
        }

        double valorTotal = Double.parseDouble(valorStr.trim());

        Financeiro f = new Financeiro();
        f.setPagar_ou_receber(tipoMovimentacao == 0 ? 1 : 2);
        f.setTipoConta(tc);
        f.setFormaPagamento(fp);
        f.setValor_total(valorTotal);
        f.setData_conta(new java.util.Date());

        // Salva o pai para obter a ID gerada
        Financeiro fSalvo = financeiroDAO.salvarERetornar(f);

        // Regra de Negócio: Desmembramento e cálculo temporal das parcelas
        int qtdParcelas = (fp != null && fp.getQtde_parcela() > 0) ? fp.getQtde_parcela() : 1;
        double valorPorParcela = valorTotal / qtdParcelas;
        long prazoEmMilissegundos = (fp != null) ? fp.getPrazo() * 24L * 60L * 60L * 1000L : 0L;
        
        java.util.Date dataVencimentoAtual = new java.util.Date();
        List<FinanceiroParcela> listaParcelas = new ArrayList<>();

        for (int i = 1; i <= qtdParcelas; i++) {
            FinanceiroParcela parcela = new FinanceiroParcela();
            parcela.setFinanceiro(fSalvo);
            parcela.setN_parcela(i);
            parcela.setValor_original(valorPorParcela);
            parcela.setValor_final(valorPorParcela);
            parcela.setStatus(1); // 1 - Aberto/Pendente

            if (i > 1) {
                dataVencimentoAtual = new java.util.Date(dataVencimentoAtual.getTime() + prazoEmMilissegundos);
            }
            parcela.setData_vencimento(dataVencimentoAtual);
            listaParcelas.add(parcela);
        }

        financeiroDAO.salvarParcelas(listaParcelas);
    }

    public void atualizarLancamento(Financeiro f) throws Exception {
        // 1. Atualiza os dados do Lançamento Pai no banco (valor_total, tipo de conta, etc.)
        financeiroDAO.salvar(f);
        
        // 2. Busca as parcelas que já existem associadas a este lançamento
        List<FinanceiroParcela> parcelasExistentes = financeiroDAO.listarParcelasPorLancamento(f.getId());
        
        if (parcelasExistentes != null && !parcelasExistentes.isEmpty()) {
            // 3. Regra de Negócio: Divide o NOVO valor total pela quantidade de parcelas que já existem
            double novoValorPorParcela = f.getValor_total() / parcelasExistentes.size();
            
            // 4. Varre a lista de parcelas aplicando o novo valor apenas nas que estão em aberto
            for (FinanceiroParcela parcela : parcelasExistentes) {
                // Supondo que 1 seja o status "Aberto" (conforme definido no gerenciarNovoLancamento)
                if (parcela.getStatus() == 1) { 
                    parcela.setValor_original(novoValorPorParcela);
                    parcela.setValor_final(novoValorPorParcela);
                }
            }
            
            // 5. Salva em lote a lista de parcelas com os valores atualizados
            financeiroDAO.salvarParcelas(parcelasExistentes);
        }
    }

    public void excluirLancamento(int id) throws Exception {
        financeiroDAO.excluir(id);
    }
}