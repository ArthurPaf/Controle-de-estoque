package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import venda.p2.model.Categoria;

public class CategoriaDAO {

    public void salvar(Categoria categoria) {
        String sql = "INSERT INTO Categoria (id, nome) VALUES (?, ?)";
        
        // Usando try-with-resources para fechar a conexão automaticamente
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoria.getId());
            stmt.setString(2, categoria.getNome());

            stmt.executeUpdate();
            System.out.println("Categoria '" + categoria.getNome() + "' salva com sucesso!");

        } catch (SQLException e) {
            // Caso o ID já exista, o Postgres lançará uma exceção
            if (e.getSQLState().equals("23505")) { 
                System.out.println("Aviso: Categoria ID " + categoria.getId() + " já existe no banco.");
            } else {
                System.err.println("Erro ao salvar categoria: " + e.getMessage());
            }
        }
    }
}
