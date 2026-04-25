package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import venda.p2.model.Cliente;

public class ClienteDAO {

    public boolean salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (id, nome, cpf, rg, endereco, telefone) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getRg());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getTelefone());

            //isso aqui retorna o numero de linhas afetadas pelo banco, se for maior que 0, significa que a alteração foi feita
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Cliente cliente) {
        String sql = "UPDATE cliente SET nome = ?, cpf = ?, rg = ?, endereco = ?, telefone = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getCpf());
            stmt.setString(3, cliente.getRg());
            stmt.setString(4, cliente.getEndereco());
            stmt.setString(5, cliente.getTelefone());
            stmt.setInt(6, cliente.getId());

            //isso aqui retorna o numero de linhas afetadas pelo banco, se for maior que 0, significa que a alteração foi feita
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM cliente WHERE id = ?";
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

    public Cliente pesquisar(int id) {
        String sql = "SELECT * FROM cliente WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Cliente c = new Cliente();
                c.setId(rs.getInt("id"));
                c.setNome(rs.getString("nome"));
                c.setCpf(rs.getString("cpf"));
                c.setRg(rs.getString("rg"));
                c.setEndereco(rs.getString("endereco"));
                c.setTelefone(rs.getString("telefone"));
                return c;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}