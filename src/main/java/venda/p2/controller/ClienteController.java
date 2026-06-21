package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.ClienteDAO;
import venda.p2.model.Cliente;
import java.util.List;

public class ClienteController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO PARA CLIENTE
    private static final Logger logger = LogManager.getLogger(ClienteController.class);

    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public List<Cliente> listarClientes() throws Exception {
        // LOG ADICIONADO
        logger.info("Método listarClientes() executado.");
        return clienteDAO.listarTodos();
    }

    public Cliente buscarPorId(int id) throws Exception {
        // LOG ADICIONADO
        logger.info("Método buscarPorId() executado para o ID: {}", id);
        return clienteDAO.buscarPorId(id);
    }

    public void salvarCliente(Cliente cliente) throws Exception {
        // LOG ADICIONADO
        logger.info("Método salvarCliente() executado.");

        // A regra de validação que estava na View agora fica aqui!
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
            cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            
            // LOG ADICIONADO (warn para falhas de preenchimento obrigatório)
            logger.warn("Tentativa de salvar cliente com campos obrigatórios vazios (Nome/CPF).");
            throw new Exception("Nome e CPF são campos obrigatórios!");
        }
        
        try {
            clienteDAO.salvar(cliente);
            // LOG ADICIONADO
            logger.info("Cliente '{}' (CPF: {}) salvo com sucesso.", cliente.getNome(), cliente.getCpf());
        } catch (Exception e) {
            // LOG ADICIONADO
            logger.error("Erro ao salvar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public void excluirCliente(int id) throws Exception {
        // LOG ADICIONADO
        logger.info("Método excluirCliente() executado para o ID: {}", id);
        
        try {
            clienteDAO.excluir(id);
            // LOG ADICIONADO
            logger.info("Cliente ID: {} excluído com sucesso.", id);
        } catch (Exception e) {
            // LOG ADICIONADO
            logger.error("Erro ao excluir cliente ID: {}: {}", id, e.getMessage());
            throw e;
        }
    }
}