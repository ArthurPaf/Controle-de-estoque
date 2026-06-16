package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.FornecedorController;
import venda.p2.model.Fornecedor;

public class FormFornecedor extends JFrame {

    private JTextField txtNomeFantasia, txtRazaoSocial, txtCnpj;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaFornecedores;
    private DefaultTableModel modeloTabela;

    // View conversando estritamente com a camada de controle correspondente
    private FornecedorController fornecedorController;
    private Fornecedor fornecedorSelecionado;

    public FormFornecedor() {
        fornecedorController = new FornecedorController();

        setTitle("Gerenciar Fornecedores (CRUD)");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- FORMULÁRIO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Fornecedor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNomeFantasia = new JTextField(25);
        txtRazaoSocial = new JTextField(25);
        txtCnpj = new JTextField(18);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome Fantasia:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNomeFantasia, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Razão Social:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtRazaoSocial, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtCnpj, gbc);

        // --- BOTÕES ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar Novo");
        btnEditar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome Fantasia", "Razão Social", "CNPJ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tabelaFornecedores = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFornecedores);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Fornecedores Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- EVENTOS E LISTENERS ---

        // Clique na JTable para carregar o registro nos campos de texto
        tabelaFornecedores.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaFornecedores.getSelectedRow();
                if (linha >= 0) {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    try {
                        fornecedorSelecionado = fornecedorController.buscarPorId(id);
                        if (fornecedorSelecionado != null) {
                            txtNomeFantasia.setText(fornecedorSelecionado.getNomeFantasia());
                            txtRazaoSocial.setText(fornecedorSelecionado.getRazaoSocial());
                            txtCnpj.setText(fornecedorSelecionado.getCnpj());

                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormFornecedor.this, "Erro ao carregar: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> {
            try {
                fornecedorController.salvarFornecedor(
                    txtNomeFantasia.getText(),
                    txtRazaoSocial.getText(),
                    txtCnpj.getText()
                );
                JOptionPane.showMessageDialog(this, "Fornecedor salvo!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
            }
        });

        // Ação do Botão Editar (Atualizar)
        btnEditar.addActionListener(e -> {
            if (fornecedorSelecionado != null) {
                try {
                    fornecedorController.atualizarFornecedor(
                        fornecedorSelecionado,
                        txtNomeFantasia.getText(),
                        txtRazaoSocial.getText(),
                        txtCnpj.getText()
                    );
                    JOptionPane.showMessageDialog(this, "Fornecedor atualizado!");
                    btnLimpar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
                }
            }
        });

        // Ação do Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (fornecedorSelecionado != null) {
                int conf = JOptionPane.showConfirmDialog(this, 
                    "Excluir " + fornecedorSelecionado.getNomeFantasia() + "?", 
                    "Confirmação", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        fornecedorController.excluirFornecedor(fornecedorSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Excluído!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir (pode estar ligado a uma Compra).");
                    }
                }
            }
        });

        // Ação do Botão Limpar / Atualização Gráfica da Tabela
        btnLimpar.addActionListener(e -> {
            txtNomeFantasia.setText("");
            txtRazaoSocial.setText("");
            txtCnpj.setText("");
            fornecedorSelecionado = null;
            
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            modeloTabela.setRowCount(0);
            try {
                List<Fornecedor> lista = fornecedorController.listarTodos();
                for (Fornecedor f : lista) {
                    modeloTabela.addRow(new Object[]{
                        f.getId(), 
                        f.getNomeFantasia(), 
                        f.getRazaoSocial(), 
                        f.getCnpj()
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar: " + ex.getMessage());
            }
        });

        // Executa a primeira atualização visual da tabela ao abrir o formulário
        btnLimpar.doClick();
    } // <-- Fim do construtor e encerramento limpo da classe
}