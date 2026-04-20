package venda.p2.model;

public class CompraProduto {
    private int id;
    private Compra compra;
    private Produto produto;
    private Double quantidade;
    private Double valorUnitario;

    /**
     * 
     */
    public CompraProduto() {
    }

    /**
     * @param id
     * @param Compra
     * @param produto
     * @param quantidade
     * @param valorUnitario
     */
    public CompraProduto(int id, Compra compra, Produto produto, Double quantidade, Double valorUnitario) {
        this.id = id;
        this.compra = compra;
        this.produto = produto;
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario;
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
     * @return the Compra
     */
    public Compra getCompra() {
        return compra;
    }

    /**
     * @param compra the Compra to set
     */
    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    /**
     * @return the produto
     */
    public Produto getProduto() {
        return produto;
    }

    /**
     * @param produto the produto to set
     */
    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    /**
     * @return the quantidade
     */
    public Double getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the valorUnitario
     */
    public Double getValorUnitario() {
        return valorUnitario;
    }

    /**
     * @param valorUnitario the valorUnitario to set
     */
    public void setValorUnitario(Double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

}
