package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.CategoriaController;
import venda.p2.model.Categoria;

public class FormCategoria extends JFrame {
    
    private JTextField txtNome;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaCategorias;
    private DefaultTableModel modeloTabela;
    
    // View conversa estritamente com o Controller correspondente
    private CategoriaController categoriaController;
    private Categoria categoriaSelecionada;

    public FormCategoria() {
        categoriaController = new CategoriaController();
        
        setTitle("Gerenciar Categorias (CRUD)");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // --- 1. FORMULÁRIO DE CADASTRO (Superior) ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados da Categoria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome da Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        // --- 2. PAINEL DE BOTÕES ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar Nova");
        btnEditar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- 3. TABELA DE CATEGORIAS (Centro/Inferior) ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome da Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        tabelaCategorias = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaCategorias);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. EVENTOS E AÇÕES DOS BOTÕES (A classe encerra aqui!) ---

        // Evento de clique na tabela: busca a entidade pelo id e atualiza estado visual dos campos
        tabelaCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaCategorias.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
                    try {
                        categoriaSelecionada = categoriaController.buscarPorId(id);
                        if (categoriaSelecionada != null) {
                            txtNome.setText(categoriaSelecionada.getNome());
                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormCategoria.this, "Erro ao carregar: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (nome.isEmpty()) {
                JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Categoria novaCategoria = new Categoria();
                novaCategoria.setNome(nome);
                
                categoriaController.salvarCategoria(novaCategoria);
                
                JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!");
                btnLimpar.doClick(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        // Ação do Botão Editar
        btnEditar.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            if (categoriaSelecionada != null && !nome.isEmpty()) {
                try {
                    categoriaSelecionada.setNome(nome);
                    
                    categoriaController.salvarCategoria(categoriaSelecionada);
                    
                    JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");
                    btnLimpar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
                }
            }
        });

        // Ação do Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (categoriaSelecionada != null) {
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir a categoria '" + categoriaSelecionada.getNome() + "'?", 
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                    
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        categoriaController.excluirCategoria(categoriaSelecionada.getId());
                        JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage() + 
                            "\n(Provavelmente existem produtos vinculados a esta categoria!)", "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Ação do Botão Limpar / Responsável também por resetar o estado e reatualizar a tabela
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            categoriaSelecionada = null;
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);
            
            modeloTabela.setRowCount(0);
            try {
                List<Categoria> lista = categoriaController.listarCategorias();
                for (Categoria cat : lista) {
                    modeloTabela.addRow(new Object[]{ cat.getId(), cat.getNome() });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar tabela: " + ex.getMessage());
            }
        });

        // Carrega os dados na JTable na primeira abertura da janela
        btnLimpar.doClick();
    }
}