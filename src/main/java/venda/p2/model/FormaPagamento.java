package venda.p2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "forma_pagamento")
public class FormaPagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    
    private String nome;
    private int qtde_parcela;
    private int prazo;
    private String avista_aprazo; 

    
    public FormaPagamento() {
    }

    
    public FormaPagamento(String nome, int qtde_parcela, int prazo, String avista_aprazo) {
        this.nome = nome;
        this.qtde_parcela = qtde_parcela;
        this.prazo = prazo;
        this.avista_aprazo = avista_aprazo;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtde_parcela() {
        return qtde_parcela;
    }

    public void setQtde_parcela(int qtde_parcela) {
        this.qtde_parcela = qtde_parcela;
    }

    public int getPrazo() {
        return prazo;
    }

    public void setPrazo(int prazo) {
        this.prazo = prazo;
    }

    public String getAvista_aprazo() {
        return avista_aprazo;
    }

    public void setAvista_aprazo(String avista_aprazo) {
        this.avista_aprazo = avista_aprazo;
    }
}