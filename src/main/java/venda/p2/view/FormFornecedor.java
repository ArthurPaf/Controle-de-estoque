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

    private JTextField txtNomeFantasia, txtRazaoSocial, txtCnpj, txtPesquisa;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar, btnPesquisar;
    private JTable tabelaFornecedores;
    private DefaultTableModel modeloTabela;

    private FornecedorController fornecedorController;
    private Fornecedor fornecedorSelecionado;

    public FormFornecedor() {
        fornecedorController = new FornecedorController();

        setTitle("Gerenciar Fornecedores (CRUD)");
        setSize(650, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PAINEL PRINCIPAL ---
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        // --- 1. FORMULÁRIO DE CADASTRO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Fornecedor"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNomeFantasia = new JTextField(20);
        txtRazaoSocial = new JTextField(20);
        txtCnpj = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome Fantasia:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNomeFantasia, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Razão Social:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtRazaoSocial, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("CNPJ:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtCnpj, gbc);

        // --- 2. PAINEL DE BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
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
        painelCampos.add(painelAcoes, gbc);

        painelTopo.add(painelCampos);

        // --- PAINEL DE PESQUISA ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Pesquisar Fornecedores"));
        txtPesquisa = new JTextField(25);
        btnPesquisar = new JButton("Pesquisar");

        painelBusca.add(new JLabel("Nome Fantasia:"));
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);

        painelTopo.add(painelBusca);

        add(painelTopo, BorderLayout.NORTH);

        // --- 3. TABELA DE FORNECEDORES ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome Fantasia", "Razão Social", "CNPJ"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaFornecedores = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFornecedores);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Fornecedores Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. EVENTOS ---

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
                        JOptionPane.showMessageDialog(FormFornecedor.this, "Erro ao carregar fornecedor: " + ex.getMessage());
                    }
                }
            }
        });

        // Botão Salvar
        btnSalvar.addActionListener(e -> {
            try {
                fornecedorController.salvarFornecedor(
                    txtNomeFantasia.getText(),
                    txtRazaoSocial.getText(),
                    txtCnpj.getText()
                );
                JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        // Botão Editar
        btnEditar.addActionListener(e -> {
            try {
                fornecedorController.atualizarFornecedor(
                    fornecedorSelecionado,
                    txtNomeFantasia.getText(),
                    txtRazaoSocial.getText(),
                    txtCnpj.getText()
                );
                JOptionPane.showMessageDialog(this, "Fornecedor atualizado com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        });

        // Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (fornecedorSelecionado != null) {
                int conf = JOptionPane.showConfirmDialog(this, "Deseja excluir o fornecedor '" + fornecedorSelecionado.getNomeFantasia() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        fornecedorController.excluirFornecedor(fornecedorSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Fornecedor removido com sucesso!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Pesquisar
        btnPesquisar.addActionListener(e -> {
            String busca = txtPesquisa.getText().trim();
            try {
                List<Fornecedor> resultado = fornecedorController.pesquisarPorNome(busca);
                atualizarTabela(resultado);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage());
            }
        });

        // Botão Limpar
        btnLimpar.addActionListener(e -> {
            txtNomeFantasia.setText("");
            txtRazaoSocial.setText("");
            txtCnpj.setText("");
            txtPesquisa.setText("");
            
            fornecedorSelecionado = null;
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            try {
                List<Fornecedor> completa = fornecedorController.listarTodos();
                atualizarTabela(completa);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar: " + ex.getMessage());
            }
        });

        btnLimpar.doClick();
    }


    private void atualizarTabela(List<Fornecedor> lista) {
        modeloTabela.setRowCount(0);
        for (Fornecedor f : lista) {
            modeloTabela.addRow(new Object[]{
                f.getId(),
                f.getNomeFantasia(),
                f.getRazaoSocial(),
                f.getCnpj()
            });
        }
    }
}