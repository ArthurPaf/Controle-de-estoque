package venda.p2.model;

public class Fornecedor {
    private int id;
    private String nome_fantasia;
    private String razao_social;
    private String cnpj;

   
    public Fornecedor() {
    }

    
    public Fornecedor(int id, String nomeFantasia, String razaoSocial, String cnpj) {
        this.id = id;
        this.nome_fantasia = nomeFantasia;
        this.razao_social = razaoSocial;
        this.cnpj = cnpj;
    }

    
    public int getId() {
        return id;
    }

    
    public void setId(int id) {
        this.id = id;
    }

    
    public String getNomeFantasia() {
        return nome_fantasia;
    }

    
    public void setNomeFantasia(String nomeFantasia) {
        this.nome_fantasia = nomeFantasia;
    }

    
    public String getRazaoSocial() {
        return razao_social;
    }

    
    public void setRazaoSocial(String razaoSocial) {
        this.razao_social = razaoSocial;
    }

    
    public String getCnpj() {
        return cnpj;
    }

    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

}
