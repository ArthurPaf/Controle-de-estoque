package venda.p2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        // Configurações Básicas da Janela Principal
        setTitle("Sistema de Controle de Estoque e Vendas");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Fecha o programa inteiro ao fechar o menu
        setLocationRelativeTo(null); // Centraliza na tela
        setLayout(new BorderLayout());

        // 1. Cabeçalho da Tela
        JPanel painelCabecalho = new JPanel();
        painelCabecalho.setBackground(new Color(45, 52, 71)); // Azul escuro elegante
        painelCabecalho.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        
        JLabel lblTitulo = new JLabel("MÓDULOS DO SISTEMA");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        painelCabecalho.add(lblTitulo);
        
        add(painelCabecalho, BorderLayout.NORTH);

        // 2. Painel Central com os Botões (Grid Layout de 3 linhas e 2 colunas)
        JPanel painelBotoes = new JPanel(new GridLayout(3, 2, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Criando os botões de acesso
        JButton btnCategorias = new JButton("📦 Cadastrar Categorias");
        JButton btnProdutos = new JButton("🏷️ Gerenciar Produtos");
        JButton btnFornecedor = new JButton("🚚 Gerenciar Fornecedor");
        JButton btnClientes = new JButton("👥 Cadastro de Clientes");
        JButton btnTipoConta = new JButton("💳 Gerenciar Tipos de Conta");
        JButton btnFormaPagamento = new JButton("💳 Gerenciar Formas de Pagamento");
        JButton btnParcelas = new JButton("💳 Gerenciar Parcelas");
        JButton btnFinanceiro = new JButton("💰 Gerenciar Financeiro");
        JButton btnVendas = new JButton("🛒 Registrar Venda");
        JButton btnCompras = new JButton("📈 Registrar Compra");
        JButton btnSair = new JButton("❌ Sair do Sistema");

        // Estilizando os botões para não ficar com aquela cara padrão antiga do Windows
        JButton[] botoes = {btnCategorias, btnProdutos, btnFornecedor, btnClientes, btnTipoConta, btnFormaPagamento, btnParcelas, btnFinanceiro, btnVendas, btnCompras, btnSair};
        for (JButton btn : botoes) {
            btn.setFont(new Font("Arial", Font.PLAIN, 14));
            btn.setFocusPainted(false);
        }
        btnSair.setBackground(new Color(255, 102, 102)); // Destaca o botão sair em vermelho fosco
        btnSair.setForeground(Color.WHITE);

        // Adicionando os botões ao Grid
        painelBotoes.add(btnCategorias);
        painelBotoes.add(btnProdutos);
        painelBotoes.add(btnFornecedor);
        painelBotoes.add(btnClientes);
        painelBotoes.add(btnTipoConta);
        painelBotoes.add(btnFormaPagamento);
        painelBotoes.add(btnParcelas);
        painelBotoes.add(btnFinanceiro);
        painelBotoes.add(btnVendas);
        painelBotoes.add(btnCompras);
        painelBotoes.add(btnSair);

        add(painelBotoes, BorderLayout.CENTER);

        // 3. Rodapé informativo
        JPanel painelRodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelRodape.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
        JLabel lblStatus = new JLabel("Banco de Dados: PostgreSQL Conectado ✔");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 11));
        painelRodape.add(lblStatus);
        
        add(painelRodape, BorderLayout.SOUTH);

        // --- AÇÕES DOS BOTÕES ---

        // Abrir Tela de Categorias
        btnCategorias.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Instancia e abre a tela que criamos antes
                FormCategoria telaCategoria = new FormCategoria();
                telaCategoria.setVisible(true);
            }
        });

        // Abrir Tela de Produtos
        btnProdutos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormProduto telaProduto = new FormProduto();
                telaProduto.setVisible(true);
            }
        });

        // Abrir Tela de Fornecedores
        btnFornecedor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormFornecedor telaFornecedor = new FormFornecedor();
                telaFornecedor.setVisible(true);
            }
        });

        // Abrir Tela de Clientes
        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormCliente telaCliente = new FormCliente();
                telaCliente.setVisible(true);
            }
        });

        // Abrir Tela de Tipos de Conta
        btnTipoConta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormTipoConta telaTipoConta = new FormTipoConta();
                telaTipoConta.setVisible(true);
            }
        });

        // Abrir Tela de Formas de Pagamento
        btnFormaPagamento.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormFormaPagamento telaFormaPagamento = new FormFormaPagamento();
                telaFormaPagamento.setVisible(true);
            }
        });

        // Abrir Tela de Parcelas
        btnParcelas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                FormParcelas telaParcelas = new FormParcelas();
                telaParcelas.setVisible(true);
            }
        });


        btnFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                FormFinanceiro telaFinanceiro = new FormFinanceiro();
                telaFinanceiro.setVisible(true);
            }
        });

        btnVendas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormVenda telaVenda = new FormVenda();
                telaVenda.setVisible(true);
            }
        });


        btnCompras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormCompra telaCompra = new FormCompra();
                telaCompra.setVisible(true);
            }
        });

        // Fechar o sistema com segurança
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        
    }
}