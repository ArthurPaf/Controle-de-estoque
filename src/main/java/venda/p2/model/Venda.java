package venda.p2.model;

import java.time.LocalDate;
import java.util.List;

public class Venda {
    private int id;
    private Cliente cliente;
    private LocalDate dataVenda;
    private Double valorTotal;
    private List<VendaProduto> vendaProdutos;

    /**
     * 
     */
    public Venda() {
    }

    /**
     * @param id
     * @param cliente
     * @param dataVenda
     * @param valorTotal
     * 
     */
    public Venda(int id, Cliente cliente, LocalDate dataVenda, Double valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;

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
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    /**
     * @return the dataVenda
     */
    public LocalDate getDataVenda() {
        return dataVenda;
    }

    /**
     * @param dataVenda the dataVenda to set
     */
    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
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
     * @return the vendaProdutos
     */
    public List<VendaProduto> getVendaProdutos() {
        return vendaProdutos;
    }

    /**
     * @param vendaProdutos the vendaProdutos to set
     */
    public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
        this.vendaProdutos = vendaProdutos;
    }

}
