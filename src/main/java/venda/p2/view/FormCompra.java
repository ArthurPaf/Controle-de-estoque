package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import venda.p2.controller.CompraController;
import venda.p2.model.Produto;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Fornecedor; // Import do modelo de fornecedor adicionado

public class FormCompra extends JFrame {

    // Aba 1
    private JComboBox<Fornecedor> cbFornecedor; // Adicionado para corrigir o erro do Banco
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade, txtPrecoCusto, txtTotalGeral;
    private JButton btnAdicionar, btnRemoverItem, btnFinalizar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    // Aba 2
    private JTable tabelaHistoricoCompras;
    private DefaultTableModel modeloHistoricoCompras;
    private JTable tabelaDetalhesItens;
    private DefaultTableModel modeloDetalhesItens;

    private CompraController compraController;
    private List<CompraProduto> listaItensCarrinho;
    private double totalCompra = 0.0;

    public FormCompra() {
        compraController = new CompraController();
        listaItensCarrinho = new ArrayList<>();

        setTitle("Módulo de Compras (Entrada de Estoque & Histórico)");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane abas = new JTabbedPane();

        // =========================================================================
        // ABA 1: REGISTRAR COMPRA
        // =========================================================================
        JPanel painelNovaCompra = new JPanel(new BorderLayout());

        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações da Nota"));
        
        // Configuração do JComboBox de Fornecedores na tela
        cbFornecedor = new JComboBox<>();
        painelCabecalho.add(new JLabel("Fornecedor:"));
        painelCabecalho.add(cbFornecedor);
        
        painelCabecalho.add(new JLabel("Data da Entrada: " + LocalDate.now()));

        JPanel painelProdutos = new JPanel(new GridBagLayout());
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Adicionar Produtos à Nota"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbProduto = new JComboBox<>();
        txtQuantidade = new JTextField("1", 5);
        txtPrecoCusto = new JTextField(8); 
        btnAdicionar = new JButton("Adicionar Produto ➕");

        gbc.gridx = 0; gbc.gridy = 0; painelProdutos.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelProdutos.add(cbProduto, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelProdutos.add(new JLabel("Preço Custo Un:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelProdutos.add(txtPrecoCusto, gbc);
        gbc.gridx = 4; gbc.gridy = 0; painelProdutos.add(new JLabel("Qtd Comprada:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; painelProdutos.add(txtQuantidade, gbc);
        gbc.gridx = 6; gbc.gridy = 0; painelProdutos.add(btnAdicionar, gbc);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCabecalho, BorderLayout.NORTH);
        painelSuperior.add(painelProdutos, BorderLayout.CENTER);
        painelNovaCompra.add(painelSuperior, BorderLayout.NORTH);

        modeloTabela = new DefaultTableModel(new Object[]{"Item", "Produto", "Preço Custo", "Qtd", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Produtos desta Nota de Compra"));
        painelNovaCompra.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTotalGeral = new JTextField("R$ 0.00", 12);
        txtTotalGeral.setFont(new Font("Arial", Font.BOLD, 18));
        txtTotalGeral.setEditable(false);
        txtTotalGeral.setHorizontalAlignment(JTextField.RIGHT);
        painelTotal.add(new JLabel("TOTAL DA NOTA:"));
        painelTotal.add(txtTotalGeral);

        JPanel painelBotoesFinais = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRemoverItem = new JButton("Remover Item ❌");
        btnFinalizar = new JButton("Salvar Entrada 💾");
        btnFinalizar.setBackground(new Color(52, 152, 219)); 
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        
        painelBotoesFinais.add(btnRemoverItem);
        painelBotoesFinais.add(btnFinalizar);

        painelInferior.add(painelTotal, BorderLayout.NORTH);
        painelInferior.add(painelBotoesFinais, BorderLayout.SOUTH);
        painelNovaCompra.add(painelInferior, BorderLayout.SOUTH);

        // =========================================================================
        // ABA 2: HISTÓRICO DE COMPRAS
        // =========================================================================
        JPanel painelHistorico = new JPanel(new GridLayout(2, 1, 0, 10));

        // Adicionado a coluna "Fornecedor" para deixar o histórico informativo
        modeloHistoricoCompras = new DefaultTableModel(new Object[]{"ID Nota / Compra", "Fornecedor", "Data de Registro"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaHistoricoCompras = new JTable(modeloHistoricoCompras);
        JScrollPane scrollHistorico = new JScrollPane(tabelaHistoricoCompras);
        scrollHistorico.setBorder(BorderFactory.createTitledBorder("Notas de Compra Recebidas"));
        painelHistorico.add(scrollHistorico);

        modeloDetalhesItens = new DefaultTableModel(new Object[]{"Produto", "Preço de Custo Un.", "Qtd Recebida"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaDetalhesItens = new JTable(modeloDetalhesItens);
        JScrollPane scrollDetalhes = new JScrollPane(tabelaDetalhesItens);
        scrollDetalhes.setBorder(BorderFactory.createTitledBorder("Itens Pertencentes à Nota Selecionada"));
        painelHistorico.add(scrollDetalhes);

        abas.addTab("📦 Registrar Entrada / Compra", painelNovaCompra);
        abas.addTab("📋 Histórico de Compras", painelHistorico);
        add(abas, BorderLayout.CENTER);

        // =========================================================================
        // LISTENERS
        // =========================================================================
        
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 1) {
                carregarHistoricoComprasDoBanco();
            }
        });

        tabelaHistoricoCompras.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int linha = tabelaHistoricoCompras.getSelectedRow();
                if (linha >= 0) {
                    int idCompra = (int) modeloHistoricoCompras.getValueAt(linha, 0);
                    carregarItensDaCompraSelecionada(idCompra);
                }
            }
        });

        btnAdicionar.addActionListener(e -> {
            try {
                Produto prodSelecionado = (Produto) cbProduto.getSelectedItem();
                CompraProduto item = compraController.criarItemCarrinho(prodSelecionado, txtQuantidade.getText(), txtPrecoCusto.getText());
                listaItensCarrinho.add(item);

                double subtotal = item.getValorUnitario() * item.getQuantidade();
                modeloTabela.addRow(new Object[]{
                    listaItensCarrinho.size(),
                    item.getProduto().getNome(),
                    item.getValorUnitario(),
                    item.getQuantidade(),
                    subtotal
                });

                totalCompra += subtotal;
                txtTotalGeral.setText(String.format("R$ %.2f", totalCompra));
                txtQuantidade.setText("1");
                txtPrecoCusto.setText("");
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
                totalCompra -= subtotalItem;
                txtTotalGeral.setText(String.format("R$ %.2f", totalCompra));
                for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                    modeloTabela.setValueAt(i + 1, i, 0);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Selecione um item para remover.");
            }
        });

        btnFinalizar.addActionListener(e -> {
            try {
                // Recupera o fornecedor selecionado na interface gráfica
                Fornecedor fornSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
                
                // Repassa o fornecedor para o controlador tratar a transação
                compraController.finalizarCompra(fornSelecionado, listaItensCarrinho);
                
                JOptionPane.showMessageDialog(this, "Estoque alimentado e compra registrada com sucesso!");
                limparEAtualizarComponentes();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao processar transação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        limparEAtualizarComponentes();
    }

    private void limparEAtualizarComponentes() {
        listaItensCarrinho.clear();
        modeloTabela.setRowCount(0);
        totalCompra = 0.0;
        txtTotalGeral.setText("R$ 0.00");
        txtQuantidade.setText("1");
        txtPrecoCusto.setText("");
        try {
            // Atualiza e carrega a lista de fornecedores do banco
            cbFornecedor.removeAllItems();
            for (Fornecedor f : compraController.listarFornecedores()) {
                cbFornecedor.addItem(f);
            }
            if (cbFornecedor.getItemCount() > 0) cbFornecedor.setSelectedIndex(0);

            cbProduto.removeAllItems();
            for (Produto p : compraController.listarProdutos()) cbProduto.addItem(p);
            if (cbProduto.getItemCount() > 0) cbProduto.setSelectedIndex(0);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar combos da tela: " + ex.getMessage());
        }
    }

    private void carregarHistoricoComprasDoBanco() {
        try {
            modeloHistoricoCompras.setRowCount(0);
            modeloDetalhesItens.setRowCount(0);
            List<Compra> compras = compraController.listarHistoricoCompras();
            for (Compra c : compras) {
                // Se sua classe Compra tiver relacionamento mapeado com Fornecedor, usamos c.getFornecedor().getNome()
                String nomeFornecedor = (c.getFornecedor() != null) ? c.getFornecedor().getNomeFantasia() : "Não Informado";
                
                modeloHistoricoCompras.addRow(new Object[]{
                    c.getId(),
                    nomeFornecedor,
                    LocalDate.now() 
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + ex.getMessage());
        }
    }

    private void carregarItensDaCompraSelecionada(int idCompra) {
        try {
            modeloDetalhesItens.setRowCount(0);
            List<CompraProduto> itens = compraController.listarItensDaCompra(idCompra);
            for (CompraProduto item : itens) {
                modeloDetalhesItens.addRow(new Object[]{
                    item.getProduto().getNome(),
                    item.getValorUnitario(),
                    item.getQuantidade()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao trazer itens: " + ex.getMessage());
        }
    }
}