package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.text.SimpleDateFormat;
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

    // Componentes da Aba 2 (Histórico de Vendas com Filtros - RF012)
    private JFormattedTextField txtFiltroDataInicio;
    private JFormattedTextField txtFiltroDataFim;
    private JComboBox<Object> cbFiltroCliente; // Usamos Object para aceitar a opção "Todos"
    private JButton btnPesquisarFiltro;
    private JTable tabelaHistoricoVendas;
    private DefaultTableModel modeloHistoricoVendas;
    private JTable tabelaDetalhesItens;
    private DefaultTableModel modeloDetalhesItens;

    // Controle
    private VendaController vendaController;
    private List<VendaProduto> listaItensCarrinho;
    private double totalVenda = 0.0;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public FormVenda() {
        vendaController = new VendaController();
        listaItensCarrinho = new ArrayList<>();

        setTitle("Módulo de Vendas (PDV & Histórico)");
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane abas = new JTabbedPane();

        // =========================================================================
        // CONSTRUÇÃO DA ABA 1: REGISTRAR VENDA
        // =========================================================================
        JPanel painelNovaVenda = new JPanel(new BorderLayout());

        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações Básicas"));
        cbCliente = new JComboBox<>();
        painelCabecalho.add(new JLabel("Cliente:"));
        painelCabecalho.add(cbCliente);
        painelCabecalho.add(new JLabel("Data: " + LocalDate.now()));

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

        modeloTabela = new DefaultTableModel(new Object[]{"Item", "Produto", "Preço Un.", "Qtd", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Itens da Venda Atual"));
        painelNovaVenda.add(scrollTabela, BorderLayout.CENTER);

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
        // CONSTRUÇÃO DA ABA 2: HISTÓRICO DE VENDAS COM PAINEL DE FILTROS (RF012)
        // =========================================================================
        JPanel painelAbaHistoricoCompleta = new JPanel(new BorderLayout());

        // --- Painel Superior de Pesquisa Avançada ---
        JPanel painelFiltrosPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        painelFiltrosPesquisa.setBorder(BorderFactory.createTitledBorder("Filtros de Pesquisa (Período & Cliente)"));
        
        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtFiltroDataInicio = new JFormattedTextField(mascaraData);
            txtFiltroDataFim = new JFormattedTextField(mascaraData);
            
            // Reverte para o estado anterior caso a digitação seja parcial/inválida
            txtFiltroDataInicio.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
            txtFiltroDataFim.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
            
            // Listeners para tratar a deleção completa mantendo os placeholders estruturais
            txtFiltroDataInicio.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    String limpo = txtFiltroDataInicio.getText().replace("/", "").replace("_", "").trim();
                    if (limpo.isEmpty()) {
                        txtFiltroDataInicio.setValue(null);
                    }
                }
            });

            txtFiltroDataFim.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    String limpo = txtFiltroDataFim.getText().replace("/", "").replace("_", "").trim();
                    if (limpo.isEmpty()) {
                        txtFiltroDataFim.setValue(null);
                    }
                }
            });
            
        } catch (Exception ex) {
            txtFiltroDataInicio = new JFormattedTextField();
            txtFiltroDataFim = new JFormattedTextField();
        }
        txtFiltroDataInicio.setColumns(8);
        txtFiltroDataFim.setColumns(8);
        cbFiltroCliente = new JComboBox<>();
        btnPesquisarFiltro = new JButton("Pesquisar / Filtrar 🔍");

        painelFiltrosPesquisa.add(new JLabel("Data Início:"));
        painelFiltrosPesquisa.add(txtFiltroDataInicio);
        painelFiltrosPesquisa.add(new JLabel("Data Fim:"));
        painelFiltrosPesquisa.add(txtFiltroDataFim);
        painelFiltrosPesquisa.add(new JLabel("Cliente:"));
        painelFiltrosPesquisa.add(cbFiltroCliente);
        painelFiltrosPesquisa.add(btnPesquisarFiltro);

        painelAbaHistoricoCompleta.add(painelFiltrosPesquisa, BorderLayout.NORTH);

        // Painel Central com as tabelas de Mestre-Detalhe
        JPanel painelTabelasHistorico = new JPanel(new GridLayout(2, 1, 0, 10));

        modeloHistoricoVendas = new DefaultTableModel(new Object[]{"ID Venda", "Cliente", "Data"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaHistoricoVendas = new JTable(modeloHistoricoVendas);
        JScrollPane scrollHistorico = new JScrollPane(tabelaHistoricoVendas);
        scrollHistorico.setBorder(BorderFactory.createTitledBorder("Vendas Realizadas (Selecione uma para ver os itens)"));
        painelTabelasHistorico.add(scrollHistorico);

        modeloDetalhesItens = new DefaultTableModel(new Object[]{"Produto", "Preço Unitário", "Qtd Vendida"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaDetalhesItens = new JTable(modeloDetalhesItens);
        JScrollPane scrollDetalhes = new JScrollPane(tabelaDetalhesItens);
        scrollDetalhes.setBorder(BorderFactory.createTitledBorder("Produtos Pertencentes à Venda Selecionada"));
        painelTabelasHistorico.add(scrollDetalhes);

        painelAbaHistoricoCompleta.add(painelTabelasHistorico, BorderLayout.CENTER);

        // Adiciona as abas estruturadas
        abas.addTab("🛒 Registrar Venda", painelNovaVenda);
        abas.addTab("📋 Histórico de Vendas", painelAbaHistoricoCompleta);
        add(abas, BorderLayout.CENTER);

        // =========================================================================
        // EVENTOS E LISTENERS
        // =========================================================================
        
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 1) {
                carregarHistoricoVendasDoBanco();
            }
        });

        btnPesquisarFiltro.addActionListener(e -> carregarHistoricoVendasDoBanco());

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
            cbFiltroCliente.removeAllItems();
            
            cbFiltroCliente.addItem("-- TODOS OS CLIENTES --");

            List<Cliente> clientes = vendaController.listarClientes();
            for (Cliente c : clientes) {
                cbCliente.addItem(c);
                cbFiltroCliente.addItem(c); 
            }
            
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

    private void carregarHistoricoVendasDoBanco() {
        try {
            modeloHistoricoVendas.setRowCount(0);
            modeloDetalhesItens.setRowCount(0);

            java.time.LocalDate dataInicio = null;
            java.time.LocalDate dateFim = null;
            Integer idCliente = null;

            java.time.format.DateTimeFormatter formatador = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            java.time.format.DateTimeFormatter formatadorExibicao = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

            String textoInicioLimpo = txtFiltroDataInicio.getText().replace("/", "").replace("_", "").trim();
            String textoFimLimpo = txtFiltroDataFim.getText().replace("/", "").replace("_", "").trim();

            if (textoInicioLimpo.length() == 8) {
                dataInicio = java.time.LocalDate.parse(txtFiltroDataInicio.getText().trim(), formatador);
            }

            if (textoFimLimpo.length() == 8) {
                dateFim = java.time.LocalDate.parse(txtFiltroDataFim.getText().trim(), formatador);
            }

            Object clienteSelecionado = cbFiltroCliente.getSelectedItem();
            if (clienteSelecionado instanceof Cliente) {
                idCliente = ((Cliente) clienteSelecionado).getId();
            }

            List<Venda> vendas = vendaController.consultarVendasComFiltros(dataInicio, dateFim, idCliente);
            
            for (Venda v : vendas) {
                String dataFormatada = "";
                if (v.getDataVenda() != null) {
                    dataFormatada = v.getDataVenda().format(formatadorExibicao);
                }

                modeloHistoricoVendas.addRow(new Object[]{
                    v.getId(),
                    v.getCliente().getNome(),
                    dataFormatada
                });
            }
            
            if (vendas.isEmpty() && (!textoInicioLimpo.isEmpty() || idCliente != null)) {
                JOptionPane.showMessageDialog(this, "Nenhuma venda encontrada para os filtros aplicados.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (java.time.format.DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Data digitada inválida! Por favor, use o formato DD/MM/AAAA.", "Aviso", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao aplicar filtros: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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