package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import venda.p2.controller.VendaController;
import venda.p2.model.Cliente;
import venda.p2.model.Produto;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class FormVenda extends JFrame {

    // Componentes da Aba 1 (Registrar Venda)
    private JComboBox<Cliente> cbCliente;
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade, txtPrecoUn, txtTotalGeral;
    private JButton btnAdicionar, btnRemoverItem, btnFinalizar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    // Componentes da Aba 2 (Histórico de Vendas)
    private JTable tabelaHistoricoVendas;
    private DefaultTableModel modeloHistoricoVendas;
    private JTable tabelaDetalhesItens;
    private DefaultTableModel modeloDetalhesItens;

    // Controle
    private VendaController vendaController;
    private List<VendaProduto> listaItensCarrinho;
    private double totalVenda = 0.0;

    public FormVenda() {
        vendaController = new VendaController();
        listaItensCarrinho = new ArrayList<>();

        setTitle("Módulo de Vendas (PDV & Histórico)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // O segredo está aqui: o container principal agora é um JTabbedPane (Abas)
        JTabbedPane abas = new JTabbedPane();

        // =========================================================================
        // CONSTRUÇÃO DA ABA 1: REGISTRAR VENDA (Seu código original encapsulado)
        // =========================================================================
        JPanel painelNovaVenda = new JPanel(new BorderLayout());

        // --- 1. Cabeçalho ---
        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações Básicas"));
        cbCliente = new JComboBox<>();
        painelCabecalho.add(new JLabel("Cliente:"));
        painelCabecalho.add(cbCliente);
        painelCabecalho.add(new JLabel("Data: " + LocalDate.now()));

        // --- 2. Painel Itens ---
        JPanel painelProdutos = new JPanel(new GridBagLayout());
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Adicionar Itens à Venda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbProduto = new JComboBox<>();
        txtQuantidade = new JTextField("1", 5);
        txtPrecoUn = new JTextField(8);
        txtPrecoUn.setEditable(false); 
        btnAdicionar = new JButton("Adicionar Item ➕");

        gbc.gridx = 0; gbc.gridy = 0; painelProdutos.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelProdutos.add(cbProduto, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelProdutos.add(new JLabel("Preço Un:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelProdutos.add(txtPrecoUn, gbc);
        gbc.gridx = 4; gbc.gridy = 0; painelProdutos.add(new JLabel("Qtd:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; painelProdutos.add(txtQuantidade, gbc);
        gbc.gridx = 6; gbc.gridy = 0; painelProdutos.add(btnAdicionar, gbc);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCabecalho, BorderLayout.NORTH);
        painelSuperior.add(painelProdutos, BorderLayout.CENTER);
        painelNovaVenda.add(painelSuperior, BorderLayout.NORTH);

        // --- 3. Tabela do Carrinho ---
        modeloTabela = new DefaultTableModel(new Object[]{"Item", "Produto", "Preço Un.", "Qtd", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Itens da Venda Atual"));
        painelNovaVenda.add(scrollTabela, BorderLayout.CENTER);

        // --- 4. Painel Inferior ---
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTotalGeral = new JTextField("R$ 0.00", 12);
        txtTotalGeral.setFont(new Font("Arial", Font.BOLD, 18));
        txtTotalGeral.setEditable(false);
        txtTotalGeral.setHorizontalAlignment(JTextField.RIGHT);
        painelTotal.add(new JLabel("TOTAL DA VENDA:"));
        painelTotal.add(txtTotalGeral);

        JPanel painelBotoesFinais = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRemoverItem = new JButton("Remover Item Selecionado ❌");
        btnFinalizar = new JButton("Finalizar Venda 💾");
        btnFinalizar.setBackground(new Color(46, 204, 113));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        
        painelBotoesFinais.add(btnRemoverItem);
        painelBotoesFinais.add(btnFinalizar);

        painelInferior.add(painelTotal, BorderLayout.NORTH);
        painelInferior.add(painelBotoesFinais, BorderLayout.SOUTH);
        painelNovaVenda.add(painelInferior, BorderLayout.SOUTH);


        // =========================================================================
        // CONSTRUÇÃO DA ABA 2: HISTÓRICO DE VENDAS (Mestre-Detalhe)
        // =========================================================================
        JPanel painelHistorico = new JPanel(new GridLayout(2, 1, 0, 10)); // Divide a tela ao meio (vendas em cima, itens embaixo)

        // Tabela de cima (Vendas Pai)
        modeloHistoricoVendas = new DefaultTableModel(new Object[]{"ID Venda", "Cliente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaHistoricoVendas = new JTable(modeloHistoricoVendas);
        JScrollPane scrollHistorico = new JScrollPane(tabelaHistoricoVendas);
        scrollHistorico.setBorder(BorderFactory.createTitledBorder("Vendas Realizadas (Selecione uma para ver os itens)"));
        painelHistorico.add(scrollHistorico);

        // Tabela de baixo (Itens da Venda selecionada)
        modeloDetalhesItens = new DefaultTableModel(new Object[]{"Produto", "Preço Unitário", "Qtd Vendida"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaDetalhesItens = new JTable(modeloDetalhesItens);
        JScrollPane scrollDetalhes = new JScrollPane(tabelaDetalhesItens);
        scrollDetalhes.setBorder(BorderFactory.createTitledBorder("Produtos Pertencentes à Venda Selecionada"));
        painelHistorico.add(scrollDetalhes);


        // Adiciona os painéis estruturados como abas independentes
        abas.addTab("🛒 Registrar Venda", painelNovaVenda);
        abas.addTab("📋 Histórico de Vendas", painelHistorico);
        add(abas, BorderLayout.CENTER);

        // =========================================================================
        // EVENTOS E LISTENERS
        // =========================================================================
        
        // Monitora quando o usuário clica na aba de histórico para atualizar as vendas automaticamente
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 1) { // Aba do histórico selecionada
                carregarHistoricoVendasDoBanco();
            }
        });

        // Evento de clique na tabela de histórico para carregar os itens embaixo
        tabelaHistoricoVendas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linhaSelecionada = tabelaHistoricoVendas.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    int idVenda = (int) modeloHistoricoVendas.getValueAt(linhaSelecionada, 0);
                    carregarItensDaVendaSelecionada(idVenda);
                }
            }
        });

        cbProduto.addActionListener(e -> {
            Produto p = (Produto) cbProduto.getSelectedItem();
            txtPrecoUn.setText(p != null ? String.valueOf(p.getPreco()) : "");
        });

        btnAdicionar.addActionListener(e -> {
            try {
                Produto prodSelecionado = (Produto) cbProduto.getSelectedItem();
                VendaProduto item = vendaController.criarItemCarrinho(prodSelecionado, txtQuantidade.getText());
                listaItensCarrinho.add(item);

                double subtotal = item.getValorUnitario() * item.getQuantidade();
                modeloTabela.addRow(new Object[]{
                    listaItensCarrinho.size(),
                    item.getProduto().getNome(),
                    item.getValorUnitario(),
                    item.getQuantidade(),
                    subtotal
                });

                totalVenda += subtotal;
                txtTotalGeral.setText(String.format("R$ %.2f", totalVenda));
                txtQuantidade.setText("1");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRemoverItem.addActionListener(e -> {
            int linha = tabelaItens.getSelectedRow();
            if (linha >= 0) {
                double subtotalItem = (double) modeloTabela.getValueAt(linha, 4);
                listaItensCarrinho.remove(linha);
                modeloTabela.removeRow(linha);
                totalVenda -= subtotalItem;
                txtTotalGeral.setText(String.format("R$ %.2f", totalVenda));
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    modeloTabela.setValueAt(i + 1, i, 0);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um item da tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnFinalizar.addActionListener(e -> {
            try {
                Cliente clieSelecionado = (Cliente) cbCliente.getSelectedItem();
                vendaController.finalizarVenda(clieSelecionado, listaItensCarrinho);
                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso no banco de dados!");
                limparEAtualizarComponentes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro crítico ao processar transação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        limparEAtualizarComponentes();
    }

    private void limparEAtualizarComponentes() {
        listaItensCarrinho.clear();
        modeloTabela.setRowCount(0);
        totalVenda = 0.0;
        txtTotalGeral.setText("R$ 0.00");
        txtQuantidade.setText("1");
        try {
            cbCliente.removeAllItems();
            for (Cliente c : vendaController.listarClientes()) cbCliente.addItem(c);
            cbProduto.removeAllItems();
            for (Produto p : vendaController.listarProdutos()) cbProduto.addItem(p);
            if (cbCliente.getItemCount() > 0) cbCliente.setSelectedIndex(0);
            if (cbProduto.getItemCount() > 0) cbProduto.setSelectedIndex(0);
            Produto p = (Produto) cbProduto.getSelectedItem();
            txtPrecoUn.setText(p != null ? String.valueOf(p.getPreco()) : "");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados: " + ex.getMessage());
        }
    }

    // --- MÉTODOS AUXILIARES DA ABA DE HISTÓRICO ---
    
    private void carregarHistoricoVendasDoBanco() {
        try {
            modeloHistoricoVendas.setRowCount(0);
            modeloDetalhesItens.setRowCount(0); // Limpa os detalhes da consulta anterior
            List<Venda> vendas = vendaController.listarHistoricoVendas();
            for (Venda v : vendas) {
                modeloHistoricoVendas.addRow(new Object[]{
                    v.getId(),
                    v.getCliente().getNome()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + ex.getMessage());
        }
    }

    private void carregarItensDaVendaSelecionada(int idVenda) {
        try {
            modeloDetalhesItens.setRowCount(0);
            List<VendaProduto> itens = vendaController.listarItensDaVenda(idVenda);
            for (VendaProduto item : itens) {
                modeloDetalhesItens.addRow(new Object[]{
                    item.getProduto().getNome(),
                    item.getValorUnitario(),
                    item.getQuantidade()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar itens da venda: " + ex.getMessage());
        }
    }
}