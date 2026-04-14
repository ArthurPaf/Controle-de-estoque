package venda.p2.controller;

import venda.p2.dao.VendaDAO;
import venda.p2.model.Cliente;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;

public class VendaController {
    VendaDAO vendaDAO = new VendaDAO();
    ProdutoController produtoController = new ProdutoController();

    public boolean verificaQtdeVendas(Cliente cliente) {
        return vendaDAO.verificaQtdeVendas(cliente.getId());
    }

    public boolean salvar(Venda venda) {
        boolean verificaQtdeVendas = vendaDAO.verificaQtdeVendas(venda.getCliente().getId());
        if (!verificaQtdeVendas) { 
            return false;
        }

        for (VendaProduto vp : venda.getVendaProdutos()) {
            if (!produtoController.atualizarEstoque(vp.getProduto(), vp.getQuantidade())) {
                return false;
            }
        }

        return vendaDAO.salvar(venda);
    }
}
