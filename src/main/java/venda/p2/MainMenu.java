package venda.p2;

import javax.swing.SwingUtilities;
import venda.p2.view.MenuPrincipal;

public class MainMenu {
    public static void main(String[] args) {
        System.out.println("🚀 Inicializando o ecossistema gráfico da aplicação...");
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuPrincipal menu = new MenuPrincipal();
                menu.setVisible(true);
            }
        });
    }
}