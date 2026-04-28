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
        
        int totalVendasNoMes = vendaDAO.contarVendasMesAtual(venda.getCliente().getId());

        if (totalVendasNoMes >= 3) {
            return "Erro: O cliente atingiu o limite de 3 vendas este mês.";
        }

        for (VendaProduto item : venda.getVendaProdutos()) {
            Produto p = produtoDAO.pesquisar(item.getProduto().getId());

            if (p == null || p.getQuantidade() < 1) {
                return "Erro: Produto " + (p != null ? p.getNome() : "inválido") + " sem estoque disponível (RNF003).";
            }
            
            if (item.getQuantidade() > p.getQuantidade()) {
                return "Erro: Estoque insuficiente para o produto " + p.getNome();
            }

            double novaQuantidade = p.getQuantidade() - item.getQuantidade();
            p.setQuantidade(novaQuantidade);
            p.setValor_ultima_venda(item.getValorUnitario());

            produtoDAO.alterar(p);
        }

        
        int idGerado = vendaDAO.salvar(venda); 

        if (idGerado > 0) {
            return "Venda #" + idGerado + " realizada com sucesso!";
        } else {
            return "[!] Erro ao salvar a venda no banco de dados.";
        }
    }

}