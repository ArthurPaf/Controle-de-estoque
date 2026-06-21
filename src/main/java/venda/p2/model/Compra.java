package venda.p2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "compra")
public class Compra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;
    
    @Column(name = "data_compra")
    private LocalDate dataCompra;
    private Double valorTotal;

    @ManyToOne
    @JoinColumn(name = "fornecedor_id") 
    private Fornecedor fornecedor;

    
    @OneToMany(mappedBy = "compra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompraProduto> compraProdutos = new ArrayList<>();

    
    public Compra() {
    }

    
    public Compra(int id, LocalDate dataCompra, Double valorTotal, Fornecedor fornecedor) {
        this.id = id;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
        this.fornecedor = fornecedor;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<CompraProduto> getCompraProdutos() {
        return compraProdutos;
    }

    public void setCompraProdutos(List<CompraProduto> compraProdutos) {
        this.compraProdutos = compraProdutos;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }
}