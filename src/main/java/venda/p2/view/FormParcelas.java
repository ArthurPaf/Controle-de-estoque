package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;

public class FormParcelas extends JFrame {

    private JComboBox<String> cbContas;
    private List<Financeiro> listaContas;
    private JTable tabelaParcelas;
    private DefaultTableModel modeloTabela;
    private JTextField txtDesconto, txtAcrescimo, txtValorFinal;
    private JButton btnBaixa;

    private GenericDAO<Financeiro> financeiroDAO;
    private GenericDAO<FinanceiroParcela> parcelaDAO;
    private FinanceiroParcela parcelaSelecionada;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FormParcelas() {
        financeiroDAO = new GenericDAO<>(Financeiro.class);
        parcelaDAO = new GenericDAO<>(FinanceiroParcela.class);

        setTitle("Gerenciar e Pagar Parcelas");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- TOPO: SELEÇÃO DA CONTA MESTRE ---
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelTopo.setBorder(BorderFactory.createTitledBorder("Selecionar Lançamento Financeiro"));
        cbContas = new JComboBox<>();
        atualizarComboContas();
        
        cbContas.addActionListener(e -> buscarParcelasDaConta());
        painelTopo.add(new JLabel("Conta / Registro:"));
        painelTopo.add(cbContas);
        add(painelTopo, BorderLayout.NORTH);

        // --- CENTRO: TABELA DE PARCELAS ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nº Parcela", "Vencimento", "Valor Original", "Status", "Data Pagamento", "Valor Final"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaParcelas = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaParcelas);
        scroll.setBorder(BorderFactory.createTitledBorder("Parcelas Deste Lançamento"));
        add(scroll, BorderLayout.CENTER);

        // --- INFERIOR: FORMULÁRIO DE BAIXA ---
        JPanel painelInferior = new JPanel(new GridBagLayout());
        painelInferior.setBorder(BorderFactory.createTitledBorder("Ações de Pagamento / Recebimento"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        txtDesconto = new JTextField("0.00", 8);
        txtAcrescimo = new JTextField("0.00", 8);
        txtValorFinal = new JTextField(10);
        txtValorFinal.setEditable(false);
        btnBaixa = new JButton("Confirmar Pagamento / Baixa ✔");
        btnBaixa.setEnabled(false);

        gbc.gridx = 0; gbc.gridy = 0; painelInferior.add(new JLabel("Desconto (-):"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelInferior.add(txtDesconto, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelInferior.add(new JLabel("Acréscimo (+):"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelInferior.add(txtAcrescimo, gbc);
        gbc.gridx = 4; gbc.gridy = 0; painelInferior.add(new JLabel("Valor Final:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; painelInferior.add(txtValorFinal, gbc);
        gbc.gridx = 6; gbc.gridy = 0; painelInferior.add(btnBaixa, gbc);

        add(painelInferior, BorderLayout.SOUTH);

        // --- EVENTOS ---
        tabelaParcelas.getSelectionModel().addListSelectionListener(e -> selecionarParcela());
        btnBaixa.addActionListener(e -> efetuarBaixa());
    }

    private void atualizarComboContas() {
        try {
            cbContas.removeAllItems();
            listaContas = financeiroDAO.listarTodos();
            for (Financeiro f : listaContas) {
                String fluxo = (f.getPagar_ou_receber() == 1) ? "[PAGAR]" : "[RECEBER]";
                cbContas.addItem(f.getId() + " - " + fluxo + " " + (f.getTipoConta() != null ? f.getTipoConta().getDescricao() : "") + " - R$ " + f.getValor_total());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarParcelasDaConta() {
    modeloTabela.setRowCount(0); // Limpa a tabela gráfica
    int index = cbContas.getSelectedIndex();
    
    if (index >= 0) {
        Financeiro fSelecionado = listaContas.get(index);
        try {
            // Busca todas as parcelas cadastradas no banco
            List<FinanceiroParcela> todas = parcelaDAO.listarTodos();
            
            for (FinanceiroParcela p : todas) {
                // CORREÇÃO AQUI: Garante que estamos comparando os IDs corretamente como int
                if (p.getFinanceiro() != null && p.getFinanceiro().getId() == fSelecionado.getId()) {
                    
                    String statusStr = (p.getStatus() == 1) ? "🔴 Aberta" : "🟢 Paga";
                    String dtPgto = (p.getData_pagamento() != null) ? sdf.format(p.getData_pagamento()) : "-";
                    
                    modeloTabela.addRow(new Object[]{
                        p.getId(), 
                        p.getN_parcela(), 
                        sdf.format(p.getData_vencimento()),
                        p.getValor_original(), 
                        statusStr, 
                        dtPgto, 
                        p.getValor_final()
                    });
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar parcelas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

    private void selecionarParcela() {
        int linha = tabelaParcelas.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modeloTabela.getValueAt(linha, 0);
            try {
                parcelaSelecionada = parcelaDAO.buscarPorId(id);
                if (parcelaSelecionada != null && parcelaSelecionada.getStatus() == 1) { // Só permite baixa se estiver aberta
                    txtDesconto.setText("0.00");
                    txtAcrescimo.setText("0.00");
                    txtValorFinal.setText(String.valueOf(parcelaSelecionada.getValor_original()));
                    btnBaixa.setEnabled(true);
                } else {
                    btnBaixa.setEnabled(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void efetuarBaixa() {
        if (parcelaSelecionada != null) {
            try {
                double desc = Double.parseDouble(txtDesconto.getText().trim());
                double acr = Double.parseDouble(txtAcrescimo.getText().trim());
                double valorFinal = parcelaSelecionada.getValor_original() - desc + acr;

                parcelaSelecionada.setDesconto(desc);
                parcelaSelecionada.setAcrescimo(acr);
                parcelaSelecionada.setValor_final(valorFinal);
                parcelaSelecionada.setData_pagamento(new Date());
                parcelaSelecionada.setStatus(2); // Muda para Pago

                parcelaDAO.salvar(parcelaSelecionada);
                JOptionPane.showMessageDialog(this, "Baixa efetuada com sucesso!");
                btnBaixa.setEnabled(false);
                buscarParcelasDaConta(); // Atualiza a tabela gráfica
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao processar baixa: " + e.getMessage());
            }
        }
    }
}