package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class VendaDAO {
    Connection conn = null;

    public boolean salvar(Venda venda) {
        try {
            conn = Conexao.getConnection();
            conn.setAutoCommit(false); 
            Statement stmt = conn.createStatement();
            
            // 1. Insere o cabeçalho da Venda
            String sqlVenda = "INSERT INTO venda (id, cliente_id, data_venda, valor_total) VALUES ("
                    + venda.getId() + ", " + venda.getCliente().getId() + ", '" + venda.getDataVenda() + "', "
                    + venda.getValorTotal() + ")";
            
            int qtdeLinhas = stmt.executeUpdate(sqlVenda);
            
            if (qtdeLinhas > 0) {
                // 2. Insere os itens da venda_produto
                // REPARE: Removemos a chamada ao ProdutoDAO e ao atualizarEstoque daqui!
                for (VendaProduto item : venda.getVendaProdutos()) {
                    String sqlItem = "INSERT INTO venda_produto (venda_id, produto_id, quantidade, valor_unitario) VALUES ("
                            + venda.getId() + ", " + item.getProduto().getId() + ", " 
                            + item.getQuantidade() + ", " + item.getValorUnitario() + ")";
                    stmt.executeUpdate(sqlItem);
                }
                
                conn.commit();
                return true;
            }
        } catch (Exception e) {
            try { if(conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            Conexao.fecharConexao();
        }
        return false;
    }

    public boolean alterar(Venda venda) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "UPDATE venda SET cliente_id = " + venda.getCliente().getId() 
                       + ", data_venda = '" + venda.getDataVenda() 
                       + "', valor_total = " + venda.getValorTotal() 
                       + " WHERE id = " + venda.getId();
            return stmt.executeUpdate(sql) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao();
        }
    }

    public boolean excluir(int id) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate("DELETE FROM venda WHERE id = " + id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao();
        }
    }

    public Venda pesquisar(int id) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM venda WHERE id = " + id);
            if (rs.next()) {
                Venda v = new Venda();
                v.setId(rs.getInt("id"));
                v.setDataVenda(rs.getDate("data_venda").toLocalDate());
                v.setValorTotal(rs.getDouble("valor_total"));
                return v;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Conexao.fecharConexao();
        }
        return null;
    }

    // Mantemos este método aqui porque ele é uma CONSULTA ao banco, 
    // mas quem decide "bloquear" ou não a venda baseada nisso é o Controller.
    public boolean verificaQtdeVendas(int clienteId) {
        String sql = "SELECT COUNT(*) FROM venda WHERE cliente_id = ?";
        try (Connection conn = Conexao.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery(); 
            if (rs.next()) {
                // Aqui podemos até deixar mais genérico retornando o número total
                // e o Controller faz a conta de " < 3 ".
                return rs.getInt(1) < 3; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int contarVendasMesAtual(int clienteId) {
    // SQL ajustado para o RNF004 (conta apenas o mês e ano vigentes)
    String sql = "SELECT COUNT(*) FROM venda WHERE cliente_id = ? " +
                 "AND EXTRACT(MONTH FROM data_venda) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                 "AND EXTRACT(YEAR FROM data_venda) = EXTRACT(YEAR FROM CURRENT_DATE)";
                 
    try (Connection conn = Conexao.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, clienteId);
        ResultSet rs = stmt.executeQuery(); 
        
        if (rs.next()) {
            return rs.getInt(1); // Retorna a quantidade (ex: 0, 1, 2, 3...)
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0; // Se der erro, retorna 0 por segurança
}
}