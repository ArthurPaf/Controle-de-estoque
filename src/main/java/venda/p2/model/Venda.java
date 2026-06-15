package venda.p2.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private int id;

    @ManyToOne
    @JoinColumn(name = "cliente_id") 
    private Cliente cliente;

    private LocalDate dataVenda;
    private Double valorTotal;

    
    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VendaProduto> vendaProdutos = new ArrayList<>();

    
    @OneToOne(mappedBy = "venda", cascade = CascadeType.ALL)
    private Financeiro financeiro;

    
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

    public List<VendaProduto> getVendaProdutos() {
        return vendaProdutos;
    }

    public void setVendaProdutos(List<VendaProduto> vendaProdutos) {
        this.vendaProdutos = vendaProdutos;
    }

    public Financeiro getFinanceiro() {
        return financeiro;
    }

    public void setFinanceiro(Financeiro financeiro) {
        this.financeiro = financeiro;
    }
    
}