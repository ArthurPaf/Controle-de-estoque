package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import venda.p2.controller.CompraController;
import venda.p2.model.Produto;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Fornecedor;

public class FormCompra extends JFrame {

    // Componentes da Aba 1
    private JComboBox<Fornecedor> cbFornecedor; 
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade, txtPrecoCusto, txtTotalGeral;
    private JButton btnAdicionar, btnRemoverItem, btnFinalizar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    // Componentes da Aba 2 (Filtros Avançados e Tabelas)
    private JFormattedTextField txtFiltroDataInicio;
    private JFormattedTextField txtFiltroDataFim;
    private JComboBox<Object> cbFiltroFornecedor; // Aceita String "Todos" e Objetos Fornecedor
    private JButton btnPesquisarFiltro;
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
        setSize(950, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane abas = new JTabbedPane();

        // =========================================================================
        // ABA 1: REGISTRAR COMPRA
        // =========================================================================
        JPanel painelNovaCompra = new JPanel(new BorderLayout());

        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações da Nota"));
        
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
        // ABA 2: HISTÓRICO DE COMPRAS (PAINEL DE FILTROS APLICADO COM COMPORTAMENTO)
        // =========================================================================
        JPanel painelAbaHistoricoCompleta = new JPanel(new BorderLayout());

        JPanel painelFiltrosPesquisa = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        painelFiltrosPesquisa.setBorder(BorderFactory.createTitledBorder("Filtros de Pesquisa (Período & Fornecedor)"));

        try {
            MaskFormatter mascaraData = new MaskFormatter("##/##/####");
            mascaraData.setPlaceholderCharacter('_');
            txtFiltroDataInicio = new JFormattedTextField(mascaraData);
            txtFiltroDataFim = new JFormattedTextField(mascaraData);

            // Reverte em caso de digitação parcial
            txtFiltroDataInicio.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
            txtFiltroDataFim.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);

            // Listeners para manter as barrinhas estruturadas visíveis se apagar tudo
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
        cbFiltroFornecedor = new JComboBox<>();
        btnPesquisarFiltro = new JButton("Pesquisar / Filtrar 🔍");

        painelFiltrosPesquisa.add(new JLabel("Data Início:"));
        painelFiltrosPesquisa.add(txtFiltroDataInicio);
        painelFiltrosPesquisa.add(new JLabel("Data Fim:"));
        painelFiltrosPesquisa.add(txtFiltroDataFim);
        painelFiltrosPesquisa.add(new JLabel("Fornecedor:"));
        painelFiltrosPesquisa.add(cbFiltroFornecedor);
        painelFiltrosPesquisa.add(btnPesquisarFiltro);

        painelAbaHistoricoCompleta.add(painelFiltrosPesquisa, BorderLayout.NORTH);

        JPanel painelTabelasHistorico = new JPanel(new GridLayout(2, 1, 0, 10));

        modeloHistoricoCompras = new DefaultTableModel(new Object[]{"ID Nota / Compra", "Fornecedor", "Data de Registro"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaHistoricoCompras = new JTable(modeloHistoricoCompras);
        JScrollPane scrollHistorico = new JScrollPane(tabelaHistoricoCompras);
        scrollHistorico.setBorder(BorderFactory.createTitledBorder("Notas de Compra Recebidas"));
        painelTabelasHistorico.add(scrollHistorico);

        modeloDetalhesItens = new DefaultTableModel(new Object[]{"Produto", "Preço de Custo Un.", "Qtd Recebida"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaDetalhesItens = new JTable(modeloDetalhesItens);
        JScrollPane scrollDetalhes = new JScrollPane(tabelaDetalhesItens);
        scrollDetalhes.setBorder(BorderFactory.createTitledBorder("Itens Pertencentes à Nota Selecionada"));
        painelTabelasHistorico.add(scrollDetalhes);

        painelAbaHistoricoCompleta.add(painelTabelasHistorico, BorderLayout.CENTER);

        abas.addTab("📦 Registrar Entrada / Compra", painelNovaCompra);
        abas.addTab("📋 Histórico de Compras", painelAbaHistoricoCompleta);
        add(abas, BorderLayout.CENTER);

        // =========================================================================
        // LISTENERS
        // =========================================================================
        
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 1) {
                carregarHistoricoComprasDoBanco();
            }
        });

        btnPesquisarFiltro.addActionListener(e -> carregarHistoricoComprasDoBanco());

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
                Fornecedor fornSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
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
            cbFornecedor.removeAllItems();
            cbFiltroFornecedor.removeAllItems();
            
            cbFiltroFornecedor.addItem("-- TODOS OS FORNECEDORES --");

            List<Fornecedor> fornecedores = compraController.listarFornecedores();
            for (Fornecedor f : fornecedores) {
                cbFornecedor.addItem(f);
                cbFiltroFornecedor.addItem(f);
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

        LocalDate dataInicio = null;
        LocalDate dataFim = null;
        Integer idFornecedor = null;

        DateTimeFormatter formatadorInput = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatadorExibicao = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String textoInicioLimpo = txtFiltroDataInicio.getText().replace("/", "").replace("_", "").trim();
        String textoFimLimpo = txtFiltroDataFim.getText().replace("/", "").replace("_", "").trim();

        if (textoInicioLimpo.length() == 8) {
            dataInicio = LocalDate.parse(txtFiltroDataInicio.getText().trim(), formatadorInput);
        }
        if (textoFimLimpo.length() == 8) {
            dataFim = LocalDate.parse(txtFiltroDataFim.getText().trim(), formatadorInput);
        }

        Object fornecedorSelecionado = cbFiltroFornecedor.getSelectedItem();
        if (fornecedorSelecionado instanceof Fornecedor) {
            idFornecedor = ((Fornecedor) fornecedorSelecionado).getId();
        }

        // Busca os dados filtrados do banco via Controller
        List<Compra> compras = compraController.consultarComprasComFiltros(dataInicio, dataFim, idFornecedor);
        
        for (Compra c : compras) {
            String nomeFornecedor = (c.getFornecedor() != null) ? c.getFornecedor().getNomeFantasia() : "Não Informado";
            
            // CORREÇÃO AQUI: Captura a data real guardada no objeto vindo do banco
            String dataFormatada = "Não informada";
            if (c.getDataCompra() != null) {
                // Pega a data (que está no formato do banco) e transforma em DD/MM/AAAA para a tabela
                dataFormatada = c.getDataCompra().format(formatadorExibicao);
            }
            
            modeloHistoricoCompras.addRow(new Object[]{
                c.getId(),
                nomeFornecedor,
                dataFormatada // Adiciona a string formatada corretamente na coluna
            });
        }

        if (compras.isEmpty() && (!textoInicioLimpo.isEmpty() || idFornecedor != null)) {
            JOptionPane.showMessageDialog(this, "Nenhuma nota de compra encontrada para os filtros aplicados.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    } catch (java.time.format.DateTimeParseException dtpe) {
        JOptionPane.showMessageDialog(this, "Data inválida! Por favor, use o formato DD/MM/AAAA.", "Aviso", JOptionPane.WARNING_MESSAGE);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Erro ao carregar histórico: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
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