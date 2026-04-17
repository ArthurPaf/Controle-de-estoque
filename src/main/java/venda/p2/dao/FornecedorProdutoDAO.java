package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import venda.p2.model.FornecedorProduto;

public class FornecedorProdutoDAO {

    public void salvar(FornecedorProduto fp) {
        // SQL para a tabela intermediária
        String sql = "INSERT INTO fornecedor_produto (id, fornecedor_id, produto_id) VALUES (?, ?, ?)";

        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, fp.getId());
            // Pegamos o ID de dentro do objeto Fornecedor
            stmt.setInt(2, fp.getFornecedor().getId());
            // Pegamos o ID de dentro do objeto Produto
            stmt.setInt(3, fp.getProduto().getId());

            stmt.executeUpdate();
            System.out.println("Vínculo Fornecedor-Produto salvo com sucesso!");

        } catch (SQLException e) {
            if (e.getSQLState().equals("23505")) {
                System.out.println("Aviso: Vínculo Fornecedor-Produto já existe.");
            } else {
                System.err.println("Erro ao salvar vínculo Fornecedor-Produto: " + e.getMessage());
            }
        }
    }
}