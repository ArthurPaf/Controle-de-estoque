package venda.p2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "compra_produto") 
public class CompraProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    @ManyToOne
    @JoinColumn(name = "compra_id", referencedColumnName = "id", nullable = false)
    private Compra compra;

    @ManyToOne
    @JoinColumn(name = "produto_id", referencedColumnName = "id", nullable = false)
    private Produto produto;

    private double quantidade;
    private double valorUnitario;

    
    public CompraProduto() {
    }

    
    public CompraProduto(int id, Compra compra, Produto produto, double quantidade, double valorUnitario) {
        this.id = id;
        this.compra = compra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public Double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }
}