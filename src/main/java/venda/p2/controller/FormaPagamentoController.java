package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.FormaPagamentoDAO;
import venda.p2.model.FormaPagamento;
import java.util.List;

public class FormaPagamentoController {

    // 2. DECLARAÇÃO DO LOGGER ESPECÍFICO
    private static final Logger logger = LogManager.getLogger(FormaPagamentoController.class);

    // Alterado para o DAO específico da classe
    private FormaPagamentoDAO formaPagamentoDAO;

    public FormaPagamentoController() {
        this.formaPagamentoDAO = new FormaPagamentoDAO();
    }

    public List<FormaPagamento> listarTodas() throws Exception {
        logger.info("Método listarTodas() executado.");
        return formaPagamentoDAO.listarTodos();
    }

    public FormaPagamento buscarPorId(int id) throws Exception {
        logger.info("Método buscarPorId() executado para o ID: {}", id);
        return formaPagamentoDAO.buscarPorId(id);
    }

    public void salvarForma(String nome, String parcelasStr, String prazoStr, int tipoSelectedIndex) throws Exception {
        logger.info("Método salvarForma() iniciado.");

        if (nome == null || nome.trim().isEmpty()) {
            logger.warn("Tentativa de cadastro rejeitada: nome da forma de pagamento está em branco.");
            throw new Exception("O nome é obrigatório!");
        }

        try {
            FormaPagamento f = new FormaPagamento();
            f.setNome(nome.trim());
            f.setQtde_parcela(Integer.parseInt(parcelasStr.trim()));
            f.setPrazo(Integer.parseInt(prazoStr.trim()));
            f.setAvista_aprazo(tipoSelectedIndex == 0 ? 1 : 2);

            formaPagamentoDAO.salvar(f);
            logger.info("Nova Forma de Pagamento '{}' cadastrada com sucesso.", f.getNome());
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter campos numéricos (parcelas: '{}', prazo: '{}').", parcelasStr, prazoStr);
            throw new Exception("Os campos de parcelas e prazo devem conter valores numéricos válidos.");
        } catch (Exception e) {
            logger.error("Erro crítico ao salvar forma de pagamento: {}", e.getMessage());
            throw e;
        }
    }

    public void atualizarForma(FormaPagamento f, String nome, String parcelasStr, String prazoStr, int tipoSelectedIndex) throws Exception {
        logger.info("Método atualizarForma() iniciado para a Forma ID: {}", f != null ? f.getId() : "NULA");

        if (f == null) throw new Exception("Nenhuma forma selecionada.");
        if (nome == null || nome.trim().isEmpty()) {
            logger.warn("Tentativa de alteração rejeitada: nome da forma de pagamento está em branco.");
            throw new Exception("O nome é obrigatório!");
        }

        try {
            f.setNome(nome.trim());
            f.setQtde_parcela(Integer.parseInt(parcelasStr.trim()));
            f.setPrazo(Integer.parseInt(prazoStr.trim()));
            f.setAvista_aprazo(tipoSelectedIndex == 0 ? 1 : 2);

            formaPagamentoDAO.salvar(f);
            logger.info("Forma de Pagamento ID: {} alterada com sucesso. Novo Nome: '{}'", f.getId(), f.getNome());
        } catch (NumberFormatException e) {
            logger.error("Erro ao converter campos numéricos na atualização.");
            throw new Exception("Os campos de parcelas e prazo devem conter valores numéricos válidos.");
        } catch (Exception e) {
            logger.error("Erro crítico ao atualizar forma de pagamento ID {}: {}", f.getId(), e.getMessage());
            throw e;
        }
    }

    public void excluirForma(int id) throws Exception {
        logger.info("Método excluirForma() executado para o ID: {}", id);
        try {
            formaPagamentoDAO.excluir(id);
            logger.info("Forma de Pagamento ID: {} excluída do sistema.", id);
        } catch (Exception e) {
            logger.error("Erro ao excluir forma de pagamento ID {}: {}", id, e.getMessage());
            throw e;
        }
    }
}