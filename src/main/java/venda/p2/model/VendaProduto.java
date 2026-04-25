package venda.p2.model;

public class VendaProduto {
    private int id;
    private Venda venda;
    private Produto produto;
    private Double quantidade;
    private Double valorUnitario;

    
    public VendaProduto() {
    }

    
    public VendaProduto(int id, Venda venda, Produto produto, Double quantidade, Double valorUnitario) {
        this.id = id;
        this.venda = venda;
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

   
    public Venda getVenda() {
        return venda;
    }

    
    public void setVenda(Venda venda) {
        this.venda = venda;
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
