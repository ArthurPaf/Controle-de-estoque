package venda.p2.model;

public class Fornecedor {
    private int id;
    private String nome_Fantasia;
    private String razao_Social;
    private String cnpj;

    /**
     * 
     */
    public Fornecedor() {
    }

    /**
     * @param id
     * @param nomeFantasia
     * @param razaoSocial
     * @param cnpj
     */
    public Fornecedor(int id, String nomeFantasia, String razaoSocial, String cnpj) {
        this.id = id;
        this.nome_Fantasia = nomeFantasia;
        this.razao_Social = razaoSocial;
        this.cnpj = cnpj;
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
     * @return the nomeFantasia
     */
    public String getNomeFantasia() {
        return nome_Fantasia;
    }

    /**
     * @param nomeFantasia the nomeFantasia to set
     */
    public void setNomeFantasia(String nomeFantasia) {
        this.nome_Fantasia = nomeFantasia;
    }

    /**
     * @return the razaoSocial
     */
    public String getRazaoSocial() {
        return razao_Social;
    }

    /**
     * @param razaoSocial the razaoSocial to set
     */
    public void setRazaoSocial(String razaoSocial) {
        this.razao_Social = razaoSocial;
    }

    /**
     * @return the cnpj
     */
    public String getCnpj() {
        return cnpj;
    }

    /**
     * @param cnpj the cnpj to set
     */
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

}
