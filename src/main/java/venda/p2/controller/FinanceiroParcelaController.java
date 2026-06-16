package venda.p2.controller;

import venda.p2.dao.FinanceiroParcelaDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class FinanceiroParcelaController {

    private FinanceiroParcelaDAO parcelaDAO;
    private GenericDAO<Financeiro> financeiroDAO;

    public FinanceiroParcelaController() {
        this.parcelaDAO = new FinanceiroParcelaDAO();
        this.financeiroDAO = new GenericDAO<>(Financeiro.class);
    }

    public List<Financeiro> listarTodasContas() throws Exception {
        return financeiroDAO.listarTodos();
    }

    public FinanceiroParcela buscarParcelaPorId(int id) throws Exception {
        return parcelaDAO.buscarPorId(id);
    }

    public Financeiro buscarContaPorId(int id) throws Exception {
    // Chama o método buscarPorId do seu FinanceiroDAO
        return financeiroDAO.buscarPorId(id); 
    }

    // Filtra as parcelas pertencentes a um lançamento específico
    public List<FinanceiroParcela> listarParcelasPorConta(int financeiroId) throws Exception {
        List<FinanceiroParcela> todas = parcelaDAO.listarTodos();
        List<FinanceiroParcela> filtradas = new ArrayList<>();
        
        for (FinanceiroParcela p : todas) {
            if (p.getFinanceiro() != null && p.getFinanceiro().getId() == financeiroId) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    // Executa as regras de negócio para a baixa
    public void efetuarBaixa(FinanceiroParcela p, String descontoStr, String acrescimoStr) throws Exception {
        if (p == null) throw new Exception("Nenhuma parcela selecionada.");
        if (p.getStatus() != 1) throw new Exception("Esta parcela já se encontra baixada.");

        double desc = Double.parseDouble(descontoStr.trim());
        double acr = Double.parseDouble(acrescimoStr.trim());
        double valorFinal = p.getValor_original() - desc + acr;

        p.setDesconto(desc);
        p.setAcrescimo(acr);
        p.setValor_final(valorFinal);
        p.setData_pagamento(new Date());
        p.setStatus(2); // Código 2 para "Pago/Baixado"

        parcelaDAO.salvar(p);
    }
}