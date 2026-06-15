package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Categoria;
import venda.p2.model.Produto;

public class FormProduto extends JFrame {

    private JTextField txtNome, txtPreco, txtEstoque;
    private JComboBox<Categoria> cbCategoria;
    private JButton btnSalvar, btnExcluir, btnEditar, btnLimpar;
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;

    private GenericDAO<Produto> produtoDAO;
    private GenericDAO<Categoria> categoriaDAO;
    
    // Variável para guardar o produto que foi selecionado na tabela
    private Produto produtoSelecionado;

    public FormProduto() {
        produtoDAO = new GenericDAO<>(Produto.class);
        categoriaDAO = new GenericDAO<>(Categoria.class);

        setTitle("Gerenciar Produtos (CRUD)");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. FORMULÁRIO DE CADASTRO (Parte Superior) ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(20);
        txtPreco = new JTextField(10);
        txtEstoque = new JTextField(10);
        cbCategoria = new JComboBox<>();

        carregarCategorias();

        // Adicionando componentes na Grid
        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("Estoque Inicial:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtEstoque, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(cbCategoria, gbc);

        // --- 2. PAINEL DE BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSalvar = new JButton("Salvar Novo");
        btnEditar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");
        btnLimpar = new JButton("Limpar/Cancelar");

        // Desabilita editar e excluir até que o usuário selecione algo
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        painelAcoes.add(btnSalvar);
        painelAcoes.add(btnEditar);
        painelAcoes.add(btnExcluir);
        painelAcoes.add(btnLimpar);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- 3. TABELA DE PRODUTOS (Parte Inferior) ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "Preço", "Estoque", "Categoria"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Bloqueia a edição direta dando dois cliques na célula da tabela
            }
        };
        
        tabelaProdutos = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaProdutos);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Produtos Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        atualizarTabela();

        // --- 4. EVENTOS ---

        // Evento de clique na tabela
        tabelaProdutos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherCamposPelaTabela();
            }
        });

        // Botão Salvar Novo
        btnSalvar.addActionListener(e -> salvarProduto());

        // Botão Editar/Atualizar
        btnEditar.addActionListener(e -> editarProduto());

        // Botão Excluir
        btnExcluir.addActionListener(e -> excluirProduto());

        // Botão Limpar
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.listarTodos();
            for (Categoria cat : categorias) {
                cbCategoria.addItem(cat); 
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar categorias: " + e.getMessage());
        }
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            for (Produto p : produtos) {
                String nomeCategoria = (p.getCategoria() != null) ? p.getCategoria().toString() : "Sem Categoria";
                modeloTabela.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getPreco(),
                    p.getQuantidade(),
                    nomeCategoria
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar produtos: " + e.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaProdutos.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            
            try {
                // Busca o objeto completo direto do banco para garantir consistência
                produtoSelecionado = produtoDAO.buscarPorId(id); 
                
                if (produtoSelecionado != null) {
                    txtNome.setText(produtoSelecionado.getNome());
                    txtPreco.setText(String.valueOf(produtoSelecionado.getPreco()));
                    txtEstoque.setText(String.valueOf(produtoSelecionado.getQuantidade()));
                    
                    // Seleciona a categoria correta no ComboBox
                    for (int i = 0; i < cbCategoria.getItemCount(); i++) {
                        Categoria cat = cbCategoria.getItemAt(i);
                        if (cat.getId() == produtoSelecionado.getCategoria().getId()) {
                            cbCategoria.setSelectedIndex(i);
                            break;
                        }
                    }

                    // Ajusta os botões
                    btnSalvar.setEnabled(false);
                    btnEditar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados do produto: " + ex.getMessage());
            }
        }
    }

    private void salvarProduto() {
        if (validarCampos()) {
            try {
                Produto p = new Produto();
                p.setNome(txtNome.getText().trim());
                p.setPreco(Double.parseDouble(txtPreco.getText().trim()));
                p.setQuantidade(Double.parseDouble(txtEstoque.getText().trim()));
                p.setCategoria((Categoria) cbCategoria.getSelectedItem());

                produtoDAO.salvar(p);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
            }
        }
    }

    private void editarProduto() {
        if (produtoSelecionado != null && validarCampos()) {
            try {
                // Atualiza o objeto que já veio mapeado com o ID correto
                produtoSelecionado.setNome(txtNome.getText().trim());
                produtoSelecionado.setPreco(Double.parseDouble(txtPreco.getText().trim()));
                produtoSelecionado.setQuantidade(Double.parseDouble(txtEstoque.getText().trim()));
                produtoSelecionado.setCategoria((Categoria) cbCategoria.getSelectedItem());

                // No Hibernate, o próprio método salvar (ou um atualizar/merge se houver) cuida do update se o ID já existir
                produtoDAO.salvar(produtoSelecionado); 
                
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        }
    }

    private void excluirProduto() {
        if (produtoSelecionado != null) {
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir o produto " + produtoSelecionado.getNome() + "?", 
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    // Passa o ID do produto selecionado para o método deletar do GenericDAO
                    produtoDAO.excluir(produtoSelecionado.getId());
                    
                    JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage() + 
                        "\n(Pode estar acoplado a uma venda/compra ativa)");
                }
            }
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        if (cbCategoria.getItemCount() > 0) cbCategoria.setSelectedIndex(0);
        
        produtoSelecionado = null;
        
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        
        atualizarTabela();
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || txtPreco.getText().trim().isEmpty() || txtEstoque.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        try {
            Double.parseDouble(txtPreco.getText().trim());
            Double.parseDouble(txtEstoque.getText().trim());
            return true;
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço e Estoque devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}