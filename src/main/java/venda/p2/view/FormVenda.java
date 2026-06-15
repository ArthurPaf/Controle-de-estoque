package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Cliente;
import venda.p2.model.Produto;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class FormVenda extends JFrame {

    private JComboBox<Cliente> cbCliente;
    private JComboBox<Produto> cbProduto;
    private JTextField txtQuantidade, txtPrecoUn, txtTotalGeral;
    private JButton btnAdicionar, btnRemoverItem, btnFinalizar;
    private JTable tabelaItens;
    private DefaultTableModel modeloTabela;

    private GenericDAO<Venda> vendaDAO;
    private GenericDAO<VendaProduto> vendaProdutoDAO;
    private GenericDAO<Cliente> clienteDAO;
    private GenericDAO<Produto> produtoDAO;

    // Lista em memória para segurar os itens do "carrinho" antes de salvar no banco
    private List<VendaProduto> listaItensCarrinho;
    private double totalVenda = 0.0;

    public FormVenda() {
        vendaDAO = new GenericDAO<>(Venda.class);
        vendaProdutoDAO = new GenericDAO<>(VendaProduto.class);
        clienteDAO = new GenericDAO<>(Cliente.class);
        produtoDAO = new GenericDAO<>(Produto.class);
        listaItensCarrinho = new ArrayList<>();

        setTitle("Registrar Venda (PDV)");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. CABEÇALHO DA VENDA (Cliente e Data) ---
        JPanel painelCabecalho = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        painelCabecalho.setBorder(BorderFactory.createTitledBorder("Informações Básicas"));
        
        cbCliente = new JComboBox<>();
        carregarClientes();
        
        painelCabecalho.add(new JLabel("Cliente:"));
        painelCabecalho.add(cbCliente);
        painelCabecalho.add(new JLabel("Data: " + LocalDate.now())); // Pega a data atual do sistema

        // --- 2. PAINEL DE INCLUSÃO DE PRODUTOS ---
        JPanel painelProdutos = new JPanel(new GridBagLayout());
        painelProdutos.setBorder(BorderFactory.createTitledBorder("Adicionar Itens à Venda"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbProduto = new JComboBox<>();
        carregarProdutos();
        txtQuantidade = new JTextField("1", 5);
        txtPrecoUn = new JTextField(8);
        txtPrecoUn.setEditable(false); // Fica travado, puxa automático do produto
        btnAdicionar = new JButton("Adicionar Item ➕");

        // Preenche o preço unitário automaticamente ao mudar o produto selecionado
        cbProduto.addActionListener(e -> atualizarPrecoUnitario());
        atualizarPrecoUnitario(); // Roda a primeira vez para inicializar

        gbc.gridx = 0; gbc.gridy = 0; painelProdutos.add(new JLabel("Produto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelProdutos.add(cbProduto, gbc);
        gbc.gridx = 2; gbc.gridy = 0; painelProdutos.add(new JLabel("Preço Un:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; painelProdutos.add(txtPrecoUn, gbc);
        gbc.gridx = 4; gbc.gridy = 0; painelProdutos.add(new JLabel("Qtd:"), gbc);
        gbc.gridx = 5; gbc.gridy = 0; painelProdutos.add(txtQuantidade, gbc);
        gbc.gridx = 6; gbc.gridy = 0; painelProdutos.add(btnAdicionar, gbc);

        // Junta Cabeçalho e Painel de Produtos em um container superior
        JPanel painelSuperior = new JPanel(new BorderLayout());
        painelSuperior.add(painelCabecalho, BorderLayout.NORTH);
        painelSuperior.add(painelProdutos, BorderLayout.CENTER);
        add(painelSuperior, BorderLayout.NORTH);

        // --- 3. TABELA DE ITENS (Carrinho) ---
        modeloTabela = new DefaultTableModel(new Object[]{"Item", "Produto", "Preço Un.", "Qtd", "Subtotal"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaItens = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Itens da Venda Atual"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. PAINEL INFERIOR (Totalizador e Finalização) ---
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
        add(painelInferior, BorderLayout.SOUTH);

        // --- 5. EVENTOS ---
        btnAdicionar.addActionListener(e -> adicionarItemAoCarrinho());
        btnRemoverItem.addActionListener(e -> removerItemDoCarrinho());
        btnFinalizar.addActionListener(e -> fecharVendaNoBanco());
    }

    private void carregarClientes() {
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            for (Cliente c : clientes) cbCliente.addItem(c);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar clientes: " + e.getMessage());
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

    private void atualizarPrecoUnitario() {
        Produto p = (Produto) cbProduto.getSelectedItem();
        if (p != null) {
            txtPrecoUn.setText(String.valueOf(p.getPreco()));
        }
    }

    private void adicionarItemAoCarrinho() {
        Produto p = (Produto) cbProduto.getSelectedItem();
        String qtdStr = txtQuantidade.getText().trim();

        if (p == null || qtdStr.isEmpty()) return;

        try {
            double qtd = Double.parseDouble(qtdStr);
            double precoUn = p.getPreco();
            double subtotal = precoUn * qtd;

            // Cria a entidade associativa VendaProduto (sem a venda ainda, ligamos depois)
            VendaProduto item = new VendaProduto();
            item.setProduto(p);
            item.setQuantidade(qtd);
            item.setValorUnitario(precoUn);

            listaItensCarrinho.add(item);

            // Adiciona visualmente na tabela gráfica
            modeloTabela.addRow(new Object[]{
                listaItensCarrinho.size(),
                p.getNome(),
                precoUn,
                qtd,
                subtotal
            });

            // Atualiza o totalizador da interface
            totalVenda += subtotal;
            txtTotalGeral.setText(String.format("R$ %.2f", totalVenda));
            txtQuantidade.setText("1");

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerItemDoCarrinho() {
        int linha = tabelaItens.getSelectedRow();
        if (linha >= 0) {
            double subtotalItem = (double) modeloTabela.getValueAt(linha, 4);
            
            // Remove da lista em memória e do grid gráfico
            listaItensCarrinho.remove(linha);
            modeloTabela.removeRow(linha);

            // Recalcula o total
            totalVenda -= subtotalItem;
            txtTotalGeral.setText(String.format("R$ %.2f", totalVenda));
            
            // Corrige os números dos índices na tabela visual
            for (int i = 0; i < modeloTabela.getRowCount(); i++) {
                modeloTabela.setValueAt(i + 1, i, 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um item da tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void fecharVendaNoBanco() {
        Cliente cliente = (Cliente) cbCliente.getSelectedItem();

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Selecione um cliente válido.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (listaItensCarrinho.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Adicione pelo menos um produto para fechar a venda.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // 1. Instancia e salva a Venda pai
            Venda venda = new Venda();
            venda.setCliente(cliente);
            // Se sua entidade Venda usar java.util.Date ou LocalDate, atribua aqui
            // venda.setDataVenda(LocalDate.now()); 

            vendaDAO.salvar(venda); // Salva no Postgres e gera o ID da venda

            // 2. Varre os itens vinculando-os à venda gerada e salvando um por um
            for (VendaProduto item : listaItensCarrinho) {
                item.setVenda(venda); // Passa o objeto Venda com o ID gerado para a FK
                vendaProdutoDAO.salvar(item);
                
                // Opcional: Aqui você pode dar um update no estoque do produto subtraindo a quantidade vendida!
            }

            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso no banco de dados!");
            limparTelaVenda();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro crítico ao processar transação: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void limparTelaVenda() {
        listaItensCarrinho.clear();
        modeloTabela.setRowCount(0);
        totalVenda = 0.0;
        txtTotalGeral.setText("R$ 0.00");
        if (cbCliente.getItemCount() > 0) cbCliente.setSelectedIndex(0);
        if (cbProduto.getItemCount() > 0) cbProduto.setSelectedIndex(0);
    }
}