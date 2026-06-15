package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Produto;
import venda.p2.model.Fornecedor;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;

public class FormCompra extends JFrame {

    private JComboBox<Fornecedor> cbFornecedor; // Novo ComboBox adicionado
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade, txtPrecoCusto, txtTotalGeral;
    private JButton btnAdicionar, btnRemoverItem, btnFinalizar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    private GenericDAO<Compra> compraDAO;
    private GenericDAO<CompraProduto> compraProdutoDAO;
    private GenericDAO<Produto> produtoDAO;
    private GenericDAO<Fornecedor> fornecedorDAO; // DAO de Fornecedor

    private List<CompraProduto> listaItensCarrinho;
    private double totalCompra = 0.0;

    public FormCompra() {
        compraDAO = new GenericDAO<>(Compra.class);
        compraProdutoDAO = new GenericDAO<>(CompraProduto.class);
        produtoDAO = new GenericDAO<>(Produto.class);
        fornecedorDAO = new GenericDAO<>(Fornecedor.class);
        listaItensCarrinho = new ArrayList<>();

        setTitle("Registrar Compra (Entrada de Estoque)");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. CABEÇALHO DA COMPRA (Fornecedor e Data) ---
        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações Básicas"));
        
        cbFornecedor = new JComboBox<>();
        carregarFornecedores(); // Carrega os fornecedores salvos no banco
        
        painelCabecalho.add(new JLabel("Fornecedor:"));
        painelCabecalho.add(cbFornecedor);
        painelCabecalho.add(new JLabel("Data da Compra: " + LocalDate.now()));

        // --- 2. PAINEL DE ADIÇÃO DE PRODUTOS ---
        JPanel painelProdutos = new JPanel(new GridBagLayout());
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Adicionar Itens à Compra"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbProduto = new JComboBox<>();
        carregarProdutos();
        txtQuantidade = new JTextField("1", 5);
        txtPrecoCusto = new JTextField(8);

        btnAdicionar = new JButton("Adicionar Item 📥");

        gbc.gridx = 0; gbc.gridy = 0; painelProdutos.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelProdutos.add(cbProduto, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelProdutos.add(new JLabel("Preço de Custo (R$):"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelProdutos.add(txtPrecoCusto, gbc);
        gbc.gridx = 4; gbc.gridy = 0; painelProdutos.add(new JLabel("Qtd:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; painelProdutos.add(txtQuantidade, gbc);
        gbc.gridx = 6; gbc.gridy = 0; painelProdutos.add(btnAdicionar, gbc);

        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCabecalho, BorderLayout.NORTH);
        painelSuperior.add(painelProdutos, BorderLayout.CENTER);
        add(painelSuperior, BorderLayout.NORTH);

        // --- 3. TABELA DE ITENS ---
        modeloTabela = new DefaultTableModel(new Object[]{"Item", "Produto", "Preço Custo", "Qtd", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Itens da Compra Atual"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. PAINEL INFERIOR (Total e Finalização) ---
        JPanel painelInferior = new JPanel(new BorderLayout());
        painelInferior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        txtTotalGeral = new JTextField("R$ 0.00", 12);
        txtTotalGeral.setFont(new Font("Arial", Font.BOLD, 18));
        txtTotalGeral.setEditable(false);
        txtTotalGeral.setHorizontalAlignment(JTextField.RIGHT);
        painelTotal.add(new JLabel("TOTAL DA COMPRA:"));
        painelTotal.add(txtTotalGeral);

        JPanel painelBotoesFinais = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRemoverItem = new JButton("Remover Item ❌");
        btnFinalizar = new JButton("Finalizar Compra 💾");
        btnFinalizar.setBackground(new Color(41, 128, 185));
        btnFinalizar.setForeground(Color.WHITE);
        btnFinalizar.setFont(new Font("Arial", Font.BOLD, 14));
        
        painelBotoesFinais.add(btnRemoverItem);
        painelBotoesFinais.add(btnFinalizar);

        painelInferior.add(painelTotal, BorderLayout.NORTH);
        painelInferior.add(painelBotoesFinais, BorderLayout.SOUTH);
        add(painelInferior, BorderLayout.SOUTH);

        // --- 5. EVENTOS ---
        btnAdicionar.addActionListener(e -> adicionarItemAoCarrinho());
        btnRemoverItem.addActionListener(e -> removerItemDoCarrinho());
        btnFinalizar.addActionListener(e -> fecharCompraNoBanco());
    }

    private void carregarFornecedores() {
        try {
            List<Fornecedor> fornecedores = fornecedorDAO.listarTodos();
            for (Fornecedor f : fornecedores) cbFornecedor.addItem(f);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar fornecedores: " + e.getMessage());
        }
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            for (Produto p : produtos) cbProduto.addItem(p);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void adicionarItemAoCarrinho() {
        Produto p = (Produto) cbProduto.getSelectedItem();
        String qtdStr = txtQuantidade.getText().trim();
        String custoStr = txtPrecoCusto.getText().trim();

        if (p == null || qtdStr.isEmpty() || custoStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos do item.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double qtd = Double.parseDouble(qtdStr);
            double precoCusto = Double.parseDouble(custoStr);
            double subtotal = precoCusto * qtd;

            CompraProduto item = new CompraProduto();
            item.setProduto(p);
            item.setQuantidade(qtd);
            item.setValorUnitario(precoCusto);

            listaItensCarrinho.add(item);

            modeloTabela.addRow(new Object[]{
                listaItensCarrinho.size(),
                p.getNome(),
                precoCusto,
                qtd,
                subtotal
            });

            totalCompra += subtotal;
            txtTotalGeral.setText(String.format("R$ %.2f", totalCompra));
            txtQuantidade.setText("1");
            txtPrecoCusto.setText("");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade ou preço inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItemDoCarrinho() {
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
            JOptionPane.showMessageDialog(this, "Selecione um item para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void fecharCompraNoBanco() {
        Fornecedor fornecedor = (Fornecedor) cbFornecedor.getSelectedItem();

        if (fornecedor == null) {
            JOptionPane.showMessageDialog(this, "Selecione um fornecedor válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaItensCarrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione itens para fechar a compra.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Cria e persiste a Compra principal com o Fornecedor e o valor total
            Compra compra = new Compra();
            compra.setFornecedor(fornecedor); // Vincula o fornecedor selecionado
            compra.setValorTotal(totalCompra);

            compraDAO.salvar(compra); // Salva no banco e gera ID

            // 2. Vincula a compra gerada em cada item e persiste no banco
            for (CompraProduto item : listaItensCarrinho) {
                item.setCompra(compra);
                compraProdutoDAO.salvar(item);
                
                // Opcional: Atualizar a quantidade em estoque do Produto somando os itens comprados
            }

            JOptionPane.showMessageDialog(this, "Compra registrada com sucesso no PostgreSQL!");
            limparTelaCompra();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao processar a compra: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparTelaCompra() {
        listaItensCarrinho.clear();
        modeloTabela.setRowCount(0);
        totalCompra = 0.0;
        txtTotalGeral.setText("R$ 0.00");
        if (cbFornecedor.getItemCount() > 0) cbFornecedor.setSelectedIndex(0);
        if (cbProduto.getItemCount() > 0) cbProduto.setSelectedIndex(0);
    }
}