package venda.p2.controller;

import venda.p2.dao.ProdutoDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Produto;
import venda.p2.model.Categoria;
import java.util.List;

public class ProdutoController {

    private ProdutoDAO produtoDAO;
    private GenericDAO<Categoria> categoriaDAO;

    public ProdutoController() {
        this.produtoDAO = new ProdutoDAO();
        this.categoriaDAO = new GenericDAO<>(Categoria.class);
    }

    public List<Produto> listarTodos() throws Exception {
        return produtoDAO.listarTodos();
    }

    public List<Categoria> listarCategorias() throws Exception {
        return categoriaDAO.listarTodos();
    }

    public Produto buscarPorId(int id) throws Exception {
        return produtoDAO.buscarPorId(id);
    }

    public void salvarProduto(String nome, String precoStr, String quantidadeStr, Categoria categoria) throws Exception {
        validarCampos(nome, precoStr, quantidadeStr, categoria);

        Produto p = new Produto();
        p.setNome(nome.trim());
        p.setPreco(Double.parseDouble(precoStr.trim()));
        p.setQuantidade(Double.parseDouble(quantidadeStr.trim()));
        p.setCategoria(categoria);

        produtoDAO.salvar(p);
    }

    public void atualizarProduto(Produto p, String nome, String precoStr, String quantidadeStr, Categoria categoria) throws Exception {
        if (p == null) throw new Exception("Nenhum produto selecionado.");
        validarCampos(nome, precoStr, quantidadeStr, categoria);

        p.setNome(nome.trim());
        p.setPreco(Double.parseDouble(precoStr.trim()));
        p.setQuantidade(Double.parseDouble(quantidadeStr.trim()));
        p.setCategoria(categoria);

        produtoDAO.salvar(p);
    }

    public void excluirProduto(int id) throws Exception {
        produtoDAO.excluir(id);
    }

    private void validarCampos(String nome, String precoStr, String quantidadeStr, Categoria categoria) throws Exception {
        if (nome == null || nome.trim().isEmpty() || precoStr == null || precoStr.trim().isEmpty() || quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
            throw new Exception("Preencha todos os campos obrigatórios!");
        }
        if (categoria == null) {
            throw new Exception("É necessário selecionar uma Categoria válida!");
        }
        try {
            Double.parseDouble(precoStr.trim());
            Double.parseDouble(quantidadeStr.trim());
        } catch (NumberFormatException e) {
            throw new Exception("Preço e Estoque devem ser números válidos.");
        }
    }
}