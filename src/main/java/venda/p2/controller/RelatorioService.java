package venda.p2.controller;

import jakarta.persistence.EntityManager;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import venda.p2.dao.GenericDAO;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import org.hibernate.Session;

public class RelatorioService {

    public void gerarRelatorio(String nomeRelatorio, Map<String, Object> parametros) throws Exception {
        EntityManager em = GenericDAO.getEntityManager();
        
        try {
            Session session = em.unwrap(Session.class);
            
            session.doReturningWork(new org.hibernate.jdbc.ReturningWork<Void>() {
                @Override
                public Void execute(Connection conexao) throws java.sql.SQLException {
                    try {
                        // 1. Busca o arquivo fonte .jrxml (XML) ao invés do compilado
                        String caminho = "/relatorios/" + nomeRelatorio + ".jrxml";
                        InputStream relatorioStream = getClass().getResourceAsStream(caminho);

                        if (relatorioStream == null) {
                            throw new Exception("O arquivo " + nomeRelatorio + ".jrxml não foi encontrado na pasta resources/relatorios/");
                        }

                        // 2. Compila o relatório dinamicamente na memória (evita erro de versão do Studio)
                        JasperReport jasperReport = JasperCompileManager.compileReport(relatorioStream);

                        // 3. Preenche o relatório com a conexão do banco e parâmetros
                        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametros, conexao);

                        // 4. Abre o visualizador Swing
                        JasperViewer viewer = new JasperViewer(jasperPrint, false);
                        viewer.setTitle("Visualização do Relatório Financeiro");
                        viewer.setLocationRelativeTo(null);
                        viewer.setVisible(true);

                    } catch (Exception e) {
                        throw new java.sql.SQLException("Erro interno no processamento do Jasper: " + e.getMessage(), e);
                    }
                    return null;
                }
            });

        } catch (Exception e) {
            throw new Exception("Erro ao gerar relatório com o Jasper: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}