package venda.p2.dao;

import java.sql.Connection;
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
        try {
            conn = Conexao.getConnection();
            System.out.println("Conectado com sucesso!");
            Statement stmt = conn.createStatement();
            int qtdeVendas = stmt.executeUpdate("SELECT COUNT(*) FROM venda WHERE cliente_id = " + clienteId);
            stmt.close();
            if (qtdeVendas < 3) {
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

}
