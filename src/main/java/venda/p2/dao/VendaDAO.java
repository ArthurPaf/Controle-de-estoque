package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import venda.p2.model.Venda;

public class VendaDAO {
    Connection conn = null;

    public boolean salvar(Venda venda) {
        try {
            conn = Conexao.getConnection();
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            int qtdeLinhas = stmt.executeUpdate("INSERT INTO venda (id, cliente_id, data_venda, valor_total) VALUES ("
                    + venda.getId() + ", " + venda.getCliente().getId() + ", '" + venda.getDataVenda() + "', "
                    + venda.getValorTotal() + ")");
            stmt.close();
            if (qtdeLinhas > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao();
        }
    }

    public boolean verificaQtdeVendas(int clienteId) {
    // Use PreparedStatement para evitar erros de sintaxe e SQL Injection
    String sql = "SELECT COUNT(*) FROM venda WHERE cliente_id = ?";
    
    try (Connection conn = Conexao.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, clienteId);
        
        // MUDANÇA ESSENCIAL: executeQuery() em vez de executeUpdate()
        ResultSet rs = stmt.executeQuery(); 
        
        if (rs.next()) {
            int totalVendas = rs.getInt(1);
            return totalVendas < 3; // Retorna true se puder vender
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

}
