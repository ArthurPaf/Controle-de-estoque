package venda.p2.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import venda.p2.model.Usuario;

public class UsuarioDAO {

    private GenericDAO<Usuario> genericDAO;

    public UsuarioDAO() {
        this.genericDAO = new GenericDAO<>(Usuario.class);
    }

    public void salvarUsuario(Usuario usuario) throws Exception {
        genericDAO.salvar(usuario);
    }

    // Método essencial para autenticar o usuário no login
    public Usuario buscarPorLoginESenha(String login, String senha) throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        try {
            return em.createQuery(
                "SELECT u FROM Usuario u WHERE u.login = :login AND u.senha = :senha", Usuario.class)
                .setParameter("login", login)
                .setParameter("senha", senha) // Se usar criptografia no futuro, a senha entra aqui criptografada
                .getSingleResult();
        } catch (NoResultException e) {
            // Retorna null se não encontrar nenhum usuário com essas credenciais
            return null;
        } finally {
            em.close();
        }
    }
}