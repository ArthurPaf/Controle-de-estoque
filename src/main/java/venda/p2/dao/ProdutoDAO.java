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
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * from produto where id = " + idProduto);
            if (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt(1));
                produto.setNome(rs.getString(2));
                produto.setPreco(rs.getDouble(3));
                produto.setQuantidade(rs.getDouble(4));
                rs.close();
                stmt.close();
                return produto;
            } else {
                rs.close();
                stmt.close();
                return null;
            }
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
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            int qtdeLinhas = stmt.executeUpdate("insert into produto (nome, preco, quantidade) values ('"
                    + produto.getNome() + "', " + produto.getPreco()
                    + ", " + produto.getQuantidade() + ")");
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

    public boolean alterar(Produto produto) {
        try {
            conn = Conexao.getConnection();
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            int qtdeLinhas = stmt.executeUpdate("update produto set nome = '" + produto.getNome()
                    + "', preco = " + produto.getPreco() + ", quantidade = " + produto.getQuantidade()
                    + " where id = " + produto.getId());
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
        if (produtoExistente == null) {
            return false;
        }

        double novaQuantidade = produtoExistente.getQuantidade() - quantidade;
        produto.setQuantidade(novaQuantidade);
        return alterar(produto);
    }
}
