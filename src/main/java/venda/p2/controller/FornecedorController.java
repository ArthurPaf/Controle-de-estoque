package venda.p2.controller;

import java.util.List;
import venda.p2.dao.FornecedorDAO;
import venda.p2.model.Fornecedor;

public class FornecedorController {
    private FornecedorDAO fornecedorDAO = new FornecedorDAO();

    public String salvar(Fornecedor fornecedor) {
        // Validações de Regra de Negócio
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

        // RF003: Permitir cadastrar fornecedores
        if (fornecedorDAO.salvar(fornecedor)) {
            return "Fornecedor '" + fornecedor.getNomeFantasia() + "' cadastrado com sucesso!";
        } else {
            return "Erro ao salvar fornecedor no banco (verifique se o CNPJ já existe).";
        }
    }

   
}