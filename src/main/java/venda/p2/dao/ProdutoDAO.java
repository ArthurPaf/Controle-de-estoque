package venda.p2.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import venda.p2.model.Produto;

public class ProdutoDAO {
    Connection conn = null;

    public Produto pesquisar(int idProduto) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            // Adicionado os novos campos no SELECT
            ResultSet rs = stmt.executeQuery("SELECT id, nome, preco_medio, qtde_estoque, valor_ultima_compra, valor_ultima_venda FROM produto WHERE id = " + idProduto);
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco_medio"));
                produto.setQuantidade(rs.getDouble("qtde_estoque"));
                produto.setValor_ultima_compra(rs.getDouble("valor_ultima_compra"));
                produto.setValor_ultima_venda(rs.getDouble("valor_ultima_venda"));
                return produto;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao();
        }
    }

    public List<Produto> pesquisarTodos() {
        try {
            List<Produto> produtos = new ArrayList<>();
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from produto");
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco_medio"));
                produto.setQuantidade(rs.getDouble("qtde_estoque"));
                produto.setValor_ultima_compra(rs.getDouble("valor_ultima_compra"));
                produto.setValor_ultima_venda(rs.getDouble("valor_ultima_venda"));
                produtos.add(produto);
            }
            return produtos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            Conexao.fecharConexao();
        }
    }

    public boolean salvar(Produto produto) {
        try {
            conn = Conexao.getConnection();
            Statement stmt = conn.createStatement();
            // Incluído valor_ultima_compra e valor_ultima_venda (iniciando com 0 se nulo)
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
            // Atualizando todos os campos novos no UPDATE
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

    // Método aprimorado para ser usado tanto na Compra (soma) quanto na Venda (subtrai)
    // Dentro do ProdutoDAO, o método atualizarEstoque deve ser simples:
public boolean atualizarEstoque(int idProduto, double novaQuantidade, double ultimoPreco, String tipo) {
    try {
        conn = Conexao.getConnection();
        Statement stmt = conn.createStatement();
        String colunaPreco = tipo.equals("VENDA") ? "valor_ultima_venda" : "valor_ultima_compra";
        
        String sql = "UPDATE produto SET qtde_estoque = " + novaQuantidade 
                   + ", " + colunaPreco + " = " + ultimoPreco 
                   + " WHERE id = " + idProduto;
        
        return stmt.executeUpdate(sql) > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
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