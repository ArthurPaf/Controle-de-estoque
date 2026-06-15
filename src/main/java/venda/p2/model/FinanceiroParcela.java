package venda.p2.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "financeiro_parcela")
public class FinanceiroParcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "numeroparcela", nullable = false)
    private int n_parcela;

    @Column(name = "datavencimento")
    @Temporal(TemporalType.DATE)
    private Date data_vencimento;

    @Column(name = "valorparcela")
    private double valor_original;

    @Column(name = "status")
    private int status;

    @Column(name = "datapagamento", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date data_pagamento;

    @Column(name = "valor_final")
    private double valor_final;

    @Column(name = "desconto")
    private double desconto;

    @Column(name = "acrescimo")
    private double acrescimo;

    @ManyToOne
    @JoinColumn(name = "financeiro_id")
    private Financeiro financeiro;

    public FinanceiroParcela() {
    }

    // --- GETTERS E SETTERS ---

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getN_parcela() { return n_parcela; }
    public void setN_parcela(int n_parcela) { this.n_parcela = n_parcela; }

    public Date getData_vencimento() { return data_vencimento; }
    public void setData_vencimento(Date data_vencimento) { this.data_vencimento = data_vencimento; }

    public double getValor_original() { return valor_original; }
    public void setValor_original(double valor_original) { this.valor_original = valor_original; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Date getData_pagamento() { return data_pagamento; }
    public void setData_pagamento(Date data_pagamento) { this.data_pagamento = data_pagamento; }

    public double getValor_final() { return valor_final; }
    public void setValor_final(double valor_final) { this.valor_final = valor_final; }

    public double getDesconto() { return desconto; }
    public void setDesconto(double desconto) { this.desconto = desconto; } // <-- Corrigido aqui

    public double getAcrescimo() { return acrescimo; }
    public void setAcrescimo(double acrescimo) { this.acrescimo = acrescimo; }

    public Financeiro getFinanceiro() { return financeiro; }
    public void setFinanceiro(Financeiro financeiro) { this.financeiro = financeiro; }
}