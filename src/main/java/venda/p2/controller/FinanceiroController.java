package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.FinanceiroDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import venda.p2.model.FormaPagamento;
import venda.p2.model.TipoConta;
import java.util.ArrayList;
import java.util.List;

public class FinanceiroController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO PARA FINANCEIRO
    private static final Logger logger = LogManager.getLogger(FinanceiroController.class);

    private FinanceiroDAO financeiroDAO;
    private GenericDAO<TipoConta> tipoContaDAO;
    private GenericDAO<FormaPagamento> formaPagamentoDAO;

    public FinanceiroController() {
        this.financeiroDAO = new FinanceiroDAO();
        this.tipoContaDAO = new GenericDAO<>(TipoConta.class);
        this.formaPagamentoDAO = new GenericDAO<>(FormaPagamento.class);
    }

    public List<TipoConta> listarTiposConta() throws Exception {
        logger.info("Método listarTiposConta() executado.");
        return tipoContaDAO.listarTodos();
    }

    public List<FormaPagamento> listarFormasPagamento() throws Exception {
        logger.info("Método listarFormasPagamento() executado.");
        return formaPagamentoDAO.listarTodos();
    }

    public List<Financeiro> listarLancamentos() throws Exception {
        logger.info("Método listarLancamentos() executado.");
        return financeiroDAO.listarTodos();
    }

    public Financeiro buscarPorId(int id) throws Exception {
        logger.info("Método buscarPorId() executado para o ID: {}", id);
        return financeiroDAO.buscarPorId(id);
    }

    public List<FinanceiroParcela> obterParcelas(int idFinanceiro) throws Exception {
        logger.info("Método obterParcelas() executado para o Lançamento ID: {}", idFinanceiro);
        return financeiroDAO.listarParcelasPorLancamento(idFinanceiro);
    }

    public void gerenciarNovoLancamento(int tipoMovimentacao, TipoConta tc, FormaPagamento fp, String valorStr) throws Exception {
        logger.info("Método gerenciarNovoLancamento() iniciado.");

        if (valorStr == null || valorStr.trim().isEmpty()) {
            logger.warn("Tentativa de lançamento com valor em branco/nulo.");
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
        logger.info("Lançamento Pai salvo. ID Gerado: {} | Tipo: {}", fSalvo.getId(), (fSalvo.getPagar_ou_receber() == 1 ? "PAGAR" : "RECEBER"));

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
            
            logger.info("Gerando Parcela {}/{} | Vencimento: {} | Valor: R$ {}", i, qtdParcelas, dataVencimentoAtual, valorPorParcela);
        }

        try {
            financeiroDAO.salvarParcelas(listaParcelas);
            logger.info("Lote com {} parcelas salvo com sucesso para o Lançamento ID: {}.", qtdParcelas, fSalvo.getId());
        } catch (Exception e) {
            logger.error("Erro ao salvar parcelas do lançamento ID {}: {}", fSalvo.getId(), e.getMessage());
            throw e;
        }
    }

    public void atualizarLancamento(Financeiro f) throws Exception {
        logger.info("Método atualizarLancamento() executado para o Lançamento ID: {}", f.getId());
        
        try {
            // 1. Atualiza os dados do Lançamento Pai no banco
            financeiroDAO.salvar(f);
            
            // 2. Busca as parcelas que já existem associadas a este lançamento
            List<FinanceiroParcela> parcelasExistentes = financeiroDAO.listarParcelasPorLancamento(f.getId());
            
            if (parcelasExistentes != null && !parcelasExistentes.isEmpty()) {
                // 3. Regra de Negócio: Divide o NOVO valor total pela quantidade de parcelas que já existem
                double novoValorPorParcela = f.getValor_total() / parcelasExistentes.size();
                logger.info("Recalculando parcelas existentes. Nova cota por parcela: R$ {}", novoValorPorParcela);
                
                // 4. Varre a lista de parcelas aplicando o novo valor apenas nas que estão em aberto
                for (FinanceiroParcela parcela : parcelasExistentes) {
                    if (parcela.getStatus() == 1) { 
                        parcela.setValor_original(novoValorPorParcela);
                        parcela.setValor_final(novoValorPorParcela);
                    }
                }
                
                // 5. Salva em lote a lista de parcelas com os valores atualizados
                financeiroDAO.salvarParcelas(parcelasExistentes);
                logger.info("Valores das parcelas atualizados com sucesso no banco.");
            }
        } catch (Exception e) {
            logger.error("Erro ao atualizar lançamento/parcelas ID {}: {}", f.getId(), e.getMessage());
            throw e;
        }
    }

    public void excluirLancamento(int id) throws Exception {
        logger.info("Método excluirLancamento() executado para o ID: {}", id);
        try {
            financeiroDAO.excluir(id);
            logger.info("Lançamento ID: {} e suas respectivas parcelas foram excluídos.", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir lançamento ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    public List<Financeiro> filtrarLancamentos(int fluxo, TipoConta tipoConta) throws Exception {
    // 1 -> Se fluxo for 0 (TODOS), passamos null ou vazio para a busca dinâmica do DAO
    // 2 -> Se fluxo for 1 (A Pagar), passamos 1
    // 3 -> Se fluxo for 2 (A Receber), passamos 2
    
    // Altere a chamada abaixo de acordo com as variáveis aceitas no seu método multifiltro do DAO
    return financeiroDAO.buscarComFiltros(fluxo, tipoConta);
}
}