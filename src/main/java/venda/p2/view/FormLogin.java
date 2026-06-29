package venda.p2.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import venda.p2.controller.UsuarioController;
import venda.p2.model.Usuario;

public class FormLogin extends JFrame {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar, btnSair;
    private UsuarioController usuarioController;

    public FormLogin() {
        usuarioController = new UsuarioController();

        setTitle("SisCom - Autenticação de Usuário");
        setSize(450, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        setResizable(false);

        JPanel painelPrincipal = new JPanel(new GridBagLayout());
        painelPrincipal.setBackground(new Color(245, 247, 250));
        painelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- TÍTULO PRINCIPAL ---
        JLabel lblTitulo = new JLabel("Acesso ao Sistema", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(44, 62, 80));
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 15, 0); 
        painelPrincipal.add(lblTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(6, 6, 6, 6); 

        // --- CAMPO LOGIN ---
        JLabel lblLogin = new JLabel("Usuário:");
        lblLogin.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 1;
        painelPrincipal.add(lblLogin, gbc);

        txtLogin = new JTextField(15);
        txtLogin.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        painelPrincipal.add(txtLogin, gbc);

        // --- CAMPO SENHA ---
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0; gbc.gridy = 2;
        painelPrincipal.add(lblSenha, gbc);

        txtSenha = new JPasswordField(15);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        painelPrincipal.add(txtSenha, gbc);

        // --- PAINEL DE BOTÕES ---
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        painelBotoes.setOpaque(false);

        btnSair = new JButton("Sair ❌");
        btnSair.setFont(new Font("Arial", Font.PLAIN, 13));

        btnEntrar = new JButton("Entrar 🔑");
        btnEntrar.setFont(new Font("Arial", Font.BOLD, 13));
        btnEntrar.setBackground(new Color(46, 204, 113)); 
        btnEntrar.setForeground(Color.WHITE);

        painelBotoes.add(btnSair);
        painelBotoes.add(btnEntrar);

        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 0, 0); 
        painelPrincipal.add(painelBotoes, gbc);

        add(painelPrincipal);

        
        // LISTENERS / AÇÕES
        
        // Ação do Botão Entrar
        btnEntrar.addActionListener(e -> efetuarLogin());

        txtSenha.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    efetuarLogin();
                }
            }
        });

        // Ação do Botão Sair
        btnSair.addActionListener(e -> System.exit(0));
    }

    private void efetuarLogin() {
    try {
        String login = txtLogin.getText();
        String senha = new String(txtSenha.getPassword());

        Usuario usuarioLogado = usuarioController.autenticar(login, senha);

        JOptionPane.showMessageDialog(this, 
            "Bem-vindo, " + usuarioLogado.getLogin() + "!\nPerfil: " + usuarioLogado.getPerfil(), 
            "Sucesso", 
            JOptionPane.INFORMATION_MESSAGE);

        new MenuPrincipal().setVisible(true); 

        this.dispose(); 

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
        txtSenha.setText("");
        txtSenha.requestFocus();
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormLogin().setVisible(true);
        });
    }
}