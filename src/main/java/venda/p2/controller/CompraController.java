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
        double totalGeralCompra = 0; // 1. Criamos uma variável para somar tudo

    for (CompraProduto item : compra.getCompraProdutos()) {
        Produto p = produtoDAO.pesquisar(item.getProduto().getId());

        // ... SEU CÁLCULO DE PREÇO MÉDIO (está certinho) ...
        double valorNovaCompra = item.getQuantidade() * item.getValorUnitario();
        totalGeralCompra += valorNovaCompra; // 2. ACUMULAMOS o valor de cada item

        double valorEstoqueAtual = p.getQuantidade() * p.getPreco();
        double novaQuantidadeTotal = p.getQuantidade() + item.getQuantidade();
        double novoPrecoMedio = (valorEstoqueAtual + valorNovaCompra) / novaQuantidadeTotal;

        p.setPreco(novoPrecoMedio);
        p.setQuantidade(novaQuantidadeTotal);
        p.setValor_ultima_compra(item.getValorUnitario());

        produtoDAO.alterar(p);
    }

    // 3. O PULO DO GATO: Antes de mandar pro DAO, você seta o total na compra
    compra.setValorTotal(totalGeralCompra); 

    // 4. Agora o compra.getValorTotal() não será mais NULL!
    int idGerado = compraDAO.salvar(compra);

        if (idGerado > 0) {
            return "Compra #" + idGerado + " realizada com sucesso! Estoque e preço médio atualizados.";
        } else {
            return "[!] Erro ao salvar a compra no banco de dados.";
        }
}

}