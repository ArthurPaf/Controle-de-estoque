package venda.p2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Fornecedor;
import venda.p2.model.Produto;

public class CompraDAO {
    Connection conn = null;

    //ele salva id automatico
    public int salvar(Compra compra) {
    try {
        conn = Conexao.getConnection();
        conn.setAutoCommit(false); 
        
        String sqlCompra = "INSERT INTO compra (fornecedor_id, data_compra, valor_total) VALUES (?, ?, ?)";
        PreparedStatement pstCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
        
        pstCompra.setInt(1, compra.getFornecedor().getId());
        pstCompra.setDate(2, java.sql.Date.valueOf(compra.getDataCompra()));
        pstCompra.setDouble(3, compra.getValorTotal());
        pstCompra.executeUpdate();
        
        ResultSet rs = pstCompra.getGeneratedKeys();
        int idGerado = 0;
        if (rs.next()) {
            idGerado = rs.getInt(1);
        }

    
        String sqlItem = "INSERT INTO compra_produto (compra_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        
        String sqlUpdateProduto = "UPDATE produto SET qtde_estoque = qtde_estoque + ?, valor_ultima_compra = ?, preco_medio = ? WHERE id = ?";
        
        for (CompraProduto item : compra.getCompraProdutos()) {
            PreparedStatement pstItem = conn.prepareStatement(sqlItem);
            pstItem.setInt(1, idGerado);
            pstItem.setInt(2, item.getProduto().getId());
            pstItem.setDouble(3, item.getQuantidade());
            pstItem.setDouble(4, item.getValorUnitario());
            pstItem.executeUpdate();
            
            PreparedStatement pstProd = conn.prepareStatement(sqlUpdateProduto);
            pstProd.setDouble(1, item.getQuantidade()); 
            pstProd.setDouble(2, item.getValorUnitario());
            pstProd.setDouble(3, item.getProduto().getPreco()); 
            pstProd.setInt(4, item.getProduto().getId());
            pstProd.executeUpdate();
        }
        
        conn.commit();
        return idGerado; 
        
    } catch (Exception e) {
        System.err.println(" ERRO NO DAO AO SALVAR: " + e.getMessage());
        try { 
            if (conn != null) {
                System.out.println(" Executando Rollback");
                conn.rollback(); 
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        e.printStackTrace(); 
        return -1;
    } finally {
        Conexao.fecharConexao();
    }
}

    public Compra pesquisar(int id) {
    try {
        conn = Conexao.getConnection();
        Statement stmt = conn.createStatement();
        
        String sql = "SELECT c.id, c.data_compra, c.valor_total, c.fornecedor_id, " +
                     "cp.quantidade, cp.valor_unitario, cp.produto_id, " +
                     "f.nome_fantasia AS nome_fornecedor, " +
                     "p.nome AS nome_produto " +
                     "FROM compra c " +
                     "INNER JOIN compra_produto cp ON c.id = cp.compra_id " +
                     "INNER JOIN fornecedor f ON f.id = c.fornecedor_id " +
                     "INNER JOIN produto p ON p.id = cp.produto_id " +
                     "WHERE c.id = " + id;
        
        ResultSet rs = stmt.executeQuery(sql);
        
        Compra c = null;

        while (rs.next()) {
            if (c == null) {
                c = new Compra();
                c.setId(rs.getInt("id"));
                c.setDataCompra(rs.getDate("data_compra").toLocalDate());
                c.setValorTotal(rs.getDouble("valor_total"));
                
                Fornecedor forn = new Fornecedor();
                forn.setId(rs.getInt("fornecedor_id"));
                forn.setNomeFantasia(rs.getString("nome_fornecedor"));
                c.setFornecedor(forn);
            }

            CompraProduto item = new CompraProduto();
            item.setQuantidade(rs.getDouble("quantidade"));
            item.setValorUnitario(rs.getDouble("valor_unitario"));
            
            Produto prod = new Produto();
            prod.setId(rs.getInt("produto_id"));
            prod.setNome(rs.getString("nome_produto"));
            item.setProduto(prod);
            
            c.getCompraProdutos().add(item);
        }
        return c; 
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        Conexao.fecharConexao();
    }
    return null;
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