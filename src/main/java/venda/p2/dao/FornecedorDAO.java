package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import venda.p2.model.Fornecedor;

public class FornecedorDAO {

    public boolean salvar(Fornecedor fornecedor) {
        String sql = "INSERT INTO fornecedor (id, nome_fantasia, razao_social, cnpj) VALUES (?, ?, ?, ?)";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fornecedor.getId());
            stmt.setString(2, fornecedor.getNomeFantasia());
            stmt.setString(3, fornecedor.getRazaoSocial());
            stmt.setString(4, fornecedor.getCnpj());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean alterar(Fornecedor fornecedor) {
        String sql = "UPDATE fornecedor SET nome_fantasia = ?, razao_social = ?, cnpj = ? WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, fornecedor.getNomeFantasia());
            stmt.setString(2, fornecedor.getRazaoSocial());
            stmt.setString(3, fornecedor.getCnpj());
            stmt.setInt(4, fornecedor.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean excluir(int id) {
        String sql = "DELETE FROM fornecedor WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Fornecedor pesquisar(int id) {
        String sql = "SELECT * FROM fornecedor WHERE id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);

            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Fornecedor f = new Fornecedor();
                f.setId(rs.getInt("id"));
                f.setNomeFantasia(rs.getString("nome_fantasia"));
                f.setRazaoSocial(rs.getString("razao_social"));
                f.setCnpj(rs.getString("cnpj"));
                return f;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}