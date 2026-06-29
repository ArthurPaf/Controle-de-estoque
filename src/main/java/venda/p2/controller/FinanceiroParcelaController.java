package venda.p2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.FinanceiroParcelaDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class FinanceiroParcelaController {

    
    private static final Logger logger = LogManager.getLogger(FinanceiroParcelaController.class);

    private FinanceiroParcelaDAO parcelaDAO;
    private GenericDAO<Financeiro> financeiroDAO;

    public FinanceiroParcelaController() {
        this.parcelaDAO = new FinanceiroParcelaDAO();
        this.financeiroDAO = new GenericDAO<>(Financeiro.class);
    }

    public List<Financeiro> listarTodasContas() throws Exception {
        logger.info("Método listarTodasContas() executado.");
        return financeiroDAO.listarTodos();
    }

    public FinanceiroParcela buscarParcelaPorId(int id) throws Exception {
        logger.info("Método buscarParcelaPorId() executado para o ID: {}", id);
        return parcelaDAO.buscarPorId(id);
    }

    public Financeiro buscarContaPorId(int id) throws Exception {
        logger.info("Método buscarContaPorId() executado para o ID: {}", id);
        return financeiroDAO.buscarPorId(id); 
    }

    
    public List<FinanceiroParcela> listarParcelasPorConta(int financeiroId) throws Exception {
        logger.info("Método listarParcelasPorConta() executado para o Lançamento ID: {}", financeiroId);
        
        List<FinanceiroParcela> todas = parcelaDAO.listarTodos();
        List<FinanceiroParcela> filtradas = new ArrayList<>();
        
        for (FinanceiroParcela p : todas) {
            if (p.getFinanceiro() != null && p.getFinanceiro().getId() == financeiroId) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    
    public void efetuarBaixa(FinanceiroParcela p, String descontoStr, String acrescimoStr) throws Exception {
        logger.info("Método efetuarBaixa() iniciado.");

        if (p == null) {
            logger.warn("Tentativa de baixa abortada: nenhuma parcela informada.");
            throw new Exception("Nenhuma parcela selecionada.");
        }
        if (p.getStatus() != 1) {
            logger.warn("Tentativa de baixa recusada: Parcela ID {} já está baixada (Status: {}).", p.getId(), p.getStatus());
            throw new Exception("Esta parcela já se encontra baixada.");
        }

        try {
            double desc = Double.parseDouble(descontoStr.trim());
            double acr = Double.parseDouble(acrescimoStr.trim());
            double valorFinal = p.getValor_original() - desc + acr;

            p.setDesconto(desc);
            p.setAcrescimo(acr);
            p.setValor_final(valorFinal);
            p.setData_pagamento(new Date());
            p.setStatus(2); 

            parcelaDAO.salvar(p);
            
            logger.info("MOVIMENTAÇÃO FINANCEIRA - Parcela ID: {} baixada com sucesso. Valor Original: R$ {} | Desconto: R$ {} | Acréscimo: R$ {} | Valor Final Pago: R$ {}", 
                        p.getId(), p.getValor_original(), desc, acr, valorFinal);

        } catch (NumberFormatException e) {
            logger.error("Falha ao converter valores de desconto ('{}') ou acréscimo ('{}') para número.", descontoStr, acrescimoStr);
            throw new Exception("Valores informados para desconto ou acréscimo são inválidos.");
        } catch (Exception e) {
            logger.error("Erro crítico ao salvar a baixa da parcela ID {}: {}", p.getId(), e.getMessage());
            throw e;
        }
    }
}