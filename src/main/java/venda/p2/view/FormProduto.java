package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.ProdutoController;
import venda.p2.model.Produto;
import venda.p2.model.Categoria;

public class FormProduto extends JFrame {

    private JTextField txtNome, txtPreco, txtQuantidade, txtPesquisa; 
    private JComboBox<Categoria> cbCategoria;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar, btnPesquisar; 
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private ProdutoController produtoController;
    private Produto produtoSelecionado;

    public FormProduto() {
        produtoController = new ProdutoController();

        setTitle("Gerenciar Produtos (CRUD)");
        setSize(650, 550); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PAINEL PRINCIPAL ---
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        // --- 1. FORMULÁRIO DE CADASTRO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);
        txtPreco = new JTextField(10);
        txtQuantidade = new JTextField(10);
        cbCategoria = new JComboBox<>();

       
        carregarCategorias();

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("Estoque Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtQuantidade, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(cbCategoria, gbc);

        // --- 2. PAINEL DE BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnSalvar = new JButton("Salvar Novo");
        btnEditar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar");

        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        painelCampos.add(painelAcoes, gbc);

        painelTopo.add(painelCampos);

        // --- PAINEL DE PESQUISA ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Pesquisar Produtos"));
        txtPesquisa = new JTextField(25);
        btnPesquisar = new JButton("Pesquisar");

        painelBusca.add(new JLabel("Nome do Produto:"));
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);

        painelTopo.add(painelBusca);

        add(painelTopo, BorderLayout.NORTH);

        // --- 3. TABELA DE PRODUTOS ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaProdutos = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Produtos em Estoque"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. EVENTOS ---

        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaProdutos.getSelectedRow();
                if (linha >= 0) {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    try {
                        produtoSelecionado = produtoController.buscarPorId(id);
                        if (produtoSelecionado != null) {
                            txtNome.setText(produtoSelecionado.getNome());
                            txtPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
                            txtQuantidade.setText(String.valueOf(produtoSelecionado.getQuantidade()));
                            cbCategoria.setSelectedItem(produtoSelecionado.getCategoria());

                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormProduto.this, "Erro ao carregar produto: " + ex.getMessage());
                    }
                }
            }
        });

        // Botão Salvar
        btnSalvar.addActionListener(e -> {
            try {
                produtoController.salvarProduto(
                    txtNome.getText(),
                    txtPreco.getText(),
                    txtQuantidade.getText(),
                    (Categoria) cbCategoria.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        });

        // Botão Editar
        btnEditar.addActionListener(e -> {
            try {
                produtoController.atualizarProduto(
                    produtoSelecionado,
                    txtNome.getText(),
                    txtPreco.getText(),
                    txtQuantidade.getText(),
                    (Categoria) cbCategoria.getSelectedItem()
                );
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        });

        // Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (produtoSelecionado != null) {
                int conf = JOptionPane.showConfirmDialog(this, "Deseja excluir o produto '" + produtoSelecionado.getNome() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        produtoController.excluirProduto(produtoSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Produto removido com sucesso!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Pesquisar
        btnPesquisar.addActionListener(e -> {
            String busca = txtPesquisa.getText().trim();
            try {
                
                List<Produto> resultado = produtoController.pesquisarPorNome(busca);
                atualizarTabela(resultado);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage());
            }
        });

        // Botão Limpar 
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtPreco.setText("");
            txtQuantidade.setText("");
            txtPesquisa.setText("");
            if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
            
            produtoSelecionado = null;
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            try {
                List<Produto> completa = produtoController.listarTodos();
                atualizarTabela(completa);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar: " + ex.getMessage());
            }
        });

        btnLimpar.doClick();
    }

    private void atualizarTabela(List<Produto> lista) {
        modeloTabela.setRowCount(0);
        for (Produto prod : lista) {
            modeloTabela.addRow(new Object[]{
                prod.getId(),
                prod.getNome(),
                prod.getPreco(),
                prod.getQuantidade(),
                prod.getCategoria() != null ? prod.getCategoria().getNome() : "Sem Categoria"
            });
        }
    }

    private void carregarCategorias() {
        try {
            cbCategoria.removeAllItems();
            List<Categoria> list = produtoController.listarCategorias();
            for (Categoria c : list) {
                cbCategoria.addItem(c);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao carregar combo de categorias: " + ex.getMessage());
        }
    }
}