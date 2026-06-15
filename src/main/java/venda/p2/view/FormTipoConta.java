package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.dao.GenericDAO;
import venda.p2.model.TipoConta;

public class FormTipoConta extends JFrame {

    private JTextField txtDescricao;
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar;
    private JTable tabelaTipos;
    private DefaultTableModel modeloTabela;

    private GenericDAO<TipoConta> tipoContaDAO;
    private TipoConta tipoSelecionado;

    public FormTipoConta() {
        tipoContaDAO = new GenericDAO<>(TipoConta.class);

        setTitle("Gerenciar Tipos de Conta (CRUD)");
        setSize(550, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- FORMULÁRIO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Tipo de Conta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDescricao = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Descrição do Tipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtDescricao, gbc);

        // --- BOTÕES ---
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

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        painelCampos.add(painelAcoes, gbc);

        add(painelCampos, BorderLayout.NORTH);

        // --- TABELA ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Descrição do Tipo de Conta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tabelaTipos = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaTipos);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Tipos Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        atualizarTabela();

        // --- EVENTOS ---
        tabelaTipos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) { preencherCamposPelaTabela(); }
        });

        btnSalvar.addActionListener(e -> salvarTipo());
        btnEditar.addActionListener(e -> editarTipo());
        btnExcluir.addActionListener(e -> excluirTipo());
        btnLimpar.addActionListener(e -> limparCampos());
    }

    private void atualizarTabela() {
        modeloTabela.setRowCount(0);
        try {
            List<TipoConta> lista = tipoContaDAO.listarTodos();
            for (TipoConta tc : lista) {
                modeloTabela.addRow(new Object[]{tc.getId(), tc.getDescricao()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao listar: " + e.getMessage());
        }
    }

    private void preencherCamposPelaTabela() {
        int linha = tabelaTipos.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modeloTabela.getValueAt(linha, 0);
            try {
                tipoSelecionado = tipoContaDAO.buscarPorId(id);
                if (tipoSelecionado != null) {
                    txtDescricao.setText(tipoSelecionado.getDescricao());
                    btnSalvar.setEnabled(false);
                    btnEditar.setEnabled(true);
                    btnExcluir.setEnabled(true);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage());
            }
        }
    }

    private void salvarTipo() {
        String desc = txtDescricao.getText().trim();
        if (desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "A descrição é obrigatória!");
            return;
        }
        try {
            TipoConta tc = new TipoConta();
            tc.setDescricao(desc);
            tipoContaDAO.salvar(tc);
            JOptionPane.showMessageDialog(this, "Tipo de conta salvo com sucesso!");
            limparCampos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage());
        }
    }

    private void editarTipo() {
        String desc = txtDescricao.getText().trim();
        if (tipoSelecionado != null && !desc.isEmpty()) {
            try {
                tipoSelecionado.setDescricao(desc);
                tipoContaDAO.salvar(tipoSelecionado);
                JOptionPane.showMessageDialog(this, "Tipo de conta atualizado!");
                limparCampos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage());
            }
        }
    }

    private void excluirTipo() {
        if (tipoSelecionado != null) {
            int conf = JOptionPane.showConfirmDialog(this, "Excluir tipo '" + tipoSelecionado.getDescricao() + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                try {
                    tipoContaDAO.excluir(tipoSelecionado.getId());
                    JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
                    limparCampos();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro: Este tipo pode estar sendo usado em alguma conta lançada.");
                }
            }
        }
    }

    private void limparCampos() {
        txtDescricao.setText("");
        tipoSelecionado = null;
        btnSalvar.setEnabled(true);
        btnEditar.setEnabled(false);
        btnExcluir.setEnabled(false);
        atualizarTabela();
    }
}