package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.TipoContaDAO;
import venda.p2.model.TipoConta;
import java.util.List;

public class TipoContaController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO PARA TIPO DE CONTA
    private static final Logger logger = LogManager.getLogger(TipoContaController.class);

    private TipoContaDAO tipoContaDAO;

    public TipoContaController() {
        this.tipoContaDAO = new TipoContaDAO();
    }

    public List<TipoConta> listarTodos() throws Exception {
        logger.info("Método listarTodos() executado no TipoContaController.");
        return tipoContaDAO.listarTodos();
    }

    public TipoConta buscarPorId(int id) throws Exception {
        logger.info("Método buscarPorId() executado para o Tipo de Conta ID: {}", id);
        return tipoContaDAO.buscarPorId(id);
    }

    public void salvarTipoConta(String descricao) throws Exception {
        logger.info("Método salvarTipoConta() iniciado.");

        if (descricao == null || descricao.trim().isEmpty()) {
            logger.warn("Tentativa de cadastro rejeitada: descrição do tipo de conta está em branco.");
            throw new Exception("A descrição é obrigatória!");
        }

        try {
            TipoConta tc = new TipoConta();
            tc.setDescricao(descricao.trim());
            
            tipoContaDAO.salvar(tc);
            logger.info("Novo Tipo de Conta '{}' cadastrado com sucesso.", tc.getDescricao());
        } catch (Exception e) {
            logger.error("Erro crítico ao salvar tipo de conta: {}", e.getMessage());
            throw e;
        }
    }

    public void atualizarTipoConta(TipoConta tc, String descricao) throws Exception {
        logger.info("Método atualizarTipoConta() iniciado para o Tipo de Conta ID: {}", tc != null ? tc.getId() : "NULO");

        if (tc == null) throw new Exception("Nenhum tipo de conta selecionado.");
        if (descricao == null || descricao.trim().isEmpty()) {
            logger.warn("Tentativa de atualização rejeitada: descrição do tipo de conta está em branco.");
            throw new Exception("A descrição é obrigatória!");
        }

        try {
            tc.setDescricao(descricao.trim());
            
            tipoContaDAO.salvar(tc);
            logger.info("Tipo de Conta ID: {} atualizado com sucesso. Nova Descrição: '{}'", tc.getId(), tc.getDescricao());
        } catch (Exception e) {
            logger.error("Erro crítico ao atualizar tipo de conta ID {}: {}", tc.getId(), e.getMessage());
            throw e;
        }
    }

    public void excluirTipoConta(int id) throws Exception {
        logger.info("Método excluirTipoConta() executado para o ID: {}", id);
        try {
            tipoContaDAO.excluir(id);
            logger.info("Tipo de Conta ID: {} excluído com sucesso do sistema.", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir tipo de conta ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}