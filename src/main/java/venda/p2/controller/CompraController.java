package venda.p2.controller;

import venda.p2.dao.CompraDAO;
import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Produto;

public class CompraController {
    private CompraDAO compraDAO = new CompraDAO();
    private ProdutoDAO produtoDAO = new ProdutoDAO();
    
    public String realizarCompra(Compra compra) {
        double totalGeralCompra = 0; 

    for (CompraProduto item : compra.getCompraProdutos()) {
        Produto p = produtoDAO.pesquisar(item.getProduto().getId());

        
        double valorNovaCompra = item.getQuantidade() * item.getValorUnitario();
        totalGeralCompra += valorNovaCompra; 

        double valorEstoqueAtual = p.getQuantidade() * p.getPreco();
        double novaQuantidadeTotal = p.getQuantidade() + item.getQuantidade();
        double novoPrecoMedio = (valorEstoqueAtual + valorNovaCompra) / novaQuantidadeTotal;

        p.setPreco(novoPrecoMedio);
        p.setQuantidade(novaQuantidadeTotal);
        p.setValor_ultima_compra(item.getValorUnitario());

        produtoDAO.alterar(p);
    }

    compra.setValorTotal(totalGeralCompra); 

    int idGerado = compraDAO.salvar(compra);

        if (idGerado > 0) {
            return "Compra #" + idGerado + " realizada com sucesso! Estoque e preço médio atualizados.";
        } else {
            return "[!] Erro ao salvar a compra no banco de dados.";
        }
}

}