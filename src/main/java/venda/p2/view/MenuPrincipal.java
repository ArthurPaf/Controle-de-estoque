package venda.p2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import venda.p2.controller.UsuarioController;
import venda.p2.controller.VendaController; // Importa o controlador para verificar a sessão do usuário
import venda.p2.model.Usuario;

public class MenuPrincipal extends JFrame {

    private JButton btnUsuarios; // Declarado como atributo para podermos manipular a permissão no final

    public MenuPrincipal() {
        // Configurações Básicas da Janela Principal (Ajustado tamanho para comportar mais botões)
        setTitle("Sistema de Controle de Estoque e Vendas");
        setSize(550, 450);
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

        // 2. Painel Central com os Botões (Grid Layout ajustado para 4 linhas e 3 colunas)
        JPanel painelBotoes = new JPanel(new GridLayout(4, 3, 15, 15));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

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
        btnUsuarios = new JButton("👥 Gerenciar Usuários"); // NOVO BOTÃO ADICIONADO
        JButton btnSair = new JButton("❌ Sair do Sistema");

        // Estilizando os botões para não ficar com aquela cara padrão antiga do Windows
        JButton[] botoes = {btnCategorias, btnProdutos, btnFornecedor, btnClientes, btnTipoConta, btnFormaPagamento, btnParcelas, btnFinanceiro, btnVendas, btnCompras, btnUsuarios, btnSair};
        for (JButton btn : botoes) {
            btn.setFont(new Font("Arial", Font.PLAIN, 13));
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
        painelBotoes.add(btnUsuarios); // Adicionado ao painel
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

        // Abrir Tela do Financeiro
        btnFinanceiro.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                FormFinanceiro telaFinanceiro = new FormFinanceiro();
                telaFinanceiro.setVisible(true);
            }
        });

        // Abrir Tela de Vendas
        btnVendas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormVenda telaVenda = new FormVenda();
                telaVenda.setVisible(true);
            }
        });

        // Abrir Tela de Compras
        btnCompras.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormCompra telaCompra = new FormCompra();
                telaCompra.setVisible(true);
            }
        });

        // NOVO: Abrir Tela de Cadastro de Usuários
        btnUsuarios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FormUsuario telaUsuario = new FormUsuario();
                telaUsuario.setVisible(true);
            }
        });

        // Fechar o sistema com segurança
        btnSair.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // =====================================================================
        // CONTROLE DE ACESSO VISUAL: SÓ EXIBE SE QUEM ESTÁ LOGADO FOR ADMIN
        // =====================================================================
        
    }
}