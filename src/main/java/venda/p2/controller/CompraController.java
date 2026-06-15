package venda.p2.controller;

import venda.p2.dao.GenericDAO;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Produto;
import java.util.List;

public class CompraController {
    // Substituídos os DAOs específicos pelo GenericDAO
    private GenericDAO<Compra> compraDAO = new GenericDAO<>(Compra.class);
    private GenericDAO<Produto> produtoDAO = new GenericDAO<>(Produto.class);
    
    public String realizarCompra(Compra compra) {
        double totalGeralCompra = 0; 

        try {
            for (CompraProduto item : compra.getCompraProdutos()) {
                // Busca o produto atualizado utilizando o GenericDAO
                Produto p = produtoDAO.buscarPorId(item.getProduto().getId());

                double valorNovaCompra = item.getQuantidade() * item.getValorUnitario();
                totalGeralCompra += valorNovaCompra; 

                double valorEstoqueAtual = p.getQuantidade() * p.getPreco();
                double novaQuantidadeTotal = p.getQuantidade() + item.getQuantidade();
                double novoPrecoMedio = (valorEstoqueAtual + valorNovaCompra) / novaQuantidadeTotal;

                p.setPreco(novoPrecoMedio);
                p.setQuantidade(novaQuantidadeTotal);
                p.setValor_ultima_compra(item.getValorUnitario());

                // O método salvar do GenericDAO (saveOrUpdate) cuidará da alteração
                produtoDAO.salvar(p);
            }

            compra.setValorTotal(totalGeralCompra); 

            // No Hibernate, passamos o objeto para salvar. Ele popula o ID automaticamente na própria instância
            compraDAO.salvar(compra);
            int idGerado = compra.getId();

            if (idGerado > 0) {
                return "Compra #" + idGerado + " realizada com sucesso! Estoque e preço médio atualizados.";
            } else {
                return "[!] Erro ao salvar a compra no banco de dados.";
            }
            
        } catch (Exception e) {
            return "[!] Erro ao processar a compra: " + e.getMessage();
        }
    }

    // Adicionado método útil caso precise listar o histórico de compras na View
    public List<Compra> listarTodas() {
        return compraDAO.listarTodos();
    }
}