package venda.p2.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Compra {
    private int id;
    private LocalDate dataCompra;
    private Double valorTotal;
    private Fornecedor fornecedor;

    
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

    private List<CompraProduto> compraProdutos = new ArrayList<>();

    
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