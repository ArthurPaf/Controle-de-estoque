package venda.p2.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;

public class CompraDAO {
    Connection conn = null;

    public boolean salvar(Compra compra) {
    try {
        conn = Conexao.getConnection();
        conn.setAutoCommit(false); 
        Statement stmt = conn.createStatement();
        
        // 1. Insere o cabeçalho
        // DICA: Se a tabela 'compra' tiver ID serial, você não manda o ID. 
        // Se for manual, adicione 'id' no INSERT abaixo.
        String sqlCompra = "INSERT INTO compra (id, fornecedor_id, data_compra, valor_total) VALUES ("
                + compra.getId() + ", "
                + compra.getFornecedor().getId() + ", '" 
                + compra.getDataCompra() + "', "
                + compra.getValorTotal() + ")";
        
        int qtdeLinhas = stmt.executeUpdate(sqlCompra);
        
        if (qtdeLinhas > 0) {
            // 2. Insere os itens da compra_produto
            for (CompraProduto item : compra.getCompraProdutos()) {
                // REMOVEMOS o 'id' daqui para evitar o erro de coluna inexistente
                String sqlItem = "INSERT INTO compra_produto (compra_id, produto_id, quantidade, valor_unitario) VALUES ("
                        + compra.getId() + ", " 
                        + item.getProduto().getId() + ", " 
                        + item.getQuantidade() + ", " 
                        + item.getValorUnitario() + ")";
                
                stmt.executeUpdate(sqlItem);
            }
            
            conn.commit();
            return true;
        }
    } catch (Exception e) {
        try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace();
    } finally {
        Conexao.fecharConexao();
    }
    return false;
}

    public Compra pesquisar(int id) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM compra WHERE id = " + id);
            if (rs.next()) {
                Compra c = new Compra();
                c.setId(rs.getInt("id"));
                c.setDataCompra(rs.getDate("data_compra").toLocalDate());
                c.setValorTotal(rs.getDouble("valor_total"));
                return c;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Conexao.fecharConexao();
        }
        return null;
    }

    public List<Compra> pesquisarTodos() {
        List<Compra> compras = new ArrayList<>();
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM compra ORDER BY data_compra DESC");
            while (rs.next()) {
                Compra c = new Compra();
                c.setId(rs.getInt("id"));
                c.setDataCompra(rs.getDate("data_compra").toLocalDate());
                c.setValorTotal(rs.getDouble("valor_total"));
                compras.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Conexao.fecharConexao();
        }
        return compras;
    }

    public boolean excluir(int id) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("DELETE FROM compra_produto WHERE compra_id = " + id);
            return stmt.executeUpdate("DELETE FROM compra WHERE id = " + id) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao();
        }
    }
}