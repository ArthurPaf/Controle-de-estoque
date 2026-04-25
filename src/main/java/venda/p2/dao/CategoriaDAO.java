package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import venda.p2.model.Categoria;

public class CategoriaDAO {

    public boolean salvar(Categoria categoria) {
        String sql = "INSERT INTO categoria (id, nome) VALUES (?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoria.getId());
            stmt.setString(2, categoria.getNome());

            //isso aqui retorna o numero de linhas afetadas pelo banco, se for maior que 0, significa que a alteração foi feita
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Categoria categoria) {
        String sql = "UPDATE categoria SET nome = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoria.getNome());
            stmt.setInt(2, categoria.getId());

            //isso aqui retorna o numero de linhas afetadas pelo banco, se for maior que 0, significa que a alteração foi feita
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM categoria WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);

            //isso aqui retorna o numero de linhas afetadas pelo banco, se for maior que 0, significa que a alteração foi feita
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Categoria pesquisar(int id) {
        String sql = "SELECT * FROM categoria WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            //o executeQuery retorna os dados do banco, coloca dentro do "rs"
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Categoria c = new Categoria();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
}