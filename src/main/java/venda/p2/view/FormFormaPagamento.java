package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.FormaPagamentoController;
import venda.p2.model.FormaPagamento;

public class FormFormaPagamento extends JFrame {

    private JTextField txtNome, txtParcelas, txtPrazo;
    private JComboBox<String> cbTipo;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaFormas;
    private DefaultTableModel modeloTabela;

    // View agora conversa exclusivamente com o Controller específico
    private FormaPagamentoController formaController;
    private FormaPagamento formaSelecionada;

    public FormFormaPagamento() {
        formaController = new FormaPagamentoController();

        setTitle("Gerenciar Formas de Pagamento (CRUD)");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- FORMULÁRIO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados da Forma de Pagamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);
        txtParcelas = new JTextField("1", 5);
        txtPrazo = new JTextField("0", 5);
        cbTipo = new JComboBox<>(new String[]{"À Vista", "A Prazo"});

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome/Descrição:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Qtd. Parcelas:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtParcelas, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("Prazo entre Parc. (Dias):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtPrazo, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(cbTipo, gbc);

        // --- BOTÕES ---
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

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Parcelas", "Prazo", "Tipo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFormas = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFormas);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Formas Cadastradas"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- EVENTOS E LISTENERS ---

        // Evento de clique na JTable para recuperar a forma selecionada
        tabelaFormas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaFormas.getSelectedRow();
                if (linha >= 0) {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    try {
                        formaSelecionada = formaController.buscarPorId(id);
                        if (formaSelecionada != null) {
                            txtNome.setText(formaSelecionada.getNome());
                            txtParcelas.setText(String.valueOf(formaSelecionada.getQtde_parcela()));
                            txtPrazo.setText(String.valueOf(formaSelecionada.getPrazo()));
                            cbTipo.setSelectedIndex(formaSelecionada.getAvista_aprazo() == 1 ? 0 : 1);

                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormFormaPagamento.this, "Erro ao carregar: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> {
            try {
                formaController.salvarForma(
                    txtNome.getText(),
                    txtParcelas.getText(),
                    txtPrazo.getText(),
                    cbTipo.getSelectedIndex()
                );
                JOptionPane.showMessageDialog(this, "Forma de pagamento salva!");
                btnLimpar.doClick();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade de parcelas ou prazo inválidos.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        // Ação do Botão Editar (Atualizar)
        btnEditar.addActionListener(e -> {
            if (formaSelecionada != null) {
                try {
                    formaController.atualizarForma(
                        formaSelecionada,
                        txtNome.getText(),
                        txtParcelas.getText(),
                        txtPrazo.getText(),
                        cbTipo.getSelectedIndex()
                    );
                    JOptionPane.showMessageDialog(this, "Forma de pagamento actualizada!");
                    btnLimpar.doClick();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Quantidade de parcelas ou prazo inválidos.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
                }
            }
        });

        // Ação do Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (formaSelecionada != null) {
                int conf = JOptionPane.showConfirmDialog(this, 
                    "Excluir " + formaSelecionada.getNome() + "?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        formaController.excluirForma(formaSelecionada.getId());
                        JOptionPane.showMessageDialog(this, "Excluído!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro: Esta forma de pagamento pode estar vinculada a lançamentos financeiros.");
                    }
                }
            }
        });

        // Ação do Botão Limpar / Atualizar Tabela Visualmente
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtParcelas.setText("1");
            txtPrazo.setText("0");
            cbTipo.setSelectedIndex(0);
            formaSelecionada = null;
            
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            modeloTabela.setRowCount(0);
            try {
                List<FormaPagamento> lista = formaController.listarTodas();
                for (FormaPagamento f : lista) {
                    String tipoStr = (f.getAvista_aprazo() == 1) ? "À Vista" : "A Prazo";
                    modeloTabela.addRow(new Object[]{
                        f.getId(), 
                        f.getNome(), 
                        f.getQtde_parcela(), 
                        f.getPrazo(), 
                        tipoStr
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar: " + ex.getMessage());
            }
        });

        // Dispara a carga inicial do JTable ao abrir o formulário
        btnLimpar.doClick();
    } // <-- Fim do construtor e fechamento absoluto da estrutura da classe!
}