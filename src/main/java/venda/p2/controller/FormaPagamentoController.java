package venda.p2.controller;

import venda.p2.dao.FormaPagamentoDAO;
import venda.p2.model.FormaPagamento;
import java.util.List;

public class FormaPagamentoController {

    // Alterado para o DAO específico da classe
    private FormaPagamentoDAO formaPagamentoDAO;

    public FormaPagamentoController() {
        this.formaPagamentoDAO = new FormaPagamentoDAO();
    }

    public List<FormaPagamento> listarTodas() throws Exception {
        return formaPagamentoDAO.listarTodos();
    }

    public FormaPagamento buscarPorId(int id) throws Exception {
        return formaPagamentoDAO.buscarPorId(id);
    }

    public void salvarForma(String nome, String parcelasStr, String prazoStr, int tipoSelectedIndex) throws Exception {
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome é obrigatório!");
        }

        FormaPagamento f = new FormaPagamento();
        f.setNome(nome.trim());
        f.setQtde_parcela(Integer.parseInt(parcelasStr.trim()));
        f.setPrazo(Integer.parseInt(prazoStr.trim()));
        f.setAvista_aprazo(tipoSelectedIndex == 0 ? 1 : 2);

        formaPagamentoDAO.salvar(f);
    }

    public void atualizarForma(FormaPagamento f, String nome, String parcelasStr, String prazoStr, int tipoSelectedIndex) throws Exception {
        if (f == null) throw new Exception("Nenhuma forma selecionada.");
        if (nome == null || nome.trim().isEmpty()) {
            throw new Exception("O nome é obrigatório!");
        }

        f.setNome(nome.trim());
        f.setQtde_parcela(Integer.parseInt(parcelasStr.trim()));
        f.setPrazo(Integer.parseInt(prazoStr.trim()));
        f.setAvista_aprazo(tipoSelectedIndex == 0 ? 1 : 2);

        formaPagamentoDAO.salvar(f);
    }

    public void excluirForma(int id) throws Exception {
        formaPagamentoDAO.excluir(id);
    }
}