package venda.p2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "fornecedor_produto") 
public class FornecedorProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id") 
    private Fornecedor fornecedor;

    @ManyToOne
    @JoinColumn(name = "produto_id") 
    private Produto produto;

    
    public FornecedorProduto() {
    }

    
    public FornecedorProduto(int id, Fornecedor fornecedor, Produto produto) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.produto = produto;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}