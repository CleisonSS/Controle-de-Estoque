package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import controle.ControleEstoque;
import bd.ConexaoBD;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.Component;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class Main {
	private JFrame frmEstoque;
	private JTextField nomeDoItemField;
	private JTextField quantidadeField;
	private JTable tabelaEstoque;
	private JComboBox<String> ordenarPorComboBox;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmEstoque.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public Main() {
		initialize();  // Inicializa o conteúdo do frmEstoque.
		ConexaoBD.criarTabelaEstoque(); // Caso a tabela "estoque" não exista no banco de dados ela será criada
		atualizarTabelaEstoque("item", "ASC"); // Definine a ordenação inicial por "item" de forma ascendente
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		// Cria e configura o frmEstoque
		frmEstoque = new JFrame();
		frmEstoque.setTitle("Controle de Estoque");
        frmEstoque.setSize(800, 400);  //  largura, altura inicial
        frmEstoque.setLocationRelativeTo(null);  //  Centraliza a janela na tela
		frmEstoque.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
        frmEstoque.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ConexaoBD.fecharConexao();
                System.exit(0);
            }
        });
		
		// Cria o painel de entrada de dados
		JPanel inputPanel = new JPanel();
		inputPanel.setBorder(new EmptyBorder(10, 10, 0, 10));
		frmEstoque.getContentPane().add(inputPanel, BorderLayout.NORTH);
		inputPanel.setLayout(new GridLayout(7, 2));
		
		// Exibe um texto informando ao usuário o que deve ser informado no campo ao lado
		JLabel lblNomeDoItem = new JLabel("Nome do Item:  ");
		lblNomeDoItem.setHorizontalAlignment(SwingConstants.RIGHT);
		inputPanel.add(lblNomeDoItem);
		
		// Adiciona o campo para inserir o nome do item
		nomeDoItemField = new JTextField(20);
		inputPanel.add(nomeDoItemField);
		
		// Exibe um texto informando ao usuário o que deve ser informado no campo ao lado
		JLabel lblQuantidade = new JLabel("Quantidade:  ");
		lblQuantidade.setHorizontalAlignment(SwingConstants.RIGHT);
		inputPanel.add(lblQuantidade);
		
		// Adiciona o campo para inserir a quantidade do item
		quantidadeField = new JTextField(20);
		inputPanel.add(quantidadeField);
		
		// Adiciona um botão para criar um item
		JButton criarButton = new JButton("Criar Item");
		criarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				criarItem();
                atualizarTabelaEstoque(((String) ordenarPorComboBox.getSelectedItem()).toLowerCase(), "ASC");
			}
		});
		inputPanel.add(criarButton);
		
		// Adiciona um botão para adicionar uma quantia ao item
		JButton adicionarButton = new JButton("Adicionar Quantidade");
		adicionarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				adicionarQuantidade();
                atualizarTabelaEstoque(((String) ordenarPorComboBox.getSelectedItem()).toLowerCase(), "ASC");
			}
		});
		inputPanel.add(adicionarButton);
		
		// Adiciona um botão para deletar o item
		JButton deletarButton = new JButton("Deletar Item");
		deletarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deletarItem();
                atualizarTabelaEstoque(((String) ordenarPorComboBox.getSelectedItem()).toLowerCase(), "ASC");
			}
		});
		inputPanel.add(deletarButton);
		
		// Adiciona um botão para subtrair uma quantia do item
		JButton subtrairButton = new JButton("Subtrair Quantidade");
		subtrairButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subtrairQuantidade();
                atualizarTabelaEstoque(((String) ordenarPorComboBox.getSelectedItem()).toLowerCase(), "ASC");
			}
		});
		inputPanel.add(subtrairButton);
		
		// Adiciona um botão para alterar o nome do item
		JButton alterarNomeButton = new JButton("Alterar Nome do Item");
		alterarNomeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				alterarNome();
                atualizarTabelaEstoque(((String) ordenarPorComboBox.getSelectedItem()).toLowerCase(), "ASC");
			}
		});
		inputPanel.add(alterarNomeButton);
		
		// Adiciona um botão para definir a quantidade do item
		JButton definirQuantidadeButton = new JButton("Definir Quantidade");
		definirQuantidadeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				definirQuantidade();
                atualizarTabelaEstoque(((String) ordenarPorComboBox.getSelectedItem()).toLowerCase(), "ASC");
			}
		});
		inputPanel.add(definirQuantidadeButton);
		
		// Exibe um texto informando ao usuário o que o campo ao lado faz
		JLabel lblOrdenarPor = new JLabel("Ordenar por:  ");
		lblOrdenarPor.setHorizontalAlignment(SwingConstants.RIGHT);
		inputPanel.add(lblOrdenarPor);
		
		// Adiciona um JComboBox para que o usuário possa alterar a ordenação da tabela
        ordenarPorComboBox = new JComboBox<>();
        ordenarPorComboBox.addItem("Item");
        ordenarPorComboBox.addItem("ID");
        ordenarPorComboBox.addItem("Quantidade");
        ordenarPorComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String colunaSelecionada = (String) ordenarPorComboBox.getSelectedItem();
                if (colunaSelecionada.equals("ID")) {
                    atualizarTabelaEstoque("id", "ASC");
                } else if (colunaSelecionada.equals("Item")) {
                    atualizarTabelaEstoque("item", "ASC");
                } else if (colunaSelecionada.equals("Quantidade")) {
                    atualizarTabelaEstoque("quantidade, item", "ASC");
                }
            }
        });
		inputPanel.add(ordenarPorComboBox);
		
		// Configuração da tabela
        tabelaEstoque = new JTable();
        tabelaEstoque.setEnabled(true);
        tabelaEstoque.setAlignmentX(Component.LEFT_ALIGNMENT);
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBorder(new EmptyBorder(0, 10, 10, 10));
        frmEstoque.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        // ListSelectionListener para atualizar o campo "nomeDoItemField" quando o usuário selecionar uma linha na tabela exibida no app
        tabelaEstoque.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting()) {
                    int selectedRow = tabelaEstoque.getSelectedRow();
                    if (selectedRow != -1) {
                        nomeDoItemField.setText(tabelaEstoque.getValueAt(selectedRow, 1).toString());
                    }
                }
            }
        });
	}
	
	// Métodos
	
	// Cria o item e o adiciona ao estoque
	private void criarItem() {
        String nome = nomeDoItemField.getText();
        String quantidadeTexto = quantidadeField.getText();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'Nome do Item' não pode estar vazio.",
                    "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!quantidadeTexto.matches("\\d+")) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'quantidade' só aceita caracteres numéricos.",
                    "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantidade = Integer.parseInt(quantidadeTexto);
        
        if (ControleEstoque.itemExiste(nome)) {
            JOptionPane.showMessageDialog(frmEstoque, "O item '" + nome + "' já existe.",
                    "Item Já Existe", JOptionPane.WARNING_MESSAGE);
        } else {
            // Realiza a alteração no banco de dados
            ControleEstoque.adicionarItem(nome, quantidade);
        }
    }
	
	// Adiciona uma quantia ao item no estoque
    private void adicionarQuantidade() {
        String nome = nomeDoItemField.getText();
        String quantidadeTexto = quantidadeField.getText();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'Nome do Item' não pode estar vazio.",
                    "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!ControleEstoque.itemExiste(nome)) {
            JOptionPane.showMessageDialog(frmEstoque, "O item '" + nome + "' não existe.",
                    "Item Não Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!quantidadeTexto.matches("\\d+")) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'quantidade' só aceita caracteres numéricos.",
                    "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantidade = Integer.parseInt(quantidadeTexto);
        
        // Realiza a alteração no banco de dados
        ControleEstoque.somarQuantidadeItem(nome, quantidade);
    }
    
    // Deleta o item do estoque
    private void deletarItem() {
        String nome = nomeDoItemField.getText();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'Nome do Item' não pode estar vazio.",
                    "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!ControleEstoque.itemExiste(nome)) {
            JOptionPane.showMessageDialog(frmEstoque, "O item '" + nome + "' não existe.",
                    "Item Não Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(frmEstoque, "Tem certeza que quer deletar o item '" + nome + "'?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            // Realiza a alteração no banco de dados
            ControleEstoque.deletarItem(nome);
            
            // Após a exclusão, limpa os campos "nomeDoItemField" e "quantidadeField"
            nomeDoItemField.setText("");
            quantidadeField.setText("");
        }
    }
    
    // Subtrai a quantidade do item no estoque pela quantidade inserida no campo "quantidadeField"
    private void subtrairQuantidade() {
        String nome = nomeDoItemField.getText();
        String quantidadeTexto = quantidadeField.getText();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'Nome do Item' não pode estar vazio.",
                    "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!ControleEstoque.itemExiste(nome)) {
            JOptionPane.showMessageDialog(frmEstoque, "O item '" + nome + "' não existe.",
                    "Item Não Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!quantidadeTexto.matches("\\d+")) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'quantidade' só aceita caracteres numéricos.",
                    "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantidade = Integer.parseInt(quantidadeTexto);
        int quantidadeAtual = ControleEstoque.obterQuantidadeAtual(nome);
        
        if (quantidade > quantidadeAtual) {
            int faltou = quantidade - quantidadeAtual;
            JOptionPane.showMessageDialog(frmEstoque, "Subtração NÃO realizada!\nFaltou " + faltou + " item(s) no estoque.",
                    "Estoque Insuficiente", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Realiza a alteração no banco de dados
        ControleEstoque.subtrairQuantidadeItem(nome, quantidade);
    }
    
    // Define a quantidade do item no estoque
    private void definirQuantidade() {
        String nome = nomeDoItemField.getText();
        String quantidadeTexto = quantidadeField.getText();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'Nome do Item' não pode estar vazio.",
                    "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!ControleEstoque.itemExiste(nome)) {
            JOptionPane.showMessageDialog(frmEstoque, "O item '" + nome + "' não existe.",
                    "Item Não Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!quantidadeTexto.matches("\\d+")) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'quantidade' só aceita caracteres numéricos.",
                    "Quantidade Inválida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int quantidade = Integer.parseInt(quantidadeTexto);
        
        // Realiza a alteração no banco de dados
        ControleEstoque.definirQuantidadeItem(nome, quantidade);
    }
    
    // Altera o nome do item no estoque
    private void alterarNome() {
    	String nome = nomeDoItemField.getText();
        
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O campo 'Nome do Item' não pode estar vazio.",
                    "Campo Vazio", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!ControleEstoque.itemExiste(nome)) {
            JOptionPane.showMessageDialog(frmEstoque, "O item '" + nome + "' não existe.",
                    "Item Não Encontrado", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nomeNovo = JOptionPane.showInputDialog(frmEstoque, "Digite um novo nome para o item:", nome);
        
        if (nomeNovo == null) {
            return; // Se o usuário cancelar, retorna sem mostrar nenhuma mensagem
        }
        
        if (nomeNovo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frmEstoque, "O novo nome não pode estar vazio.",
                    "Nome Inválido", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (ControleEstoque.itemExiste(nomeNovo)) {
            JOptionPane.showMessageDialog(frmEstoque, "Alteração não efetuada!\nJá existe um item chamado '" + nomeNovo + "'.",
                    "Item Já Existe", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Realiza a alteração no banco de dados
        ControleEstoque.alterarNomeItem(nome, nomeNovo);
        
        // Atualiza o campo "nomeDoItemField" na interface gráfica
        nomeDoItemField.setText(nomeNovo);
    }
    
    // Atualiza a tabela de estoque na interface gráfica
    private void atualizarTabelaEstoque(String ordenarPor, String ordem) {
        try {
            Connection conn = ConexaoBD.obterConexao();
            String sql;
            if (ordenarPor.equals("quantidade, item")) {
                sql = "SELECT * FROM estoque ORDER BY quantidade " + ordem + ", item " + ordem;
            } else {
                sql = "SELECT * FROM estoque ORDER BY " + ordenarPor + " " + ordem;
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            
            // Cria o modelo de tabela
            DefaultTableModel model = new DefaultTableModel() {
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;
				
				@Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            model.addColumn("ID");
            model.addColumn("Item");
            model.addColumn("Quantidade");
            
            // Preenche a tabela com os dados do ResultSet
            while (rs.next()) {
                Object[] row = { rs.getInt("id"), rs.getString("item"), rs.getInt("quantidade") };
                model.addRow(row);
            }
            
            // Seta o modelo na tabela
            tabelaEstoque.setModel(model);
            
            // Fecha os recursos
            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frmEstoque, "Erro ao buscar dados do estoque:\n" + e.getMessage(),
                    "Erro de Banco de Dados", JOptionPane.ERROR_MESSAGE);
            System.exit(0); // Encerra o aplicativo
        }
    }
}
