package controle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import bd.ConexaoBD;

public class ControleEstoque {
	// Verifica pela coluna item se um item existe na tabela do BD
    public static boolean itemExiste(String nomeItem) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "SELECT COUNT(*) FROM estoque WHERE item = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nomeItem);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;  // Retorna true se o item existe, false caso contrário
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao verificar existência do item.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }
    
    // Obtem a quantidade atual de um item na tabela do BD onde a coluna item for igual ao parâmetro nomeItem
    public static int obterQuantidadeAtual(String nomeItem) {
        String sql = "SELECT quantidade FROM estoque WHERE item = ?";
        try (Connection conn = ConexaoBD.obterConexao();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nomeItem);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("quantidade");  // Retorna a quantidade do item
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Retornar 0 se o item não for encontrado ou em caso de erro
    }
    
    // Adiciona um novo item na tabela do BD
    public static void adicionarItem(String nomeItem, int quantidade) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "INSERT INTO estoque (item, quantidade) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nomeItem);
            stmt.setInt(2, quantidade);
            stmt.executeUpdate();  // Executa a inserção do novo item
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar item.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Define a quantidade do item para (quantidade atual no BD + parâmetro quantidade) onde a coluna item for igual ao parâmetro nomeItem
    public static void somarQuantidadeItem(String nomeItem, int quantidade) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "UPDATE estoque SET quantidade = quantidade + ? WHERE item = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, quantidade);
            stmt.setString(2, nomeItem);
            stmt.executeUpdate();  // Executa a atualização da quantidade do item
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao adicionar quantidade.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Deleta um item da tabela do BD onde a coluna item for igual ao parâmetro nomeItem
    public static void deletarItem(String nomeItem) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "DELETE FROM estoque WHERE item = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nomeItem);
            stmt.executeUpdate();  // Executa a exclusão do item
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao deletar item.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Define a quantidade de um item da tabela no BD (quantidade atual no BD - parâmetro quantidade) onde a coluna item for igual ao parâmetro nomeItem
    public static void subtrairQuantidadeItem(String nomeItem, int quantidade) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "UPDATE estoque SET quantidade = quantidade - ? WHERE item = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, quantidade);
            stmt.setString(2, nomeItem);
            stmt.executeUpdate();  // Executa a subtração da quantidade do item
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao subtrair quantidade.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Define o nome de um item da tabela no BD para o valor do parâmetro "novoNome"
    public static void alterarNomeItem(String nomeItem, String novoNome) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "UPDATE estoque SET item = ? WHERE item = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, novoNome);
            stmt.setString(2, nomeItem);
            stmt.executeUpdate();  // Executa a alteração do nome do item
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao alterar nome do item.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Define a quantidade atual de um item da tabela no BD para o valor do parâmetro "quantidade" onde a coluna item for igual ao parâmetro nomeItem
    public static void definirQuantidadeItem(String nomeItem, int quantidade) {
        try (Connection conn = ConexaoBD.obterConexao()) {
            String query = "UPDATE estoque SET quantidade = ? WHERE item = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, quantidade);
            stmt.setString(2, nomeItem);
            stmt.executeUpdate();  // Executa a definição da quantidade do item
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao definir quantidade.",
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
        }
    }
}
