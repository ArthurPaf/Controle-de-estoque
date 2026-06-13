package venda.p2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "financeiro")
public class Financeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    
    private String pagar_receber; 
    private Double valorTotal;
    private LocalDate dataLancamento;

    @ManyToOne
    @JoinColumn(name = "tipo_conta_id") 
    private TipoConta tipoConta;

    @ManyToOne
    @JoinColumn(name = "forma_pagamento_id") 
    private FormaPagamento formaPagamento;

    
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id", nullable = true)
    private Fornecedor fornecedor;

    
    @OneToOne
    @JoinColumn(name = "venda_id", nullable = true)
    private Venda venda;

    @OneToOne
    @JoinColumn(name = "compra_id", nullable = true)
    private Compra compra;

    
    @OneToMany(mappedBy = "financeiro", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinanceiroParcela> parcelas = new ArrayList<>();

    // Construtor padrão
    public Financeiro() {
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPagar_receber() {
        return pagar_receber;
    }

    public void setPagar_receber(String pagar_receber) {
        this.pagar_receber = pagar_receber;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public void setTipoConta(TipoConta tipoConta) {
        this.tipoConta = tipoConta;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public List<FinanceiroParcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<FinanceiroParcela> parcelas) {
        this.parcelas = parcelas;
    }
}