package venda.p2.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import venda.p2.model.Categoria;
import venda.p2.model.Produto;

public class ProdutoDAO {
    Connection conn = null;

    public Produto pesquisar(int idProduto) {
    try {
        conn = Conexao.getConnection();
        String sql = "SELECT p.*, c.nome AS nome_da_categoria " +
                     "FROM produto p " +
                     "INNER JOIN categoria c ON c.id = p.categoria_id " +
                     "WHERE p.id = " + idProduto;
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        
        if (rs.next()) {
            Produto produto = new Produto();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setPreco(rs.getDouble("preco_medio"));
            produto.setQuantidade(rs.getDouble("qtde_estoque"));
            produto.setValor_ultima_compra(rs.getDouble("valor_ultima_compra"));
            produto.setValor_ultima_venda(rs.getDouble("valor_ultima_venda"));

            Categoria cat = new Categoria();
            cat.setId(rs.getInt("categoria_id"));
            cat.setNome(rs.getString("nome_da_categoria"));
            
            produto.setCategoria(cat);
            return produto;
        }
    } catch (Exception e) {
        System.out.println("Erro no DAO ao buscar produto: " + e.getMessage());
    }
    return null;
}

    

    public boolean salvar(Produto produto) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO produto (id, nome, preco_medio, qtde_estoque, categoria_id, valor_ultima_compra, valor_ultima_venda) VALUES ("
                    + produto.getId() + ", '" 
                    + produto.getNome() + "', " 
                    + produto.getPreco() + ", " 
                    + produto.getQuantidade() + ", " 
                    + produto.getCategoria().getId() + ", "
                    + (produto.getValor_ultima_compra() != null ? produto.getValor_ultima_compra() : 0.0) + ", "
                    + (produto.getValor_ultima_venda() != null ? produto.getValor_ultima_venda() : 0.0) + ")";
            
            int qtdeLinhas = stmt.executeUpdate(sql);
            return qtdeLinhas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao();
        }
    }

    public boolean alterar(Produto produto) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            
            String sql = "UPDATE produto SET nome = '" + produto.getNome()
                    + "', preco_medio = " + produto.getPreco() 
                    + ", qtde_estoque = " + produto.getQuantidade()
                    + ", valor_ultima_compra = " + produto.getValor_ultima_compra()
                    + ", valor_ultima_venda = " + produto.getValor_ultima_venda()
                    + " WHERE id = " + produto.getId();
            
            int qtdeLinhas = stmt.executeUpdate(sql);
            return qtdeLinhas > 0;
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
            int qtdeLinhas = stmt.executeUpdate("delete from produto where id = " + id);
            return qtdeLinhas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            Conexao.fecharConexao();
        }
    }
}