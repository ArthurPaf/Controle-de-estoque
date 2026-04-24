package venda.p2.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
    // Ajuste o nome do banco de dados conforme o seu projeto
    private static final String URL = "jdbc:postgresql://127.0.0.1:5432/postgres";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "1234";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return connection;
    }

    public static void fecharConexao() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
