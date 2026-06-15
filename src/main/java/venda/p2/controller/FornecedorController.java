package venda.p2.controller;

import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Fornecedor;

public class FornecedorController {
    // Trocado o DAO específico pelo GenericDAO
    private GenericDAO<Fornecedor> fornecedorDAO = new GenericDAO<>(Fornecedor.class);

    public String salvar(Fornecedor fornecedor) {
        
        if (fornecedor == null) {
            return "Erro: Objeto fornecedor inválido.";
        }
        
        if (fornecedor.getNomeFantasia() == null || fornecedor.getNomeFantasia().trim().isEmpty()) {
            return "Erro: O Nome Fantasia é obrigatório.";
        }

        if (fornecedor.getRazaoSocial() == null || fornecedor.getRazaoSocial().trim().isEmpty()) {
            return "Erro: A Razão Social é obrigatória.";
        }

        // Validação simples de CNPJ (pelo menos 14 números)
        if (fornecedor.getCnpj() == null || fornecedor.getCnpj().replaceAll("\\D", "").length() != 14) {
            return "Erro: CNPJ inválido. Deve conter 14 dígitos.";
        }

        try {
            // O Hibernate cuida do INSERT ou UPDATE de forma transparente
            fornecedorDAO.salvar(fornecedor);
            return "Fornecedor '" + fornecedor.getNomeFantasia() + "' cadastrado com sucesso!";
        } catch (Exception e) {
            return "Erro ao salvar fornecedor no banco (verifique se o CNPJ já existe).";
        }
    }

    // Adicionado método para listar todos na JTable da View, se precisar
    public List<Fornecedor> listarTodos() {
        return fornecedorDAO.listarTodos();
    }

    // Adicionado método para buscar por ID se for necessário em filtros
    public Fornecedor buscarPorId(int id) {
        return fornecedorDAO.buscarPorId(id);
    }

    // Adicionado método para excluir fornecedor de forma genérica
    public String excluir(int id) {
        try {
            fornecedorDAO.excluir(id);
            return "Fornecedor excluído com sucesso!";
        } catch (Exception e) {
            return "Erro ao excluir: verifique se existem produtos ou compras vinculados a este fornecedor.";
        }
    }
}