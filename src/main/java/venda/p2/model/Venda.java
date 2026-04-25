package venda.p2.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Venda {
    private int id;
    private Cliente cliente;
    private LocalDate dataVenda;
    private Double valorTotal;

   
    public Venda() {
    }

    
    public Venda(int id, Cliente cliente, LocalDate dataVenda, Double valorTotal) {
        this.id = id;
        this.cliente = cliente;
        this.dataVenda = dataVenda;
        this.valorTotal = valorTotal;

    }

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public Cliente getCliente() {
        return cliente;
    }

    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    
    public LocalDate getDataVenda() {
        return dataVenda;
    }

    
    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    
    public Double getValorTotal() {
        return valorTotal;
    }

    
    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    // instanciando a lista de produtos para n ficar NULA e dar pau no código
    private List<VendaProduto> vendaProdutos = new ArrayList<>();
    
    public List<VendaProduto> getVendaProdutos() {
        return vendaProdutos;
    }

    
    public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
        this.vendaProdutos = vendaProdutos;
    }

}
