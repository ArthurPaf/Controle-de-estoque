package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import venda.p2.controller.FinanceiroController;
import venda.p2.model.Financeiro;
import venda.p2.model.FormaPagamento;
import venda.p2.model.TipoConta;

public class FormFinanceiro extends JFrame {

    private JComboBox<String> cbMovimentacao;
    private JComboBox<TipoConta> cbTipoConta;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JTextField txtValor;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaFinanceiro;
    private DefaultTableModel modeloTabela;

    // View conversa estritamente com o Controller
    private FinanceiroController financeiroController;
    private Financeiro lancamentoSelecionado;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FormFinanceiro() {
        financeiroController = new FinanceiroController();

        setTitle("Lançamentos Financeiros (Contas Pagar/Receber)");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Lançamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbMovimentacao = new JComboBox<>(new String[]{"Contas a Pagar (Saída)", "Contas a Receber (Entrada)"});
        cbTipoConta = new JComboBox<>();
        cbFormaPagamento = new JComboBox<>();
        txtValor = new JTextField(10);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Tipo de Movimentação:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(cbMovimentacao, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Categoria da Conta:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(cbTipoConta, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("Forma de Pagamento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(cbFormaPagamento, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Valor Total (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(txtValor, gbc);

        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Lançar Conta");
        btnEditar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir Lançamento");
        btnLimpar = new JButton("Limpar");

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 6, 6, 6);
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data", "Fluxo", "Categoria", "Forma Pgto", "Valor Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFinanceiro = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFinanceiro);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Livro Caixa / Histórico de Contas"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- CARGA INICIAL DOS COMBOBOXES ---
        try {
            List<TipoConta> tipos = financeiroController.listarTiposConta();
            for (TipoConta tc : tipos) cbTipoConta.addItem(tc);

            List<FormaPagamento> formas = financeiroController.listarFormasPagamento();
            for (FormaPagamento fp : formas) cbFormaPagamento.addItem(fp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações financeiras: " + e.getMessage());
        }

        // --- EVENTOS E LISTENERS (A classe encerra exatamente aqui) ---

        // Clique na JTable para recuperar lançamento do banco
        tabelaFinanceiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaFinanceiro.getSelectedRow();
                if (linha >= 0) {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    try {
                        lancamentoSelecionado = financeiroController.buscarPorId(id);
                        if (lancamentoSelecionado != null) {
                            cbMovimentacao.setSelectedIndex(lancamentoSelecionado.getPagar_ou_receber() == 1 ? 0 : 1);
                            cbTipoConta.setSelectedItem(lancamentoSelecionado.getTipoConta());
                            cbFormaPagamento.setSelectedItem(lancamentoSelecionado.getFormaPagamento());
                            txtValor.setText(String.valueOf(lancamentoSelecionado.getValor_total()));

                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormFinanceiro.this, "Erro ao selecionar item: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Salvar (Lançar Conta)
        btnSalvar.addActionListener(e -> {
            try {
                int tipoMov = cbMovimentacao.getSelectedIndex();
                TipoConta tc = (TipoConta) cbTipoConta.getSelectedItem();
                FormaPagamento fp = (FormaPagamento) cbFormaPagamento.getSelectedItem();
                String valorStr = txtValor.getText();

                financeiroController.gerenciarNovoLancamento(tipoMov, tc, fp, valorStr);

                JOptionPane.showMessageDialog(this, "Conta lançada e parcelas geradas com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar lançamento: " + ex.getMessage());
            }
        });

        // Ação do Botão Editar (Atualizar)
        btnEditar.addActionListener(e -> {
            if (lancamentoSelecionado != null && !txtValor.getText().trim().isEmpty()) {
                try {
                    lancamentoSelecionado.setPagar_ou_receber(cbMovimentacao.getSelectedIndex() == 0 ? 1 : 2);
                    lancamentoSelecionado.setTipoConta((TipoConta) cbTipoConta.getSelectedItem());
                    lancamentoSelecionado.setFormaPagamento((FormaPagamento) cbFormaPagamento.getSelectedItem());
                    lancamentoSelecionado.setValor_total(Double.parseDouble(txtValor.getText().trim()));

                    financeiroController.atualizarLancamento(lancamentoSelecionado);
                    JOptionPane.showMessageDialog(this, "Lançamento atualizado!");
                    btnLimpar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
                }
            }
        });

        // Ação do Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (lancamentoSelecionado != null) {
                int conf = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja apagar esse registro financeiro?", 
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        financeiroController.excluirLancamento(lancamentoSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Registro financeiro removido!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro: Este registro possui parcelas vinculadas e não pode ser apagado diretamente.");
                    }
                }
            }
        });

        // Ação do Botão Limpar / Atualizar Tabela Visualmente
        btnLimpar.addActionListener(e -> {
            txtValor.setText("");
            cbMovimentacao.setSelectedIndex(0);
            if (cbTipoConta.getItemCount() > 0) cbTipoConta.setSelectedIndex(0);
            if (cbFormaPagamento.getItemCount() > 0) cbFormaPagamento.setSelectedIndex(0);
            lancamentoSelecionado = null;
            
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            modeloTabela.setRowCount(0);
            try {
                List<Financeiro> lista = financeiroController.listarLancamentos();
                for (Financeiro f : lista) {
                    String fluxo = (f.getPagar_ou_receber() == 1) ? "🔴 APAGAR" : "🟢 ARECEBER";
                    String cat = f.getTipoConta() != null ? f.getTipoConta().getDescricao() : "Não informada";
                    String fPgto = f.getFormaPagamento() != null ? f.getFormaPagamento().getNome() : "Não informada";
                    String dataFormatada = f.getData_conta() != null ? sdf.format(f.getData_conta()) : "";

                    modeloTabela.addRow(new Object[]{
                        f.getId(),
                        dataFormatada,
                        fluxo,
                        cat,
                        fPgto,
                        String.format("R$ %.2f", f.getValor_total())
                    });
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar lançamentos: " + ex.getMessage());
            }
        });

        // Primeira carga na JTable e reset de botões ao instanciar a tela
        btnLimpar.doClick();
    }
}