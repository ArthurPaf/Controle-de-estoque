package venda.p2.model;

public class Produto {
    private int id;
    private String nome;
    private Double preco_medio;
    private Double qtde_estoque;
    private Double valor_ultima_compra;
    private Double valor_ultima_venda;
    private Categoria categoria;

    /**
     * 
     */
    public Produto() {
    }

    /**
     * @param id
     * @param nome
     * @param preco
     * @param quantidade
     * @param categoria
     */
    public Produto(int id, String nome, Double preco_medio, Double qtde_estoque, Categoria categoria) {
        this.id = id;
        this.nome = nome;
        this.preco_medio = preco_medio;
        this.qtde_estoque = qtde_estoque;
        this.categoria = categoria;
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
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the preco
     */
    public Double getPreco() {
        return preco_medio;
    }

    /**
     * @param preco the preco to set
     */
    public void setPreco(Double preco) {
        this.preco_medio = preco;
    }

    /**
     * @return the quantidade
     */
    public Double getQuantidade() {
        return qtde_estoque;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(Double quantidade) {
        this.qtde_estoque = quantidade;
    }

    public Double getValor_ultima_compra() {
        return valor_ultima_compra;
    }

    public void setValor_ultima_compra(Double valor_ultima_compra) {
        this.valor_ultima_compra = valor_ultima_compra;
    }

    public Double getValor_ultima_venda() {
        return valor_ultima_venda;
    }

    public void setValor_ultima_venda(Double valor_ultima_venda) {
        this.valor_ultima_venda = valor_ultima_venda;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
