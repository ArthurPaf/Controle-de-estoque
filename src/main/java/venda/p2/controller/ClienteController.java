package venda.p2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.ClienteDAO;
import venda.p2.model.Cliente;
import java.util.List;

public class ClienteController {

    private static final Logger logger = LogManager.getLogger(ClienteController.class);

    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public List<Cliente> listarClientes() throws Exception {
        
        logger.info("Método listarClientes() executado.");
        return clienteDAO.listarTodos();
    }

    public List<Cliente> pesquisarPorNome(String nome) throws Exception {
        logger.info("Método pesquisarPorNome() executado no ClienteController. Termo: '{}'", nome);
        
        if (nome == null) {
            nome = "";
        }
        
        return clienteDAO.buscarPorNome(nome.trim());
    }

    public Cliente buscarPorId(int id) throws Exception {
        
        logger.info("Método buscarPorId() executado para o ID: {}", id);
        return clienteDAO.buscarPorId(id);
    }

    public void salvarCliente(Cliente cliente) throws Exception {
        
        logger.info("Método salvarCliente() executado.");

        
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
            cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            
            
            logger.warn("Tentativa de salvar cliente com campos obrigatórios vazios (Nome/CPF).");
            throw new Exception("Nome e CPF são campos obrigatórios!");
        }
        
        try {
            clienteDAO.salvar(cliente);
            
            logger.info("Cliente '{}' (CPF: {}) salvo com sucesso.", cliente.getNome(), cliente.getCpf());
        } catch (Exception e) {
            
            logger.error("Erro ao salvar cliente: {}", e.getMessage());
            throw e;
        }
    }

    public void excluirCliente(int id) throws Exception {
        
        logger.info("Método excluirCliente() executado para o ID: {}", id);
        
        try {
            clienteDAO.excluir(id);
            
            logger.info("Cliente ID: {} excluído com sucesso.", id);
        } catch (Exception e) {
            
            logger.error("Erro ao excluir cliente ID: {}: {}", id, e.getMessage());
            throw e;
        }
    }
}