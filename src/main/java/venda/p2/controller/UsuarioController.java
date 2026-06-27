package venda.p2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import venda.p2.dao.UsuarioDAO;
import venda.p2.model.Usuario;

public class UsuarioController { // Ou UsuarioController, dependendo de como dividiu os controllers

    private static final Logger logger = LogManager.getLogger(UsuarioController.class);
    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Guarda globalmente quem está usando o sistema no momento
    private static Usuario usuarioLogado;

    /**
     * Efetua o login usando o método existente no seu DAO.
     */
    public Usuario autenticar(String login, String senha) throws Exception {
        logger.info("Tentativa de login para o usuário: {}", login);

        if (login == null || login.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            throw new Exception("Por favor, preencha o login e a senha.");
        }

        // Chama diretamente o método que você já criou
        Usuario usuario = usuarioDAO.buscarPorLoginESenha(login.trim(), senha);

        if (usuario == null) {
            logger.warn("Falha na autenticação para o login: {}", login);
            throw new Exception("Usuário ou senha inválidos!");
        }

        logger.info("Usuário '{}' logado com sucesso como [{}]", usuario.getLogin(), usuario.getPerfil());
        usuarioLogado = usuario; // Salva o usuário na sessão
        return usuario;
    }

    /**
     * Cadastra um novo usuário se o usuário da sessão atual for um ADMIN.
     */
    public void cadastrarNovoUsuario(Usuario novoUsuario) throws Exception {
        // 1. Validação de Segurança: Checa quem está logado no sistema
        if (usuarioLogado == null) {
            throw new Exception("Nenhum usuário está logado no sistema.");
        }
        
        if (!usuarioLogado.getPerfil().equalsIgnoreCase("ADMIN")) {
            logger.warn("O usuário '{}' (OPERADOR) tentou cadastrar um novo usuário sem permissão.", usuarioLogado.getLogin());
            throw new Exception("Acesso Negado! Apenas administradores podem cadastrar novos usuários.");
        }

        // 2. Validações básicas do novo usuário
        if (novoUsuario.getLogin() == null || novoUsuario.getLogin().trim().isEmpty() ||
            novoUsuario.getSenha() == null || novoUsuario.getSenha().trim().isEmpty() ||
            novoUsuario.getPerfil() == null) {
            throw new Exception("Todos os campos obrigatórios (Login, Senha e Perfil) devem ser preenchidos.");
        }

        // 3. Tudo correto? Invoca o método do seu DAO
        usuarioDAO.salvarUsuario(novoUsuario);
        logger.info("Novo usuário '{}' [{}] cadastrado por '{}'.", novoUsuario.getLogin(), novoUsuario.getPerfil(), usuarioLogado.getLogin());
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void efetuarLogout() {
        usuarioLogado = null;
    }
    
}