package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.ProdutoDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Produto;
import venda.p2.model.Categoria;
import java.util.List;

public class ProdutoController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO PARA PRODUTO
    private static final Logger logger = LogManager.getLogger(ProdutoController.class);

    private ProdutoDAO produtoDAO;
    private GenericDAO<Categoria> categoriaDAO;

    public ProdutoController() {
        this.produtoDAO = new ProdutoDAO();
        this.categoriaDAO = new GenericDAO<>(Categoria.class);
    }

    public List<Produto> listarTodos() throws Exception {
        logger.info("Método listarTodos() executado no ProdutoController.");
        return produtoDAO.listarTodos();
    }

    public List<Categoria> listarCategorias() throws Exception {
        logger.info("Método listarCategorias() executado no ProdutoController.");
        return categoriaDAO.listarTodos();
    }

    public Produto buscarPorId(int id) throws Exception {
        logger.info("Método buscarPorId() executado para o Produto ID: {}", id);
        return produtoDAO.buscarPorId(id);
    }

    public void salvarProduto(String nome, String precoStr, String quantidadeStr, Categoria categoria) throws Exception {
        logger.info("Método salvarProduto() iniciado.");

        try {
            validarCampos(nome, precoStr, quantidadeStr, categoria);

            Produto p = new Produto();
            p.setNome(nome.trim());
            p.setPreco(Double.parseDouble(precoStr.trim()));
            p.setQuantidade(Double.parseDouble(quantidadeStr.trim()));
            p.setCategoria(categoria);

            produtoDAO.salvar(p);
            logger.info("Novo Produto '{}' cadastrado com sucesso no estoque inicial de {}.", p.getNome(), p.getQuantidade());
        } catch (Exception e) {
            logger.error("Erro ao salvar produto: {}", e.getMessage());
            throw e;
        }
    }

    public void atualizarProduto(Produto p, String nome, String precoStr, String quantidadeStr, Categoria categoria) throws Exception {
        logger.info("Método atualizarProduto() iniciado para o Produto ID: {}", p != null ? p.getId() : "NULO");

        if (p == null) {
            logger.warn("Tentativa de atualização abortada: nenhum produto selecionado.");
            throw new Exception("Nenhum produto selecionado.");
        }

        try {
            validarCampos(nome, precoStr, quantidadeStr, categoria);

            p.setNome(nome.trim());
            p.setPreco(Double.parseDouble(precoStr.trim()));
            p.setQuantidade(Double.parseDouble(quantidadeStr.trim()));
            p.setCategoria(categoria);

            produtoDAO.salvar(p);
            logger.info("Produto ID: {} atualizado com sucesso. Novo Nome: '{}' | Novo Estoque: {}", p.getId(), p.getNome(), p.getQuantidade());
        } catch (Exception e) {
            logger.error("Erro ao atualizar produto ID {}: {}", p.getId(), e.getMessage());
            throw e;
        }
    }

    public void excluirProduto(int id) throws Exception {
        logger.info("Método excluirProduto() executado para o ID: {}", id);
        try {
            produtoDAO.excluir(id);
            logger.info("Produto ID: {} removido com sucesso do sistema.", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir produto ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    private void validarCampos(String nome, String precoStr, String quantidadeStr, Categoria categoria) throws Exception {
        if (nome == null || nome.trim().isEmpty() || precoStr == null || precoStr.trim().isEmpty() || quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
            logger.warn("Validação de Produto falhou: existem campos obrigatórios em branco.");
            throw new Exception("Preencha todos os campos obrigatórios!");
        }
        if (categoria == null) {
            logger.warn("Validação de Produto falhou: Categoria ausente.");
            throw new Exception("É necessário selecionar uma Categoria válida!");
        }
        try {
            Double.parseDouble(precoStr.trim());
            Double.parseDouble(quantidadeStr.trim());
        } catch (NumberFormatException e) {
            logger.warn("Validação de Produto falhou: Preço ou Estoque com formato numérico inválido.");
            throw new Exception("Preço e Estoque devem ser números válidos.");
        }
    }
}