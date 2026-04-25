package venda.p2.controller;

import venda.p2.dao.ClienteDAO;
import venda.p2.model.Cliente;
import java.util.List;

public class ClienteController {

    private ClienteDAO clienteDAO;

    public ClienteController() {
        this.clienteDAO = new ClienteDAO();
    }

    public String salvar(Cliente cliente) {
        // Validação de Regra de Negócio básica
        if (cliente == null) {
            return "Erro: Objeto cliente nulo.";
        }

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            return "Erro: O nome do cliente é obrigatório.";
        }

        // Validação sugerida: CPF com 11 dígitos
        if (cliente.getCpf() == null || cliente.getCpf().replaceAll("\\D", "").length() != 11) {
            return "Erro: CPF inválido. Deve conter 11 dígitos.";
        }

        // Se passar nas validações, chama o DAO (RF002)
        if (clienteDAO.salvar(cliente)) {
            return "Cliente '" + cliente.getNome() + "' cadastrado com sucesso!";
        } else {
            return "Erro ao salvar cliente no banco. Verifique se o CPF já está cadastrado.";
        }
    }

    
    
    public Cliente buscarPorId(int id) {
        return clienteDAO.pesquisar(id);
    }
}