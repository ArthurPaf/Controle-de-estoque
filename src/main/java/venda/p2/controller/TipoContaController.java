package venda.p2.controller;

import venda.p2.dao.TipoContaDAO;
import venda.p2.model.TipoConta;
import java.util.List;

public class TipoContaController {

    private TipoContaDAO tipoContaDAO;

    public TipoContaController() {
        this.tipoContaDAO = new TipoContaDAO();
    }

    public List<TipoConta> listarTodos() throws Exception {
        return tipoContaDAO.listarTodos();
    }

    public TipoConta buscarPorId(int id) throws Exception {
        return tipoContaDAO.buscarPorId(id);
    }

    public void salvarTipoConta(String descricao) throws Exception {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new Exception("A descrição é obrigatória!");
        }

        TipoConta tc = new TipoConta();
        tc.setDescricao(descricao.trim());
        
        tipoContaDAO.salvar(tc);
    }

    public void atualizarTipoConta(TipoConta tc, String descricao) throws Exception {
        if (tc == null) throw new Exception("Nenhum tipo de conta selecionado.");
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new Exception("A descrição é obrigatória!");
        }

        tc.setDescricao(descricao.trim());
        
        tipoContaDAO.salvar(tc);
    }

    public void excluirTipoConta(int id) throws Exception {
        tipoContaDAO.excluir(id);
    }
}