package venda.p2.controller;

import venda.p2.dao.ClienteDAO;
import venda.p2.model.Cliente;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        // Inicializa o DAO para que o controller possa usá-lo
        this.clienteDAO = new ClienteDAO();
    }

    public void salvar(Cliente cliente) {
        // Aqui você poderia validar, por exemplo, se o CPF tem 11 dígitos
        if (cliente != null && cliente.getCpf() != null) {
            clienteDAO.salvar(cliente);
        } else {
            System.err.println("Erro: Cliente ou CPF inválido.");
        }
    }
}