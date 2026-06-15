package venda.p2.controller;

import venda.p2.dao.GenericDAO;
import venda.p2.model.Cliente;
import java.util.List;

public class ClienteController {

    private GenericDAO<Cliente> clienteDAO;

    public ClienteController() {
        // Inicializa o DAO Genérico apontando para a classe Cliente
        this.clienteDAO = new GenericDAO<>(Cliente.class);
    }

    public String salvar(Cliente cliente) {
        
        if (cliente == null) {
            return "Erro: cliente nulo.";
        }

        if (cliente.getNome() == null || cliente.getNome().trim().isEmpty()) {
            return "Erro: O nome do cliente é obrigatório.";
        }

        // tem como colocar pontos e traços no cpf :)
        // Validação: CPF com 11 dígitos, o replaceAll("\\D", "") remove qualquer caractere que não seja um numero
        if (cliente.getCpf() == null || cliente.getCpf().replaceAll("\\D", "").length() != 11) {
            return "Erro: CPF inválido. Deve conter 11 dígitos.";
        }

        try {
            // O Hibernate salva ou atualiza automaticamente
            clienteDAO.salvar(cliente);
            return "Cliente '" + cliente.getNome() + "' cadastrado com sucesso!";
        } catch (Exception e) {
            // Se houver uma constraint de UNIQUE no CPF (PostgreSQL), o catch vai capturar aqui
            return "Erro ao salvar cliente no banco. Verifique se o CPF já está cadastrado.";
        }
    }

    public Cliente buscarPorId(int id) {
        return clienteDAO.buscarPorId(id);
    }

    // Adicionado o método para listar, caso sua tabela de Clientes precise na View
    public List<Cliente> listarTodos() {
        return clienteDAO.listarTodos();
    }

    // Adicionado o método para excluir de forma genérica
    public String excluir(int id) {
        try {
            clienteDAO.excluir(id);
            return "Cliente excluído com sucesso!";
        } catch (Exception e) {
            return "Erro ao excluir: verifique se existem vendas ou registros vinculados a este cliente.";
        }
    }
}