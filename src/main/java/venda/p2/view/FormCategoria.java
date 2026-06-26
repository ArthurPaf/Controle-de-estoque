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
    private JTextField txtPesquisa; // NOVO CAMPO DE TEXTO PARA BUSCA
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar, btnPesquisar; // NOVO BOTÃO
    private JTable tabelaCategorias;
    private DefaultTableModel modeloTabela;
    
    private CategoriaController categoriaController;
    private Categoria categoriaSelecionada;

    public FormCategoria() {
        categoriaController = new CategoriaController();
        
        setTitle("Gerenciar Categorias (CRUD)");
        setSize(550, 520); // Aumentado um pouco o tamanho vertical para acomodar a busca
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        
        // --- PAINEL PRINCIPAL (Agrupa o Formulário e a Pesquisa no Topo) ---
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        // --- 1. FORMULÁRIO DE CADASTRO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados da Categoria"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome da Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        // --- 2. PAINEL DE BOTÕES DE AÇÃO ---
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
        
        painelTopo.add(painelCampos);

        // --- NOVO: 2.5 PAINEL DE PESQUISA ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Pesquisar Categorias"));
        txtPesquisa = new JTextField(25);
        btnPesquisar = new JButton("Pesquisar");
        
        painelBusca.add(new JLabel("Nome:"));
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);
        
        painelTopo.add(painelBusca);

        add(painelTopo, BorderLayout.NORTH);

        // --- 3. TABELA DE CATEGORIAS ---
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

        // --- 4. EVENTOS E AÇÕES ---

        // Evento de clique na tabela
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
                    
                    JOptionPane.showMessageDialog(this, "Categoria updated com sucesso!");
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

        // NOVO: Ação do Botão Pesquisar
        btnPesquisar.addActionListener(e -> {
            String termoBusca = txtPesquisa.getText().trim();
            try {
                // Chama o método que colocamos no controller
                List<Categoria> resultado = categoriaController.pesquisarPorNome(termoBusca);
                atualizarTabela(resultado);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage());
            }
        });

        // Ação do Botão Limpar / Reseta os campos e traz a lista cheia
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtPesquisa.setText(""); // Limpa o campo de busca também
            categoriaSelecionada = null;
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);
            
            try {
                List<Categoria> listaCompleta = categoriaController.listarCategorias();
                atualizarTabela(listaCompleta);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar tabela: " + ex.getMessage());
            }
        });

        // Carrega os dados cheios na primeira abertura da janela
        btnLimpar.doClick();
    }

    // MÉTODO AUXILIAR PARA REPREENCHER AS LINHAS DA TABELA
    private void atualizarTabela(List<Categoria> lista) {
        modeloTabela.setRowCount(0);
        for (Categoria cat : lista) {
            modeloTabela.addRow(new Object[]{ cat.getId(), cat.getNome() });
        }
    }
}