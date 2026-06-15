package venda.p2.controller;

import org.hibernate.Session;
import org.hibernate.query.Query;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;
import venda.p2.model.Produto;
import venda.p2.util.JPAUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class VendaController {

    private GenericDAO<Venda> vendaDAO = new GenericDAO<>(Venda.class);
    private GenericDAO<Produto> produtoDAO = new GenericDAO<>(Produto.class);

    public String realizarVenda(Venda venda) {
        try {
            // Regra de Negócio: Limite de 3 vendas no mês atual por cliente
            int totalVendasNoMes = contarVendasMesAtual(venda.getCliente().getId());

            if (totalVendasNoMes >= 3) {
                return "Erro: O cliente atingiu o limite de 3 vendas este mês.";
            }

            // Loop de validação e baixa de estoque dos itens da venda
            for (VendaProduto item : venda.getVendaProdutos()) {
                Produto p = produtoDAO.buscarPorId(item.getProduto().getId());

                if (p == null || p.getQuantidade() < 1) {
                    return "Erro: Produto " + (p != null ? p.getNome() : "inválido") + " sem estoque disponível (RNF003).";
                }
                
                if (item.getQuantidade() > p.getQuantidade()) {
                    return "Erro: Estoque insuficiente para o produto " + p.getNome();
                }

                // Deduz a quantidade vendida e salva o histórico do valor do item
                double novaQuantidade = p.getQuantidade() - item.getQuantidade();
                p.setQuantidade(novaQuantidade);
                p.setValor_ultima_venda(item.getValorUnitario());

                // O método salvar do GenericDAO faz o papel de dar o update na tabela produto
                produtoDAO.salvar(p);
            }

            // Salva a venda mestre (o Hibernate cuida de gerar o ID e salvar a lista cascateada se mapeada com CascadeType.ALL)
            vendaDAO.salvar(venda); 
            int idGerado = venda.getId();

            if (idGerado > 0) {
                return "Venda #" + idGerado + " realizada com sucesso!";
            } else {
                return "[!] Erro ao salvar a venda no banco de dados.";
            }
        } catch (Exception e) {
            return "[!] Erro ao processar a venda: " + e.getMessage();
        }
    }

    // Método substituto da antiga VendaDAO para contar as vendas usando Hibernate/HQL de forma direta
    // Método atualizado para usar EntityManager do JPA
    private int contarVendasMesAtual(int clienteId) {
        // Pega o EntityManager direto do nosso GenericDAO estruturado acima
        jakarta.persistence.EntityManager em = venda.p2.dao.GenericDAO.getEntityManager();
        try {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.set(java.util.Calendar.DAY_OF_MONTH, 1);
            java.util.Date inicioMes = cal.getTime();
            
            cal.set(java.util.Calendar.DAY_OF_MONTH, cal.getActualMaximum(java.util.Calendar.DAY_OF_MONTH));
            java.util.Date fimMes = cal.getTime();

            String hql = "select count(v) from Venda v where v.cliente.id = :clienteId and v.data between :inicioMes and :fimMes";
            jakarta.persistence.Query query = em.createQuery(hql);
            query.setParameter("clienteId", clienteId);
            query.setParameter("inicioMes", inicioMes);
            query.setParameter("fimMes", fimMes);

            return ((Long) query.getSingleResult()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            em.close(); // Sempre fechar o EntityManager para não vazar conexão
        }
    }

    // Método útil para alimentar histórico de vendas nas JTables das Views
    public List<Venda> listarTodas() {
        return vendaDAO.listarTodos();
    }
}