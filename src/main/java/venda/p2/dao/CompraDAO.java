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

    public boolean salvar(Compra compra) {
    try {
        conn = Conexao.getConnection();
        conn.setAutoCommit(false); 
        
        // 1. Inserimos a COMPRA sem passar o ID (deixamos o Serial do banco agir)
        // Usamos RETURN_GENERATED_KEYS para o banco nos devolver o ID criado
        String sqlCompra = "INSERT INTO compra (fornecedor_id, data_compra, valor_total) VALUES (?, ?, ?)";
        PreparedStatement pstCompra = conn.prepareStatement(sqlCompra, Statement.RETURN_GENERATED_KEYS);
        
        pstCompra.setInt(1, compra.getFornecedor().getId());
        pstCompra.setDate(2, java.sql.Date.valueOf(compra.getDataCompra()));
        pstCompra.setDouble(3, compra.getValorTotal());
        
        pstCompra.executeUpdate();
        
        // Pega o ID que o Postgres acabou de criar para essa compra
        ResultSet rs = pstCompra.getGeneratedKeys();
        int idGerado = 0;
        if (rs.next()) {
            idGerado = rs.getInt(1);
        }

        // 2. Insere os itens da compra_produto usando o ID gerado
        String sqlItem = "INSERT INTO compra_produto (compra_id, produto_id, quantidade, valor_unitario) VALUES (?, ?, ?, ?)";
        PreparedStatement pstItem = conn.prepareStatement(sqlItem);

        for (CompraProduto item : compra.getCompraProdutos()) {
            pstItem.setInt(1, idGerado); // Usa o ID que o banco criou agora pouco
            pstItem.setInt(2, item.getProduto().getId());
            pstItem.setDouble(3, item.getQuantidade());
            pstItem.setDouble(4, item.getValorUnitario());
            pstItem.executeUpdate();
            
            // DICA: Aproveite para atualizar o estoque do produto aqui!
            // prodDAO.adicionarEstoque(item.getProduto().getId(), item.getQuantidade());
        }
        
        conn.commit();
        return true;
        
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
        
        // Fazendo o JOIN triplo: Compra + Fornecedor + Produto
        String sql = "SELECT cp.*, f.nome AS nome_fornecedor, p.nome AS nome_produto " +
                     "FROM compra cp " +
                     "INNER JOIN fornecedor f ON f.id = cp.fornecedor_id " +
                     "INNER JOIN produto p ON p.id = cp.produto_id " +
                     "WHERE cp.id = " + id;
        
        ResultSet rs = stmt.executeQuery(sql);
        
        if (rs.next()) {
            Compra c = new Compra();
            CompraProduto cp = new CompraProduto(); // Associa a compra ao item 
            c.setId(rs.getInt("id"));
            c.setDataCompra(rs.getDate("data_compra").toLocalDate());
            cp.setQuantidade(rs.getDouble("quantidade"));
            cp.setValorUnitario(rs.getDouble("valor_unitario"));
            c.setValorTotal(rs.getDouble("valor_total"));

            // 1. Montando o objeto Fornecedor que veio do JOIN
            Fornecedor forn = new Fornecedor();
            forn.setId(rs.getInt("fornecedor_id"));
            forn.setNomeFantasia(rs.getString("nome_fornecedor"));
            c.setFornecedor(forn); // Associa à compra

            // 2. Montando o objeto Produto que veio do JOIN
            Produto prod = new Produto();
            prod.setId(rs.getInt("produto_id"));
            prod.setNome(rs.getString("nome_produto"));
            cp.setProduto(prod); // Associa à compra

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

    public CompraProduto pesquisarCompraProduto(int compraId) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM compra_produto WHERE compra_id = " + compraId);
            if (rs.next()) {
                CompraProduto cp = new CompraProduto();
                cp.setCompra(pesquisar(compraId)); // Associa a compra
                // Aqui você pode querer associar o produto também, se necessário
                cp.setQuantidade(rs.getDouble("quantidade"));
                cp.setValorUnitario(rs.getDouble("valor_unitario"));
                return cp;
            }
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