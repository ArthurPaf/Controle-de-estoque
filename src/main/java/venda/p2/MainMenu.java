package venda.p2;

import javax.swing.SwingUtilities;
import venda.p2.view.FormLogin;

public class MainMenu {
    public static void main(String[] args) {
        System.out.println("🚀 Inicializando o ecossistema gráfico da aplicação com segurança...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // CHAMADA CORRETA: Abre a tela de login primeiro
                FormLogin login = new FormLogin();
                login.setVisible(true);
            }
        });
    }
}