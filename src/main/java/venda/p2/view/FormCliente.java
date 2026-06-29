package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.ClienteController;
import venda.p2.model.Cliente;

public class FormCliente extends JFrame {

    private JTextField txtNome, txtCpf, txtRg, txtEndereco, txtTelefone, txtPesquisa; 
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar, btnPesquisar; 
    private JTable tabelaClientes;
    private DefaultTableModel modeloTabela;

    
    private ClienteController clienteController;
    private Cliente clienteSelecionado;

    public FormCliente() {
        clienteController = new ClienteController();

        setTitle("Gerenciar Clientes (CRUD)");
        setSize(750, 600); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PAINEL PRINCIPAL ---
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        // --- 1. FORMULÁRIO DE CADASTRO ---
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

        painelTopo.add(painelCampos);

        // --- PAINEL DE PESQUISA ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Pesquisar Clientes"));
        txtPesquisa = new JTextField(30);
        btnPesquisar = new JButton("Pesquisar");

        painelBusca.add(new JLabel("Nome do Cliente:"));
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);

        painelTopo.add(painelBusca);

        
        add(painelTopo, BorderLayout.NORTH);

        // --- 3. TABELA DE CLIENTES ---
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

        // --- 4. EVENTOS E AÇÕES DOS BOTÕES ---

        
        tabelaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linhaSelecionada = tabelaClientes.getSelectedRow();
                if (linhaSelecionada >= 0) {
                    int id = (int) modeloTabela.getValueAt(linhaSelecionada, 0);
                    try {
                        clienteSelecionado = clienteController.buscarPorId(id);
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
                        JOptionPane.showMessageDialog(FormCliente.this, "Erro ao carregar dados: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Salvar 
        btnSalvar.addActionListener(e -> {
            try {
                Cliente c = new Cliente();
                c.setNome(txtNome.getText().trim());
                c.setCpf(txtCpf.getText().trim());
                c.setRg(txtRg.getText().trim());
                c.setEndereco(txtEndereco.getText().trim());
                c.setTelefone(txtTelefone.getText().trim());

                clienteController.salvarCliente(c);
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        // Ação do Botão Editar / Atualizar
        btnEditar.addActionListener(e -> {
            if (clienteSelecionado != null) {
                try {
                    clienteSelecionado.setNome(txtNome.getText().trim());
                    clienteSelecionado.setCpf(txtCpf.getText().trim());
                    clienteSelecionado.setRg(txtRg.getText().trim());
                    clienteSelecionado.setEndereco(txtEndereco.getText().trim());
                    clienteSelecionado.setTelefone(txtTelefone.getText().trim());

                    clienteController.salvarCliente(clienteSelecionado);
                    JOptionPane.showMessageDialog(this, "Cliente updated com sucesso!");
                    btnLimpar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            }
        });

        // Ação do Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (clienteSelecionado != null) {
                int confirmacao = JOptionPane.showConfirmDialog(this, 
                    "Tem certeza que deseja excluir o cliente " + clienteSelecionado.getNome() + "?", 
                    "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
                    
                if (confirmacao == JOptionPane.YES_OPTION) {
                    try {
                        clienteController.excluirCliente(clienteSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Cliente excluído com sucesso!");
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
                
                List<Cliente> resultado = clienteController.pesquisarPorNome(busca);
                atualizarTabela(resultado);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage());
            }
        });

        // Ação do Botão Limpar
        btnLimpar.addActionListener(e -> {
            txtNome.setText("");
            txtCpf.setText("");
            txtRg.setText("");
            txtEndereco.setText("");
            txtTelefone.setText("");
            txtPesquisa.setText(""); 
            clienteSelecionado = null;

            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            try {
                List<Cliente> completa = clienteController.listarClientes();
                atualizarTabela(completa);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar clientes: " + ex.getMessage());
            }
        });

        
        btnLimpar.doClick();
    }

    
    private void atualizarTabela(List<Cliente> lista) {
        modeloTabela.setRowCount(0);
        for (Cliente c : lista) {
            modeloTabela.addRow(new Object[]{
                c.getId(), 
                c.getNome(), 
                c.getCpf(), 
                c.getRg(), 
                c.getEndereco(), 
                c.getTelefone()
            });
        }
    }
}