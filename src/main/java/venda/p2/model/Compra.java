package venda.p2.model;

import java.time.LocalDate;
import java.util.List;

public class Compra {
    private int id;
    private LocalDate dataCompra;
    private Double valorTotal;
    private List<CompraProduto> compraProdutos;
    private Fornecedor fornecedor;

    /**
     * 
     */
    public Compra() {
    }

    /**
     * @param id
     * @param cliente
     * @param dataCompra
     * @param valorTotal
     * 
     */
    public Compra(int id, LocalDate dataCompra, Double valorTotal, Fornecedor fornecedor) {
        this.id = id;
        this.dataCompra = dataCompra;
        this.valorTotal = valorTotal;
        this.fornecedor = fornecedor;

    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the dataCompra
     */
    public LocalDate getDataCompra() {
        return dataCompra;
    }

    /**
     * @param dataCompra the dataCompra to set
     */
    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    /**
     * @return the valorTotal
     */
    public Double getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the CompraProdutos
     */
    public List<CompraProduto> getCompraProdutos() {
        return compraProdutos;
    }

    /**
     * @param compraProdutos the compraProdutos to set
     */
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