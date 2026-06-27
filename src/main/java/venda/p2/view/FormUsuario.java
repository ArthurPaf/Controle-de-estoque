package venda.p2.view;

import javax.swing.*;
import java.awt.*;
import venda.p2.controller.UsuarioController;
import venda.p2.model.Usuario;

public class FormUsuario extends JFrame {

    private JTextField txtNome, txtLogin;
    private JPasswordField txtSenha;
    private JComboBox<String> cbPerfil; // Seletor de ADMIN / OPERADOR
    private JButton btnSalvar;
    private UsuarioController usuarioController;

    public FormUsuario() {
        usuarioController = new UsuarioController();
        
        setTitle("Cadastro de Usuários");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNome = new JTextField(15);
        txtLogin = new JTextField(15);
        txtSenha = new JPasswordField(15);
        
        // ComboBox com as duas opções solicitadas
        cbPerfil = new JComboBox<>(new String[]{"OPERADOR", "ADMIN"});

        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Login:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Perfil de Acesso:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(cbPerfil, gbc);

        btnSalvar = new JButton("Salvar Usuário");
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnSalvar, gbc);

        // =====================================================================
        // TRAVA VISUAL DA TELA COM BASE NO LOGADO
        // =====================================================================
        Usuario logado = UsuarioController.getUsuarioLogado();
        if (logado == null || !logado.getPerfil().equalsIgnoreCase("ADMIN")) {
            // Se não for admin, desativa o botão de salvar e avisa na tela
            btnSalvar.setEnabled(false);
            JOptionPane.showMessageDialog(this, 
                "Atenção: Você não tem permissão de Administrador. Esta tela estará bloqueada para salvamento.", 
                "Aviso de Permissão", JOptionPane.WARNING_MESSAGE);
        }

        // Ação do Botão Salvar
        btnSalvar.addActionListener(e -> {
    try {
        Usuario novo = new Usuario();
        novo.setLogin(txtLogin.getText().trim());
        novo.setSenha(new String(txtSenha.getPassword()));
        
        // Pega o valor selecionado no JComboBox do Perfil (Ex: "ADMIN" ou "OPERADOR")
        novo.setPerfil(cbPerfil.getSelectedItem().toString()); 

        // Envia para o método do controller que criamos acima
        usuarioController.cadastrarNovoUsuario(novo);

        JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
        this.dispose();
    } catch (Exception ex) {
        // Se não for admin, a exceção cai aqui e avisa o operador
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Permissão", JOptionPane.ERROR_MESSAGE);
    }
});
    }
}