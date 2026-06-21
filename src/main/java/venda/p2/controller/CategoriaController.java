package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.CategoriaDAO;
import venda.p2.model.Categoria;
import java.util.List;

public class CategoriaController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO PARA CATEGORIA
    private static final Logger logger = LogManager.getLogger(CategoriaController.class);

    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO(); // Instancia o DAO focado em Categoria
    }

    public List<Categoria> listarCategorias() throws Exception {
        // LOG ADICIONADO
        logger.info("Método listarCategorias() executado.");
        return categoriaDAO.listarTodos();
    }

    public Categoria buscarPorId(int id) throws Exception {
        // LOG ADICIONADO
        logger.info("Método buscarPorId() executado para o ID: {}", id);
        return categoriaDAO.buscarPorId(id);
    }

    public void salvarCategoria(Categoria categoria) throws Exception {
        // LOG ADICIONADO
        logger.info("Método salvarCategoria() executado.");

        // Validação de regra de negócio antes de mandar pro banco
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            // LOG ADICIONADO (Usa warn para validações de tela que falharam)
            logger.warn("Tentativa de salvar categoria com nome inválido ou vazio.");
            throw new Exception("Nome inválido!");
        }
        
        try {
            categoriaDAO.salvar(categoria);
            // LOG ADICIONADO
            logger.info("Categoria '{}' salva com sucesso.", categoria.getNome());
        } catch (Exception e) {
            // LOG ADICIONADO
            logger.error("Erro ao salvar categoria: {}", e.getMessage());
            throw e;
        }
    }

    public void excluirCategoria(int id) throws Exception {
        // LOG ADICIONADO
        logger.info("Método excluirCategoria() executado para o ID: {}", id);
        
        try {
            categoriaDAO.excluir(id);
            // LOG ADICIONADO
            logger.info("Categoria ID: {} excluída com sucesso.", id);
        } catch (Exception e) {
            // LOG ADICIONADO
            logger.error("Erro ao excluir categoria ID: {}: {}", id, e.getMessage());
            throw e;
        }
    }
}