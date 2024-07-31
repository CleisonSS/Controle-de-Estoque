package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.SQLTimeoutException;

public class ConexaoBD {
    private static final String URL = "jdbc:postgresql://localhost:5432/EstoqueDB";  // URL do banco de dados
    private static final String USUARIO = "postgres";  // Nome de usuário do BD
    private static final String SENHA = "123";  // Senha do BD
    private static Connection conn = null;

    // Método para obter a conexão com o BD
    public static Connection obterConexao() throws SQLException {
        if (conn == null || conn.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver");  // Carrega o driver do PostgreSQL
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver do PostgreSQL não encontrado.", e);
            } catch (ExceptionInInitializerError e) {
                throw new SQLException("Falha na inicialização ao carregar o driver do PostgreSQL.", e);
            } catch (LinkageError e) {
                throw new SQLException("Falha de ligação ao carregar o driver do PostgreSQL.", e);
            }

            // Cria e retorna a conexão com o BD
            conn = DriverManager.getConnection(URL, USUARIO, SENHA);
        }
        return conn;
    }

    // Método para criar a tabela "estoque" caso ela não exista
    public static void criarTabelaEstoque() {
        String sql = "CREATE TABLE IF NOT EXISTS estoque (" +
                     "id SERIAL PRIMARY KEY, " +
                     "item VARCHAR(255) NOT NULL, " +
                     "quantidade INT NOT NULL" +
                     ");";
        
        try (Connection conn = obterConexao();
             Statement stmt = conn.createStatement()) {
            stmt.setQueryTimeout(30); // Configura o tempo limite para 30 segundos
            stmt.executeUpdate(sql);  // Executa o comando SQL para criar a tabela
        } catch (NullPointerException e) {
            System.err.println("Erro: Um argumento nulo foi encontrado. \nVerifique a configuração da conexão.");
        } catch (SQLTimeoutException e) {
            // Trata a exceção de tempo limite e encerra o aplicativo
            System.err.println("Erro: O comando SQL excedeu o tempo limite.\nO aplicativo será encerrado.");
            fecharConexao();
            System.exit(0); // Encerra o aplicativo
        } catch (SQLException e) {
            System.err.println("Erro: Problema ao executar comando SQL.");
        }
    }

    // Método para fechar a conexão com o BD
    public static void fecharConexao() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                System.out.println("Conexão com o banco de dados fechada.");
            } catch (SQLException e) {
                System.err.println("Erro ao fechar a conexão com o banco de dados.");
                e.printStackTrace();
            }
        }
    }
}
