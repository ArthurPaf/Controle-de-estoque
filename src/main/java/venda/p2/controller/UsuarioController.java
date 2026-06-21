package venda.p2.controller;

import venda.p2.dao.UsuarioDAO;
import venda.p2.model.Usuario;

public class UsuarioController {

    private UsuarioDAO usuarioDAO;
    // Variável estática para guardar quem está logado no SisCom enquanto o sistema estiver aberto
    private static Usuario usuarioLogado;

    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Realiza a autenticação do usuário.
     * @return Usuario se as credenciais forem válidas, ou lança uma Exception com o erro.
     */
    public Usuario autenticar(String login, String senha) throws Exception {
        if (login == null || login.trim().isEmpty()) {
            throw new Exception("O campo de usuário não pode ficar vazio.");
        }
        if (senha == null || new String(senha).trim().isEmpty()) {
            throw new Exception("O campo de senha não pode ficar vazio.");
        }

        // Busca o usuário no banco de dados
        Usuario usuario = usuarioDAO.buscarPorLoginESenha(login.trim(), senha.trim());

        if (usuario == null) {
            throw new Exception("Usuário ou senha incorretos.");
        }

        // Guarda a sessão do usuário logado globalmente na aplicação
        usuarioLogado = usuario;
        return usuario;
    }

    // Métodos utilitários para controlar a sessão nas outras telas do sistema
    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void efetuarLogout() {
        usuarioLogado = null;
    }
}