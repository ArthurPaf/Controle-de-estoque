package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import venda.p2.controller.FinanceiroParcelaController;
import venda.p2.model.Financeiro;
import venda.p2.model.FinanceiroParcela;

public class FormParcelas extends JFrame {

    private JComboBox<String> cbContas;
    private List<Financeiro> listaContas;
    private JTable tabelaParcelas;
    private DefaultTableModel modeloTabela;
    private JTextField txtDesconto, txtAcrescimo, txtValorFinal;
    private JButton btnBaixa;

    private FinanceiroParcelaController parcelaController;
    private FinanceiroParcela parcelaSelecionada;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FormParcelas() {
        parcelaController = new FinanceiroParcelaController();

        setTitle("Gerenciar e Pagar Parcelas");
        setSize(850, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // SELEÇÃO DA CONTA
        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelTopo.setBorder(BorderFactory.createTitledBorder("Selecionar Lançamento Financeiro"));
        cbContas = new JComboBox<>();
        
        painelTopo.add(new JLabel("Conta / Registro:"));
        painelTopo.add(cbContas);
        add(painelTopo, BorderLayout.NORTH);

        // TABELA DE PARCELAS
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nº Parcela", "Vencimento", "Valor Original", "Status", "Data Pagamento", "Valor Final"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaParcelas = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabelaParcelas);
        scroll.setBorder(BorderFactory.createTitledBorder("Parcelas Deste Lançamento"));
        add(scroll, BorderLayout.CENTER);

        // FORMULÁRIO DE BAIXA
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

        // --- EVENTOS E LISTENERS ---

        cbContas.addActionListener(e -> {
            modeloTabela.setRowCount(0);
            int index = cbContas.getSelectedIndex();
            if (index >= 0 && listaContas != null) {
                Financeiro fSelecionadoOld = listaContas.get(index);
                try {
                    Financeiro fSelecionado = parcelaController.buscarContaPorId(fSelecionadoOld.getId());

                    List<FinanceiroParcela> parcelas = parcelaController.listarParcelasPorConta(fSelecionado.getId());
                    for (FinanceiroParcela p : parcelas) {
                    
                        String statusStr = (p.getStatus() == 1) ? "🔴 Aberta" : "🟢 Paga";
                        String dtPgto = (p.getData_pagamento() != null) ? sdf.format(p.getData_pagamento()) : "-";
                        
                        modeloTabela.addRow(new Object[]{
                            p.getId(), 
                            p.getN_parcela(), 
                            p.getData_vencimento() != null ? sdf.format(p.getData_vencimento()) : "-",
                            p.getValor_original(), 
                            statusStr, 
                            dtPgto, 
                            p.getValor_final()
                        });
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao carregar parcelas atualizadas: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });

        
        tabelaParcelas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaParcelas.getSelectedRow();
                if (linha >= 0) {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    try {
                        parcelaSelecionada = parcelaController.buscarParcelaPorId(id);
                        if (parcelaSelecionada != null && parcelaSelecionada.getStatus() == 1) {
                            txtDesconto.setText("0.00");
                            txtAcrescimo.setText("0.00");
                            txtValorFinal.setText(String.valueOf(parcelaSelecionada.getValor_original()));
                            btnBaixa.setEnabled(true);
                        } else {
                            btnBaixa.setEnabled(false);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormParcelas.this, "Erro ao carregar parcela: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Confirmar Baixa
        btnBaixa.addActionListener(e -> {
            if (parcelaSelecionada != null) {
                try {
                    parcelaController.efetuarBaixa(
                        parcelaSelecionada, 
                        txtDesconto.getText(), 
                        txtAcrescimo.getText()
                    );
                    JOptionPane.showMessageDialog(this, "Baixa efetuada com sucesso!");
                    btnBaixa.setEnabled(false);
                    
                    txtDesconto.setText("0.00");
                    txtAcrescimo.setText("0.00");
                    txtValorFinal.setText("");
                    
                    
                    int indexAtual = cbContas.getSelectedIndex();
                    cbContas.setSelectedIndex(-1);
                    cbContas.setSelectedIndex(indexAtual);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Insira valores monetários válidos para desconto/acréscimo.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao processar baixa: " + ex.getMessage());
                }
            }
        });

        try {
            cbContas.removeAllItems();
            listaContas = parcelaController.listarTodasContas();
            for (Financeiro f : listaContas) {
                String fluxo = (f.getPagar_ou_receber() == 1) ? "[PAGAR]" : "[RECEBER]";
                String descricaoTipo = (f.getTipoConta() != null) ? f.getTipoConta().getDescricao() : "";
                cbContas.addItem(f.getId() + " - " + fluxo + " " + descricaoTipo + " - R$ " + f.getValor_total());
            }
            cbContas.setSelectedIndex(-1); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao inicializar lançamentos: " + e.getMessage());
        }
    } 
}