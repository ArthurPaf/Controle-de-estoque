package venda.p2.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_conta")
public class TipoConta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String descricao;

    public TipoConta() {
    }

    public TipoConta(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    @Override
    public String toString() {
        return this.descricao; 
    }
}