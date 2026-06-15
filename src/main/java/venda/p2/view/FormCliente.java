package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Cliente;

public class FormCliente extends JFrame {

    private JTextField txtNome, txtCpf, txtRg, txtEndereco, txtTelefone;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    private GenericDAO<Cliente> clienteDAO;
    private Cliente clienteSelecionado;

    public FormCliente() {
        clienteDAO = new GenericDAO<>(Cliente.class);

        setTitle("Gerenciar Clientes (CRUD)");
        setSize(750, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- 1. FORMULÁRIO DE CADASTRO (Parte Superior) ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Cliente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(25);
        txtCpf = new JTextField(14);
        txtRg = new JTextField(12);
        txtEndereco = new JTextField(30);
        txtTelefone = new JTextField(15);

        // Posicionando na Grid
        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; painelCampos.add(new JLabel("CPF:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; painelCampos.add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 2; painelCampos.add(new JLabel("RG:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; painelCampos.add(txtRg, gbc);

        gbc.gridx = 0; gbc.gridy = 3; painelCampos.add(new JLabel("Endereço:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; painelCampos.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 4; painelCampos.add(new JLabel("Telefone:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; painelCampos.add(txtTelefone, gbc);

        // --- 2. PAINEL DE BOTÕES DE AÇÃO ---
        JPanel painelAcoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
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

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 5, 5);
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- 3. TABELA DE CLIENTES (Parte Central/Inferior) ---
        // Tabela atualizada com as novas colunas
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Nome", "CPF", "RG", "Endereço", "Telefone"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tabelaClientes = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaClientes);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Clientes Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        atualizarTabela();

        // --- 4. EVENTOS ---
        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                preencherCamposPelaTabela();
            }
        });

        btnSalvar.addActionListener(e -> salvarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnExcluir.addActionListener(e -> excluirCliente());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<Cliente> clientes = clienteDAO.listarTodos();
            for (Cliente c : clientes) {
                modeloTabela.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpf(),
                    c.getRg(),
                    c.getEndereco(),
                    c.getTelefone()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar clientes: " + e.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linhaSelecionada = tabelaClientes.getSelectedRow();
        if (linhaSelecionada >= 0) {
            int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
            try {
                clienteSelecionado = clienteDAO.buscarPorId(id);
                if (clienteSelecionado != null) {
                    txtNome.setText(clienteSelecionado.getNome());
                    txtCpf.setText(clienteSelecionado.getCpf());
                    txtRg.setText(clienteSelecionado.getRg());
                    txtEndereco.setText(clienteSelecionado.getEndereco());
                    txtTelefone.setText(clienteSelecionado.getTelefone());

                    btnSalvar.setEnabled(false);
                    btnEditar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar dados do cliente: " + ex.getMessage());
            }
        }
    }

    private void salvarCliente() {
        if (validarCampos()) {
            try {
                Cliente c = new Cliente();
                c.setNome(txtNome.getText().trim());
                c.setCpf(txtCpf.getText().trim());
                c.setRg(txtRg.getText().trim());
                c.setEndereco(txtEndereco.getText().trim());
                c.setTelefone(txtTelefone.getText().trim());

                clienteDAO.salvar(c);
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar cliente: " + ex.getMessage());
            }
        }
    }

    private void editarCliente() {
        if (clienteSelecionado != null && validarCampos()) {
            try {
                clienteSelecionado.setNome(txtNome.getText().trim());
                clienteSelecionado.setCpf(txtCpf.getText().trim());
                clienteSelecionado.setRg(txtRg.getText().trim());
                clienteSelecionado.setEndereco(txtEndereco.getText().trim());
                clienteSelecionado.setTelefone(txtTelefone.getText().trim());

                clienteDAO.salvar(clienteSelecionado);
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar cliente: " + ex.getMessage());
            }
        }
    }

    private void excluirCliente() {
        if (clienteSelecionado != null) {
            int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja excluir o cliente " + clienteSelecionado.getNome() + "?", 
                "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                
            if (confirmacao == JOptionPane.YES_OPTION) {
                try {
                    clienteDAO.excluir(clienteSelecionado.getId());
                    JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao excluir: " + ex.getMessage());
                }
            }
        }
    }

    private void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtRg.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        clienteSelecionado = null;

        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);

        atualizarTabela();
    }

    private boolean validarCampos() {
        if (txtNome.getText().trim().isEmpty() || txtCpf.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nome e CPF são campos obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
}