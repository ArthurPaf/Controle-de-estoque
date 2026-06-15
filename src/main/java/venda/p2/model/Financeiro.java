package venda.p2.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "financeiro")
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date data_conta;

    private int pagar_ou_receber; // Ex: 1 para Pagar (Saída), 2 para Receber (Entrada)

    @ManyToOne
    @JoinColumn(name = "tipo_conta_id")
    private TipoConta tipoConta;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "compra_id", nullable = true)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "venda_id", nullable = true)
    private Venda venda;

    private double valor_total;

    public Financeiro() {
        this.data_conta = new Date();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getData_conta() { return data_conta; }
    public void setData_conta(Date data_conta) { this.data_conta = data_conta; }

    public int getPagar_ou_receber() { return pagar_ou_receber; }
    public void setPagar_ou_receber(int pagar_ou_receber) { this.pagar_ou_receber = pagar_ou_receber; }

    public TipoConta getTipoConta() { return tipoConta; }
    public void setTipoConta(TipoConta tipoConta) { this.tipoConta = tipoConta; }

    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento formaPagamento) { this.formaPagamento = formaPagamento; }

    public Compra getCompra() { return compra; }
    public void setCompra(Compra compra) { this.compra = compra; }

    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }

    public double getValor_total() { return valor_total; }
    public void setValor_total(double valor_total) { this.valor_total = valor_total; }
}