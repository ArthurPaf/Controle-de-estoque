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
            // CORREÇÃO: nomes das colunas conforme o banco
            ResultSet rs = stmt.executeQuery("SELECT id, nome, preco_medio, qtde_estoque FROM produto WHERE id = " + idProduto);
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setPreco(rs.getDouble("preco_medio"));
                produto.setQuantidade(rs.getDouble("qtde_estoque"));
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
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from produto");
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt(1));
                produto.setNome(rs.getString(2));
                produto.setPreco(rs.getDouble(3));
                produto.setQuantidade(rs.getDouble(4));
                produtos.add(produto);
            }
            rs.close();
            stmt.close();
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
            // CORREÇÃO: Adicionado categoria_id no INSERT
            String sql = "INSERT INTO produto (id, nome, preco_medio, qtde_estoque, categoria_id) VALUES ("
                    + produto.getId() + ", '" 
                    + produto.getNome() + "', " 
                    + produto.getPreco() + ", " 
                    + produto.getQuantidade() + ", " 
                    + produto.getCategoria().getId() + ")";
            
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
            // CORREÇÃO: nomes das colunas ajustados para preco_medio e qtde_estoque
            String sql = "UPDATE produto SET nome = '" + produto.getNome()
                    + "', preco_medio = " + produto.getPreco() 
                    + ", qtde_estoque = " + produto.getQuantidade()
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
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            int qtdeLinhas = stmt.executeUpdate("delete from produto where id = " + id);
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

    public boolean atualizarEstoque(Produto produto, int quantidade) {
        Produto produtoExistente = pesquisar(produto.getId());
        if (produtoExistente == null) return false;

        double novaQuantidade = produtoExistente.getQuantidade() - quantidade;
        produto.setQuantidade(novaQuantidade);
        return alterar(produto);
    }
}
