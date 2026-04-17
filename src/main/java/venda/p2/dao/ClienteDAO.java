package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import venda.p2.model.Cliente;

public class ClienteDAO {

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (id, nome, cpf, rg, endereco, telefone) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cliente.getId());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getCpf());
            stmt.setString(4, cliente.getRg());
            stmt.setString(5, cliente.getEndereco());
            stmt.setString(6, cliente.getTelefone());

            stmt.executeUpdate();
            System.out.println("Cliente '" + cliente.getNome() + "' salvo com sucesso!");

        } catch (SQLException e) {
            // Verifica se o erro é de CPF duplicado (Unique Constraint)
            if (e.getSQLState().equals("23505")) {
                System.out.println("Aviso: Cliente ID ou CPF '" + cliente.getCpf() + "' já existe.");
            } else {
                System.err.println("Erro ao salvar cliente: " + e.getMessage());
            }
        }
    }
}
