package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Categoria;

public class FormCategoria extends JFrame {
    
    private JTextField txtNome;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaCategorias;
    private DefaultTableModel modeloTabela;
    
    private GenericDAO<Categoria> categoriaDAO;
    
    // Variável para guardar a categoria selecionada para alteração/exclusão
    private Categoria categoriaSelecionada;

    public FormCategoria() {
        categoriaDAO = new GenericDAO<>(Categoria.class);
        
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

        // Desabilita botões de edição inicialmente
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
                return false; // Bloqueia edição direta nas células
            }
        };
        
        tabelaCategorias = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaCategorias);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Categorias Cadastradas"));
        add(scrollTabela, BorderLayout.CENTER);

        // Atualiza a tabela com o que já existe no banco
        atualizarTabela();

        // --- 4. EVENTOS ---

        // Evento de clique na tabela para selecionar
        tabelaCategorias.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherCamposPelaTabela();
            }
        });

        // Ações dos botões
        btnSalvar.addActionListener(e -> salvarCategoria());
        btnEditar.addActionListener(e -> editarCategoria());
        btnExcluir.addActionListener(e -> excluirCategoria());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Categoria> lista = categoriaDAO.listarTodos();
            for (Categoria cat : lista) {
                modeloTabela.addRow(new Object[]{
                    cat.getId(),
                    cat.getNome()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar categorias: " + e.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaCategorias.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            
            try {
                categoriaSelecionada = categoriaDAO.buscarPorId(id);
                
                if (categoriaSelecionada != null) {
                    txtNome.setText(categoriaSelecionada.getNome());
                    
                    // Modifica os botões
                    btnSalvar.setEnabled(false);
                    btnEditar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar categoria: " + ex.getMessage());
            }
        }
    }

    private void salvarCategoria() {
        String nome = txtNome.getText().trim();

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome da categoria é obrigatório.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Categoria novaCategoria = new Categoria();
            novaCategoria.setNome(nome);

            categoriaDAO.salvar(novaCategoria);
            JOptionPane.showMessageDialog(this, "Categoria cadastrada com sucesso!");
            limparCampos();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void editarCategoria() {
        String nome = txtNome.getText().trim();

        if (categoriaSelecionada != null && !nome.isEmpty()) {
            try {
                categoriaSelecionada.setNome(nome);

                categoriaDAO.salvar(categoriaSelecionada); // Atualiza no Hibernate
                JOptionPane.showMessageDialog(this, "Categoria atualizada com sucesso!");
                limparCampos();
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        }
    }

    private void excluirCategoria() {
        if (categoriaSelecionada != null) {
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir a categoria '" + categoriaSelecionada.getNome() + "'?", 
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    categoriaDAO.excluir(categoriaSelecionada.getId());
                    JOptionPane.showMessageDialog(this, "Categoria excluída com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage() + 
                        "\n(Provavelmente existem produtos vinculados a esta categoria!)", "Erro de Integridade", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        categoriaSelecionada = null;
        
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        atualizarTabela();
    }
}