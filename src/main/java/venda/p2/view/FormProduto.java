package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.ProdutoController;
import venda.p2.model.Categoria;
import venda.p2.model.Produto;

public class FormProduto extends JFrame {

    private JTextField txtNome, txtPreco, txtEstoque;
    private JComboBox<Categoria> cbCategoria;
    private JButton btnSalvar, btnExcluir, btnEditar, btnLimpar;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    
    // View comunicando-se estritamente com a camada de controle
    private ProdutoController produtoController;
    private Produto produtoSelecionado;

    public FormProduto() {
        produtoController = new ProdutoController();

        setTitle("Gerenciar Produtos (CRUD)");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. FORMULÁRIO DE CADASTRO (Parte Superior) ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);
        txtPreco = new JTextField(10);
        txtEstoque = new JTextField(10);
        cbCategoria = new JComboBox<>();

        // Adicionando componentes na Grid
        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("Estoque Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtEstoque, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(cbCategoria, gbc);

        // --- 2. PAINEL DE BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar Novo");
        btnEditar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar/Cancelar");

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- 3. TABELA DE PRODUTOS (Parte Inferior) ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloqueia a edição direta na célula
            }
        };
        
        tabelaProdutos = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. EVENTOS E LISTENERS ---

        // Evento de clique na tabela para carregar dados nos campos
        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaProdutos.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
                    try {
                        produtoSelecionado = produtoController.buscarPorId(id);
                        if (produtoSelecionado != null) {
                            txtNome.setText(produtoSelecionado.getNome());
                            txtPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
                            txtEstoque.setText(String.valueOf(produtoSelecionado.getQuantidade()));
                            
                            if (produtoSelecionado.getCategoria() != null) {
                                for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                                    Categoria cat = cbCategoria.getItemAt(i);
                                    if (cat.getId() == produtoSelecionado.getCategoria().getId()) {
                                        cbCategoria.setSelectedIndex(i);
                                        break;
                                    }
                                }
                            }

                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormProduto.this, "Erro ao carregar dados do produto: " + ex.getMessage());
                    }
                }
            }
        });

        // Botão Salvar Novo
        btnSalvar.addActionListener(e -> {
            try {
                produtoController.salvarProduto(
                    txtNome.getText(),
                    txtPreco.getText(),
                    txtEstoque.getText(),
                    (Categoria) cbCategoria.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao salvar", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Botão Editar/Atualizar
        btnEditar.addActionListener(e -> {
            if (produtoSelecionado != null) {
                try {
                    produtoController.atualizarProduto(
                        produtoSelecionado,
                        txtNome.getText(),
                        txtPreco.getText(),
                        txtEstoque.getText(),
                        (Categoria) cbCategoria.getSelectedItem()
                    );
                    JOptionPane.showMessageDialog(this, "Produto updated com sucesso!");
                    btnLimpar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro ao atualizar", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (produtoSelecionado != null) {
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir o produto " + produtoSelecionado.getNome() + "?", 
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                    
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        produtoController.excluirProduto(produtoSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage() + 
                            "\n(Pode estar acoplado a uma venda/compra ativa)");
                    }
                }
            }
        });

        // Botão Limpar / Atualização Gráfica Geral
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtPreco.setText("");
            txtEstoque.setText("");
            if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
            
            produtoSelecionado = null;
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            modeloTabela.setRowCount(0);
            try {
                List<Produto> produtos = produtoController.listarTodos();
                for (Produto p : produtos) {
                    String nomeCategoria = (p.getCategoria() != null) ? p.getCategoria().toString() : "Sem Categoria";
                    modeloTabela.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getPreco(),
                        p.getQuantidade(),
                        nomeCategoria
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar produtos: " + ex.getMessage());
            }
        });

        // Carga Inicial do ComboBox de Categorias Relacionadas
        try {
            cbCategoria.removeAllItems();
            List<Categoria> categorias = produtoController.listarCategorias();
            for (Categoria cat : categorias) {
                cbCategoria.addItem(cat); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias no combo: " + e.getMessage());
        }

        // Força a primeira varredura/limpeza gráfica ao renderizar o Jframe
        btnLimpar.doClick();
    } // <-- Fim do Construtor
}