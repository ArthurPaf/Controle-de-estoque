package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.FornecedorDAO;
import venda.p2.model.Fornecedor;
import java.util.List;

public class FornecedorController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO PARA FORNECEDOR
    private static final Logger logger = LogManager.getLogger(FornecedorController.class);

    private FornecedorDAO fornecedorDAO;

    public FornecedorController() {
        this.fornecedorDAO = new FornecedorDAO();
    }

    public List<Fornecedor> listarTodos() throws Exception {
        logger.info("Método listarTodos() executado no FornecedorController.");
        return fornecedorDAO.listarTodos();
    }

    public Fornecedor buscarPorId(int id) throws Exception {
        logger.info("Método buscarPorId() executado para o Fornecedor ID: {}", id);
        return fornecedorDAO.buscarPorId(id);
    }

    public void salvarFornecedor(String nomeFantasia, String razaoSocial, String cnpj) throws Exception {
        logger.info("Método salvarFornecedor() iniciado.");

        if (nomeFantasia == null || nomeFantasia.trim().isEmpty() || cnpj == null || cnpj.trim().isEmpty()) {
            logger.warn("Tentativa de cadastro rejeitada: Nome Fantasia ou CNPJ ausentes.");
            throw new Exception("Nome Fantasia e CNPJ são obrigatórios!");
        }

        try {
            Fornecedor f = new Fornecedor();
            f.setNomeFantasia(nomeFantasia.trim());
            f.setRazaoSocial(razaoSocial.trim());
            f.setCnpj(cnpj.trim());

            fornecedorDAO.salvar(f);
            logger.info("Novo Fornecedor '{}' (CNPJ: {}) cadastrado com sucesso.", f.getNomeFantasia(), f.getCnpj());
        } catch (Exception e) {
            logger.error("Erro crítico ao salvar fornecedor: {}", e.getMessage());
            throw e;
        }
    }

    public void atualizarFornecedor(Fornecedor f, String nomeFantasia, String razaoSocial, String cnpj) throws Exception {
        logger.info("Método atualizarFornecedor() iniciado para o Fornecedor ID: {}", f != null ? f.getId() : "NULO");

        if (f == null) throw new Exception("Nenhum fornecedor selecionado.");
        if (nomeFantasia == null || nomeFantasia.trim().isEmpty() || cnpj == null || cnpj.trim().isEmpty()) {
            logger.warn("Tentativa de atualização rejeitada: Nome Fantasia ou CNPJ vazios.");
            throw new Exception("Nome Fantasia e CNPJ são obrigatórios!");
        }

        try {
            f.setNomeFantasia(nomeFantasia.trim());
            f.setRazaoSocial(razaoSocial.trim());
            f.setCnpj(cnpj.trim());

            fornecedorDAO.salvar(f);
            logger.info("Fornecedor ID: {} atualizado com sucesso. Novo Nome Fantasia: '{}'", f.getId(), f.getNomeFantasia());
        } catch (Exception e) {
            logger.error("Erro crítico ao atualizar fornecedor ID {}: {}", f.getId(), e.getMessage());
            throw e;
        }
    }

    public void excluirFornecedor(int id) throws Exception {
        logger.info("Método excluirFornecedor() executado para o ID: {}", id);
        try {
            fornecedorDAO.excluir(id);
            logger.info("Fornecedor ID: {} excluído com sucesso do sistema.", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir fornecedor ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}