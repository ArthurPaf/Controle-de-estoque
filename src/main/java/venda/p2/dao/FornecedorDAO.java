package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import venda.p2.model.Fornecedor;

public class FornecedorDAO {

    public void salvar(Fornecedor fornecedor) {
        // SQL baseado nas colunas: id, nome_fantasia, razao_social, cnpj
        String sql = "INSERT INTO Fornecedor (id, nome_fantasia, razao_social, cnpj) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, fornecedor.getId());
            stmt.setString(2, fornecedor.getNomeFantasia());
            stmt.setString(3, fornecedor.getRazaoSocial());
            stmt.setString(4, fornecedor.getCnpj());

            stmt.executeUpdate();
            System.out.println("Fornecedor '" + fornecedor.getNomeFantasia() + "' salvo com sucesso!");

        } catch (SQLException e) {
            // Verifica se o CNPJ ou ID já existem
            if (e.getSQLState().equals("23505")) {
                System.out.println("Aviso: Fornecedor ID ou CNPJ '" + fornecedor.getCnpj() + "' já existe.");
            } else {
                System.err.println("Erro ao salvar fornecedor: " + e.getMessage());
            }
        }
    }
}