package venda.p2.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import venda.p2.controller.TipoContaController;
import venda.p2.model.TipoConta;

public class FormTipoConta extends JFrame {

    private JTextField txtDescricao, txtPesquisa; // txtPesquisa adicionado
    private JButton btnSalvar, btnEditar, btnExcluir, btnLimpar, btnPesquisar; // btnPesquisar adicionado
    private JTable tabelaTipos;
    private DefaultTableModel modeloTabela;

    // View comunicando-se estritamente com a camada de controle
    private TipoContaController tipoContaController;
    private TipoConta tipoSelecionado;

    public FormTipoConta() {
        tipoContaController = new TipoContaController();

        setTitle("Gerenciar Tipos de Conta (CRUD)");
        setSize(550, 520); // Ajustado ligeiramente na altura para acomodar a barra de busca
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- PAINEL PRINCIPAL (Agrupa Formulário e Busca no Topo) ---
        JPanel painelTopo = new JPanel();
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));

        // --- 1. FORMULÁRIO DE CADASTRO ---
        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setBorder(BorderFactory.createTitledBorder("Dados do Tipo de Conta"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtDescricao = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0; painelCampos.add(new JLabel("Descrição do Tipo:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; painelCampos.add(txtDescricao, gbc);

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

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        painelCampos.add(painelAcoes, gbc);

        painelTopo.add(painelCampos);

        // --- NOVO: PAINEL DE PESQUISA ---
        JPanel painelBusca = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        painelBusca.setBorder(BorderFactory.createTitledBorder("Pesquisar Tipos de Conta"));
        txtPesquisa = new JTextField(20);
        btnPesquisar = new JButton("Pesquisar");

        painelBusca.add(new JLabel("Descrição:"));
        painelBusca.add(txtPesquisa);
        painelBusca.add(btnPesquisar);

        painelTopo.add(painelBusca);

        // Adiciona o bloco empilhado no topo da janela
        add(painelTopo, BorderLayout.NORTH);

        // --- 3. TABELA DE COMPONENTES ---
        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Descrição do Tipo de Conta"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tabelaTipos = new JTable(modeloTabela);
        JScrollPane scrollTabela = new JScrollPane(tabelaTipos);
        scrollTabela.setBorder(BorderFactory.createTitledBorder("Tipos Cadastrados"));
        add(scrollTabela, BorderLayout.CENTER);

        // --- 4. EVENTOS E LISTENERS ---

        // Evento de clique na tabela para carregar os campos de texto
        tabelaTipos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int linha = tabelaTipos.getSelectedRow();
                if (linha >= 0) {
                    int id = (int) modeloTabela.getValueAt(linha, 0);
                    try {
                        tipoSelecionado = tipoContaController.buscarPorId(id);
                        if (tipoSelecionado != null) {
                            txtDescricao.setText(tipoSelecionado.getDescricao());
                            
                            btnSalvar.setEnabled(false);
                            btnEditar.setEnabled(true);
                            btnExcluir.setEnabled(true);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FormTipoConta.this, "Erro ao carregar: " + ex.getMessage());
                    }
                }
            }
        });

        // Ação do Botão Salvar Novo
        btnSalvar.addActionListener(e -> {
            try {
                tipoContaController.salvarTipoConta(txtDescricao.getText().trim());
                JOptionPane.showMessageDialog(this, "Tipo de conta salvo com sucesso!");
                btnLimpar.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Ação do Botão Editar (Atualizar)
        btnEditar.addActionListener(e -> {
            if (tipoSelecionado != null) {
                try {
                    tipoContaController.atualizarTipoConta(tipoSelecionado, txtDescricao.getText().trim());
                    JOptionPane.showMessageDialog(this, "Tipo de conta atualizado!");
                    btnLimpar.doClick();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Ação do Botão Excluir
        btnExcluir.addActionListener(e -> {
            if (tipoSelecionado != null) {
                int conf = JOptionPane.showConfirmDialog(this, 
                    "Excluir tipo '" + tipoSelecionado.getDescricao() + "'?", 
                    "Confirmar", JOptionPane.YES_NO_OPTION);
                    
                if (conf == JOptionPane.YES_OPTION) {
                    try {
                        tipoContaController.excluirTipoConta(tipoSelecionado.getId());
                        JOptionPane.showMessageDialog(this, "Excluído com sucesso!");
                        btnLimpar.doClick();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erro: Este tipo pode estar sendo usado em alguma conta lançada.");
                    }
                }
            }
        });

        // NOVO: Ação do Botão Pesquisar
        btnPesquisar.addActionListener(e -> {
            String busca = txtPesquisa.getText().trim();
            try {
                List<TipoConta> resultado = tipoContaController.pesquisarPorDescricao(busca);
                atualizarTabela(resultado);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao pesquisar: " + ex.getMessage());
            }
        });

        // Ação do Botão Limpar / Atualização Gráfica do JTable
        btnLimpar.addActionListener(e -> {
            txtDescricao.setText("");
            txtPesquisa.setText(""); // limpa o campo de busca
            tipoSelecionado = null;
            
            btnSalvar.setEnabled(true);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);

            try {
                List<TipoConta> completa = tipoContaController.listarTodos();
                atualizarTabela(completa);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao listar: " + ex.getMessage());
            }
        });

        // Executa a primeira varredura gráfica para popular a tabela inicial
        btnLimpar.doClick();
    }

    // MÉTODO AUXILIAR PARA PREENCHER AS LINHAS DA TABELA
    private void atualizarTabela(List<TipoConta> lista) {
        modeloTabela.setRowCount(0);
        for (TipoConta tc : lista) {
            modeloTabela.addRow(new Object[]{tc.getId(), tc.getDescricao()});
        }
    }
}