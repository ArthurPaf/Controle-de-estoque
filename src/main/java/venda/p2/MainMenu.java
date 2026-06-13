package venda.p2;

import venda.p2.dao.GenericDAO;
import venda.p2.model.Categoria;

public class MainMenu {
    public static void main(String[] args) {
        System.out.println("🚀 Conectando ao PostgreSQL e ligando o Hibernate...");
        
        try {
            // Instancia o DAO de Categoria apenas para forçar o Hibernate a ler o persistence.xml
            GenericDAO<Categoria> dao = new GenericDAO<>(Categoria.class);
            
            // Faz uma busca boba (mesmo com o banco vazio) só para disparar a conexão
            dao.listarTodos();
            
            System.out.println("\n=================================================");
            System.out.println("✅ SUCESSO! O banco conectou e as tabelas foram criadas.");
            System.out.println("=================================================");
            
        } catch (Exception e) {
            System.out.println("\n❌ ERRO CRÍTICO NA CONEXÃO:");
            System.out.println("Verifique seu persistence.xml (usuário, senha ou nome do banco).");
            e.printStackTrace();
        }
    }
}