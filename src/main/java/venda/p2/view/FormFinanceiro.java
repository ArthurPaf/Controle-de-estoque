package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;
import venda.p2.model.FormaPagamento;
import venda.p2.model.TipoConta;

public class FormFinanceiro extends JFrame {

    private JComboBox<String> cbMovimentacao; // Pagar ou Receber
    private JComboBox<TipoConta> cbTipoConta;
    private JComboBox<FormaPagamento> cbFormaPagamento;
    private JTextField txtValor;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaFinanceiro;
    private DefaultTableModel modeloTabela;

    private GenericDAO<Financeiro> financeiroDAO;
    private GenericDAO<TipoConta> tipoContaDAO;
    private GenericDAO<FormaPagamento> formaPagamentoDAO;
    
    private Financeiro lancamentoSelecionado;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FormFinanceiro() {
        financeiroDAO = new GenericDAO<>(Financeiro.class);
        tipoContaDAO = new GenericDAO<>(TipoConta.class);
        formaPagamentoDAO = new GenericDAO<>(FormaPagamento.class);
        

        setTitle("Lançamentos Financeiros (Contas Pagar/Receber)");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. FORMULÁRIO DE LANÇAMENTO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Lançamento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbMovimentacao = new JComboBox<>(new String[]{"Contas a Pagar (Saída)", "Contas a Receber (Entrada)"});
        cbTipoConta = new JComboBox<>();
        cbFormaPagamento = new JComboBox<>();
        txtValor = new JTextField(10);

        carregarCombos();

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Tipo de Movimentação:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(cbMovimentacao, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Categoria da Conta:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(cbTipoConta, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("Forma de Pagamento:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(cbFormaPagamento, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Valor Total (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(txtValor, gbc);

        // --- 2. BOTÕES DE AÇÃO ---
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

        // --- 3. TABELA DE REGISTROS ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Data", "Fluxo", "Categoria", "Forma Pgto", "Valor Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaFinanceiro = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaFinanceiro);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Livro Caixa / Histórico de Contas"));
        add(scrollTabela, BorderLayout.CENTER);

        atualizarTabela();

        // --- 4. EVENTOS ---
        tabelaFinanceiro.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { preencherCamposPelaTabela(); }
        });

        btnSalvar.addActionListener(e -> salvarLancamento());
        btnEditar.addActionListener(e -> editarLancamento());
        btnExcluir.addActionListener(e -> excluirLancamento());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void carregarCombos() {
        try {
            List<TipoConta> tipos = tipoContaDAO.listarTodos();
            for (TipoConta tc : tipos) cbTipoConta.addItem(tc);

            List<FormaPagamento> formas = formaPagamentoDAO.listarTodos();
            for (FormaPagamento fp : formas) cbFormaPagamento.addItem(fp);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar configurações financeiras: " + e.getMessage());
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Financeiro> lista = financeiroDAO.listarTodos();
            for (Financeiro f : lista) {
                String fluxo = (f.getPagar_ou_receber() == 1) ? "🔴 APAGAR" : "🟢 ARECEBER";
                String cat = f.getTipoConta() != null ? f.getTipoConta().getDescricao() : "Não informada";
                String fPgto = f.getFormaPagamento() != null ? f.getFormaPagamento().getNome() : "Não informada";
                
                modeloTabela.addRow(new Object[]{
                    f.getId(),
                    sdf.format(f.getData_conta()),
                    fluxo,
                    cat,
                    fPgto,
                    String.format("R$ %.2f", f.getValor_total())
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar lançamentos: " + e.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linha = tabelaFinanceiro.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modeloTabela.getValueAt(linha, 0);
            try {
                lancamentoSelecionado = financeiroDAO.buscarPorId(id);
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
                JOptionPane.showMessageDialog(this, "Erro ao selecionar item: " + ex.getMessage());
            }
        }
    }

    private void salvarLancamento() {
    if (txtValor.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Defina o valor do lançamento.");
        return;
    }
    try {
        Financeiro f = new Financeiro();
        f.setPagar_ou_receber(cbMovimentacao.getSelectedIndex() == 0 ? 1 : 2);
        f.setTipoConta((TipoConta) cbTipoConta.getSelectedItem());
        
        FormaPagamento fp = (FormaPagamento) cbFormaPagamento.getSelectedItem();
        f.setFormaPagamento(fp);
        
        double valorTotal = Double.parseDouble(txtValor.getText().trim());
        f.setValor_total(valorTotal);

        // 1. Salva a conta pai no banco
        financeiroDAO.salvar(f);

        // 2. Cria o GenericDAO de parcelas aqui mesmo, na hora de usar!
        GenericDAO<FinanceiroParcela> localParcelaDAO = new GenericDAO<>(FinanceiroParcela.class);

        // 3. Gerar as parcelas automaticamente com base na Forma de Pagamento
        int qtdParcelas = fp.getQtde_parcela() > 0 ? fp.getQtde_parcela() : 1;
        double valorPorParcela = valorTotal / qtdParcelas;
        long prazoEmMilissegundos = fp.getPrazo() * 24L * 60L * 60L * 1000L;

        java.util.Date dataVencimentoAtual = new java.util.Date();

        for (int i = 1; i <= qtdParcelas; i++) {
            FinanceiroParcela parcela = new FinanceiroParcela();
            parcela.setFinanceiro(f);
            parcela.setN_parcela(i);
            parcela.setValor_original(valorPorParcela);
            parcela.setValor_final(valorPorParcela);
            parcela.setStatus(1); // 1 = Em aberto
            
            if (i > 1) {
                dataVencimentoAtual = new java.util.Date(dataVencimentoAtual.getTime() + prazoEmMilissegundos);
            }
            parcela.setData_vencimento(dataVencimentoAtual);

            // Usa o DAO local que acabamos de instanciar
            localParcelaDAO.salvar(parcela); 
        }

        JOptionPane.showMessageDialog(this, "Conta lançada e " + qtdParcelas + " parcela(s) gerada(s)!");
        limparCampos();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao salvar lançamento: " + ex.getMessage());
    }
}

    private void editarLancamento() {
        if (lancamentoSelecionado != null && !txtValor.getText().trim().isEmpty()) {
            try {
                lancamentoSelecionado.setPagar_ou_receber(cbMovimentacao.getSelectedIndex() == 0 ? 1 : 2);
                lancamentoSelecionado.setTipoConta((TipoConta) cbTipoConta.getSelectedItem());
                lancamentoSelecionado.setFormaPagamento((FormaPagamento) cbFormaPagamento.getSelectedItem());
                lancamentoSelecionado.setValor_total(Double.parseDouble(txtValor.getText().trim()));

                financeiroDAO.salvar(lancamentoSelecionado);
                JOptionPane.showMessageDialog(this, "Lançamento atualizado!");
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        }
    }

    private void excluirLancamento() {
        if (lancamentoSelecionado != null) {
            int conf = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja apagar esse registro financeiro?", "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    financeiroDAO.excluir(lancamentoSelecionado.getId());
                    JOptionPane.showMessageDialog(this, "Registro financeiro removido!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: Este registro possui parcelas vinculadas e não pode ser apagado diretamente.");
                }
            }
        }
    }

    private void limparCampos() {
        txtValor.setText("");
        cbMovimentacao.setSelectedIndex(0);
        if (cbTipoConta.getItemCount() > 0) cbTipoConta.setSelectedIndex(0);
        if (cbFormaPagamento.getItemCount() > 0) cbFormaPagamento.setSelectedIndex(0);
        lancamentoSelecionado = null;
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        atualizarTabela();
    }
}