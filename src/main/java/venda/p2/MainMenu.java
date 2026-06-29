package venda.p2;

import javax.swing.SwingUtilities;
import venda.p2.view.FormLogin;
import venda.p2.view.MenuPrincipal;

public class MainMenu {
    public static void main(String[] args) {
        System.out.println("🚀 Inicializando o ecossistema gráfico da aplicação com segurança...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                FormLogin login = new FormLogin(); 
                login.setVisible(true);

            }
        });
    }
}