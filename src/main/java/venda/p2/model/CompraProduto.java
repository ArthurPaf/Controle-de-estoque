package venda.p2.model;

public class CompraProduto {
    private int id;
    private Compra compra;
    private Produto produto;
    private Double quantidade;
    private Double valorUnitario;

    
    public CompraProduto() {
    }

    
    public CompraProduto(int id, Compra compra, Produto produto, Double quantidade, Double valorUnitario) {
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
