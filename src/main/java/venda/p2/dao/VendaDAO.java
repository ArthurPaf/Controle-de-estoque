package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import venda.p2.model.Cliente;
import venda.p2.model.Produto;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class VendaDAO {
    Connection conn = null;

    public int salvar(Venda v) { // Alterado de boolean para int
    String sqlVenda = "INSERT INTO venda (cliente_id, data_venda, valor_total) VALUES (?, ?, ?)";
    String sqlItem = "INSERT INTO venda_produto (venda_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
    String sqlUpdateProduto = "UPDATE produto SET valor_ultima_venda = ? WHERE id = ?";

    try {
        conn = Conexao.getConnection();
        conn.setAutoCommit(false); 

        PreparedStatement pst = conn.prepareStatement(sqlVenda, Statement.RETURN_GENERATED_KEYS);
        pst.setInt(1, v.getCliente().getId());
        pst.setDate(2, java.sql.Date.valueOf(v.getDataVenda()));
        pst.setDouble(3, v.getValorTotal());
        pst.executeUpdate();

        
        ResultSet rs = pst.getGeneratedKeys();
        int idVendaGerado = 0;
        if (rs.next()) {
            idVendaGerado = rs.getInt(1);
        }

        
        for (VendaProduto vp : v.getVendaProdutos()) {
            // Insere o item na tabela venda_produto
            PreparedStatement pstItem = conn.prepareStatement(sqlItem);
            pstItem.setInt(1, idVendaGerado);
            pstItem.setInt(2, vp.getProduto().getId());
            pstItem.setDouble(3, vp.getQuantidade());
            pstItem.setDouble(4, vp.getValorUnitario());
            pstItem.executeUpdate();

            
            PreparedStatement pstProd = conn.prepareStatement(sqlUpdateProduto);
            pstProd.setDouble(1, vp.getValorUnitario());
            pstProd.setInt(2, vp.getProduto().getId());
            pstProd.executeUpdate();
        }

        conn.commit();
        return idVendaGerado; 

    } catch (Exception e) {
        try { conn.rollback(); } catch (Exception ex) {}
        e.printStackTrace();
        return -1; 
    } finally {
        Conexao.fecharConexao();
    }
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
        conn.setAutoCommit(false); 

       
        String sqlItens = "DELETE FROM venda_produto WHERE venda_id = ?";
        PreparedStatement pst = conn.prepareStatement(sqlItens);
        pst.setInt(1, id);
        pst.executeUpdate();

        
        String sqlVenda = "DELETE FROM venda WHERE id = ?";
        pst = conn.prepareStatement(sqlVenda);
        pst.setInt(1, id);
        int linhasAfetadas = pst.executeUpdate();

        conn.commit(); 
        return linhasAfetadas > 0;

    } catch (Exception e) {
        try { conn.rollback(); } catch (Exception ex) { }
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
        
        String sql = "SELECT v.id AS venda_id, v.data_venda, v.valor_total, v.cliente_id, " +
                     "c.nome AS nome_cliente, " +
                     "vp.quantidade, vp.valor_unitario, vp.produto_id, " +
                     "p.nome AS nome_produto " +
                     "FROM venda v " +
                     "INNER JOIN cliente c ON c.id = v.cliente_id " +
                     "INNER JOIN venda_produto vp ON v.id = vp.venda_id " +
                     "INNER JOIN produto p ON p.id = vp.produto_id " +
                     "WHERE v.id = " + id; 

        ResultSet rs = stmt.executeQuery(sql);
        Venda v = null;

        while (rs.next()) {
            if (v == null) {
                v = new Venda();
                v.setId(rs.getInt("venda_id")); 
                v.setDataVenda(rs.getDate("data_venda").toLocalDate());
                v.setValorTotal(rs.getDouble("valor_total"));

                Cliente cli = new Cliente();
                cli.setId(rs.getInt("cliente_id"));
                cli.setNome(rs.getString("nome_cliente"));
                v.setCliente(cli);
            }

            VendaProduto item = new VendaProduto();
            item.setQuantidade(rs.getDouble("quantidade"));
            item.setValorUnitario(rs.getDouble("valor_unitario"));

            Produto prod = new Produto();
            prod.setId(rs.getInt("produto_id"));
            prod.setNome(rs.getString("nome_produto"));
            item.setProduto(prod);

            v.getVendaProdutos().add(item);
        }
        return v;
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        Conexao.fecharConexao();
    }
    return null;
}
    

    public int contarVendasMesAtual(int clienteId) {
    String sql = "SELECT COUNT(*) FROM venda WHERE cliente_id = ? " +
                 "AND EXTRACT(MONTH FROM data_venda) = EXTRACT(MONTH FROM CURRENT_DATE) " +
                 "AND EXTRACT(YEAR FROM data_venda) = EXTRACT(YEAR FROM CURRENT_DATE)";
                 
    try (Connection conn = Conexao.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, clienteId);
        ResultSet rs = stmt.executeQuery(); 
        
        if (rs.next()) {
            return rs.getInt(1); 
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return 0; 
}
}