package venda.p2.controller;

import venda.p2.dao.VendaDAO;
import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;
import venda.p2.model.Produto;

public class VendaController {

    private VendaDAO vendaDAO = new VendaDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public String realizarVenda(Venda venda) {
        // RNF004: O Controller pergunta "quantas tem?" e toma a decisão (limite 3)
        // Usando o método que retorna int que sugerimos para o seu DAO
        int totalVendasNoMes = vendaDAO.contarVendasMesAtual(venda.getCliente().getId());

        if (totalVendasNoMes >= 3) {
            return "Erro: O cliente atingiu o limite de 3 vendas este mês (RNF004).";
        }

        for (VendaProduto item : venda.getVendaProdutos()) {
            Produto p = produtoDAO.pesquisar(item.getProduto().getId());

            // RNF003: Bloquear venda se estoque < 1
            if (p == null || p.getQuantidade() < 1) {
                return "Erro: Produto " + (p != null ? p.getNome() : "inválido") + " sem estoque disponível (RNF003).";
            }
            
            // Verificação extra de segurança: estoque suficiente para a venda
            if (item.getQuantidade() > p.getQuantidade()) {
                return "Erro: Estoque insuficiente para o produto " + p.getNome();
            }

            // RNF001: Preparar a nova quantidade (Controller calcula)
            double novaQuantidade = p.getQuantidade() - item.getQuantidade();
            p.setQuantidade(novaQuantidade);

            // RNF005: Atualizar valor_ultima_venda no objeto antes de salvar
            p.setValor_ultima_venda(item.getValorUnitario());

            // Manda o DAO apenas persistir a alteração no produto
            produtoDAO.alterar(p);
        }

        // Finalmente, salva a venda e seus itens através do DAO limpo
        if (vendaDAO.salvar(venda)) {
            return "Venda realizada com sucesso!";
        } else {
            return "Erro ao salvar a venda no banco de dados.";
        }
    }
}