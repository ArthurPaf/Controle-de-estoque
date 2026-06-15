package venda.p2.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "financeiro_parcela")
public class FinanceiroParcela {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "numeroparcela")
    private int n_parcela;

    @Temporal(TemporalType.DATE)
    private Date data_vencimento;

    @Temporal(TemporalType.DATE)
    private Date data_pagamento;

    private double valor_original;
    private double desconto;
    private double acrescimo;
    private double valor_final;
    private int status; // Ex: 1 para Aberto, 2 para Pago

    @ManyToOne
    @JoinColumn(name = "financeiro_id")
    private Financeiro financeiro;

    public FinanceiroParcela() {
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getN_parcela() { return n_parcela; }
    public void setN_parcela(int n_parcela) { this.n_parcela = n_parcela; }

    public Date getData_vencimento() { return data_vencimento; }
    public void setData_vencimento(Date data_vencimento) { this.data_vencimento = data_vencimento; }

    public Date getData_pagamento() { return data_pagamento; }
    public void setData_pagamento(Date data_pagamento) { this.data_pagamento = data_pagamento; }

    public double getValor_original() { return valor_original; }
    public void setValor_original(double valor_original) { this.valor_original = valor_original; }

    public double getDesconto() { return desconto; }
    public void setDesconto(double desconto) { this.desconto = desconto; }

    public double getAcrescimo() { return acrescimo; }
    public void setAcrescimo(double acrescimo) { this.acrescimo = acrescimo; }

    public double getValor_final() { return valor_final; }
    public void setValor_final(double valor_final) { this.valor_final = valor_final; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public Financeiro getFinanceiro() { return financeiro; }
    public void setFinanceiro(Financeiro financeiro) { this.financeiro = financeiro; }
}