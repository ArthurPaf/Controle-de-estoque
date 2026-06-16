package venda.p2.controller;

import venda.p2.dao.ClienteDAO;
import venda.p2.model.Cliente;
import java.util.List;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public List<Cliente> listarClientes() throws Exception {
        return clienteDAO.listarTodos();
    }

    public Cliente buscarPorId(int id) throws Exception {
        return clienteDAO.buscarPorId(id);
    }

    public void salvarCliente(Cliente cliente) throws Exception {
        // A regra de validação que estava na View agora fica aqui!
        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty() ||
            cliente.getCpf() == null || cliente.getCpf().trim().isEmpty()) {
            throw new Exception("Nome e CPF são campos obrigatórios!");
        }
        clienteDAO.salvar(cliente);
    }

    public void excluirCliente(int id) throws Exception {
        clienteDAO.excluir(id);
    }
}