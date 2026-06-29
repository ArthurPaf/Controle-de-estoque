package venda.p2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.CategoriaDAO;
import venda.p2.model.Categoria;
import java.util.List;

public class CategoriaController {

    
    private static final Logger logger = LogManager.getLogger(CategoriaController.class);

    private CategoriaDAO categoriaDAO;

    public CategoriaController() {
        this.categoriaDAO = new CategoriaDAO(); 
    }

    public List<Categoria> listarCategorias() throws Exception {
        
        logger.info("Método listarCategorias() executado.");
        return categoriaDAO.listarTodos();
    }

    public Categoria buscarPorId(int id) throws Exception {
        
        logger.info("Método buscarPorId() executado para o ID: {}", id);
        return categoriaDAO.buscarPorId(id);
    }

    public List<Categoria> pesquisarPorNome(String nome) throws Exception {
        
        logger.info("Método pesquisarPorNome() executado com o termo: '{}'", nome);
        
        if (nome == null) {
            nome = "";
        }
        
        return categoriaDAO.buscarPorNome(nome.trim());
    }

    public void salvarCategoria(Categoria categoria) throws Exception {
        
        logger.info("Método salvarCategoria() executado.");

        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            
            logger.warn("Tentativa de salvar categoria com nome inválido ou vazio.");
            throw new Exception("Nome inválido!");
        }
        
        try {
            categoriaDAO.salvar(categoria);
            
            logger.info("Categoria '{}' salva com sucesso.", categoria.getNome());
        } catch (Exception e) {
            
            logger.error("Erro ao salvar categoria: {}", e.getMessage());
            throw e;
        }
    }

    public void excluirCategoria(int id) throws Exception {
        
        logger.info("Método excluirCategoria() executado para o ID: {}", id);
        
        try {
            categoriaDAO.excluir(id);
            
            logger.info("Categoria ID: {} excluída com sucesso.", id);
        } catch (Exception e) {
            
            logger.error("Erro ao excluir categoria ID: {}: {}", id, e.getMessage());
            throw e;
        }
    }
}