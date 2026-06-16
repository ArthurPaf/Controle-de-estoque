package venda.p2.controller;

import venda.p2.dao.VendaDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;
import venda.p2.model.Cliente;
import venda.p2.model.Produto;
import java.util.List;

public class VendaController {

    private VendaDAO vendaDAO;
    private ProdutoDAO produtoDAO;
    private GenericDAO<Cliente> clienteDAO;

    public VendaController() {
        this.vendaDAO = new VendaDAO();
        this.produtoDAO = new ProdutoDAO();
        this.clienteDAO = new GenericDAO<>(Cliente.class);
    }

    public List<Cliente> listarClientes() throws Exception {
        return clienteDAO.listarTodos();
    }

    public List<Produto> listarProdutos() throws Exception {
        return produtoDAO.listarTodos();
    }

    public List<Venda> listarHistoricoVendas() throws Exception {
    return vendaDAO.listarTodasVendas();
    }

    public List<VendaProduto> listarItensDaVenda(int idVenda) throws Exception {
    // Retorna os itens específicos de uma venda para mostrar na tabela detalhada
    return vendaDAO.listarItensPorVenda(idVenda);
    }

    public VendaProduto criarItemCarrinho(Produto produto, String quantidadeStr) throws Exception {
        if (produto == null) {
            throw new Exception("Selecione um produto válido.");
        }
        if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) {
            throw new Exception("Insira a quantidade do item.");
        }

        double qtd;
        try {
            qtd = Double.parseDouble(quantidadeStr.trim());
        } catch (NumberFormatException e) {
            throw new Exception("Quantidade inválida.");
        }

        if (qtd <= 0) {
            throw new Exception("A quantidade deve ser maior que zero.");
        }
        if (qtd > produto.getQuantidade()) {
            throw new Exception("Estoque insuficiente! Saldo atual: " + produto.getQuantidade());
        }

        VendaProduto item = new VendaProduto();
        item.setProduto(produto);
        item.setQuantidade(qtd);
        item.setValorUnitario(produto.getPreco());

        return item;
    }

    public void finalizarVenda(Cliente cliente, List<VendaProduto> carrinho) throws Exception {
    if (cliente == null) {
        throw new Exception("Selecione um cliente válido.");
    }
    if (carrinho == null || carrinho.isEmpty()) {
        throw new Exception("Adicione pelo menos um produto para fechar a venda.");
    }

    // 1. Instancia a venda transiente
    Venda venda = new Venda();
    
    // GARANTIA 1: Garante que o cliente esteja atrelado ao contexto do Hibernate
    Cliente clienteBD = new GenericDAO<>(Cliente.class).buscarPorId(cliente.getId());
    if (clienteBD == null) {
        throw new Exception("Cliente não encontrado no banco de dados.");
    }
    venda.setCliente(clienteBD);
    
    // Se a sua model Venda exigir uma data ou valor total, defina-os aqui:
    // venda.setDataVenda(java.time.LocalDate.now());

    // 2. Salva e CAPTURA o objeto gerenciado pelo JPA (que agora possui o ID do banco)
    Venda vendaSalva = vendaDAO.salvarVenda(venda); 

    // 3. Vincula os itens do carrinho à venda salva
    for (VendaProduto item : carrinho) {
        item.setVenda(vendaSalva); // <--- Garante que a FK venda_id não seja null

        // GARANTIA 2: Evita o erro de "null value in column produto_id"
        // Se o produto veio da View desanexado, reatamos ele ao EntityManager buscando pelo ID
        Produto prod = new GenericDAO<>(Produto.class).buscarPorId(item.getProduto().getId());
        if (prod == null) {
            throw new Exception("Produto " + item.getProduto().getNome() + " não foi encontrado no banco.");
        }
        item.setProduto(prod); // Alinha o ID correto para a coluna produto_id

        // Salva o item da venda na tabela venda_produto
        vendaDAO.salvarItem(item);

        // Atualização de estoque usando o objeto monitorado
        double estoqueAtualizado = prod.getQuantidade() - item.getQuantidade();
        prod.setQuantidade(estoqueAtualizado);
        
        // Persiste o estoque atualizado
        new GenericDAO<>(Produto.class).salvar(prod);
    }
}

}