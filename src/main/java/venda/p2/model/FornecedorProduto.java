package venda.p2.model;

public class FornecedorProduto {
    private int id;
    private Fornecedor fornecedor;
    private Produto produto;

    /**
     * 
     */
    public FornecedorProduto() {
    }

    /**
     * @param id
     * @param fornecedor
     * @param produto
     */
    public FornecedorProduto(int id, Fornecedor fornecedor, Produto produto) {
        this.id = id;
        this.fornecedor = fornecedor;
        this.produto = produto;
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
     * @return the fornecedor
     */
    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    /**
     * @param fornecedor the fornecedor to set
     */
    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
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

}
