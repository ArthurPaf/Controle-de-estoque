package venda.p2.controller;

import venda.p2.dao.CompraDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Produto;
import venda.p2.model.Fornecedor; // Importante: importando o modelo do Fornecedor
import java.util.List;

public class CompraController {

    private CompraDAO compraDAO;
    private GenericDAO<Produto> produtoDAO;

    public CompraController() {
        this.compraDAO = new CompraDAO();
        this.produtoDAO = new GenericDAO<>(Produto.class);
    }

    public List<Produto> listarProdutos() throws Exception {
        return produtoDAO.listarTodos();
    }

    // Método essencial para preencher o JComboBox na View
    public List<Fornecedor> listarFornecedores() throws Exception {
        return new GenericDAO<>(Fornecedor.class).listarTodos();
    }

    public List<Compra> listarHistoricoCompras() throws Exception {
        return compraDAO.listarTodasCompras();
    }

    public List<CompraProduto> listarItensDaCompra(int idCompra) throws Exception {
        return compraDAO.listarItensPorCompra(idCompra);
    }

    public CompraProduto criarItemCarrinho(Produto produto, String quantidadeStr, String precoCustoStr) throws Exception {
        if (produto == null) throw new Exception("Selecione um produto válido.");
        if (quantidadeStr == null || quantidadeStr.trim().isEmpty()) throw new Exception("Insira a quantidade.");
        if (precoCustoStr == null || precoCustoStr.trim().isEmpty()) throw new Exception("Insira o preço de custo.");

        double qtd;
        double precoCusto;
        try {
            qtd = Double.parseDouble(quantidadeStr.trim());
            precoCusto = Double.parseDouble(precoCustoStr.trim());
        } catch (NumberFormatException e) {
            throw new Exception("Valores numéricos inválidos.");
        }

        if (qtd <= 0 || precoCusto <= 0) throw new Exception("Os valores devem ser maiores que zero.");

        CompraProduto item = new CompraProduto();
        item.setProduto(produto);
        item.setQuantidade(qtd);
        item.setValorUnitario(precoCusto);

        return item;
    }

    // Ajustado para receber o Fornecedor selecionado na tela
    public void finalizarCompra(Fornecedor fornecedor, List<CompraProduto> carrinho) throws Exception {
    if (fornecedor == null) {
        throw new Exception("Selecione um fornecedor válido.");
    }
    if (carrinho == null || carrinho.isEmpty()) {
        throw new Exception("Adicione pelo menos um produto para fechar a compra.");
    }

    // 1. Cria e persiste a Compra mestre vinculando o fornecedor
    Compra compra = new Compra();
    
    // GARANTIA: Busca a instância oficial monitorada pelo Hibernate usando o ID do fornecedor
    Fornecedor fornecedorBD = new GenericDAO<>(Fornecedor.class).buscarPorId(fornecedor.getId());
    if (fornecedorBD == null) {
        throw new Exception("Fornecedor não encontrado no banco de dados.");
    }
    compra.setFornecedor(fornecedorBD); 
    
    // Define a data da compra (conforme o insert do banco exige 'dataCompra')
    // CORREÇÃO: Passa a data atual usando LocalDate
    compra.setDataCompra(java.time.LocalDate.now());

    // Calcula o valor total da nota
    double total = 0;
    for (CompraProduto item : carrinho) {
        total += item.getValorUnitario() * item.getQuantidade();
    }
    compra.setValorTotal(total);

    // Salva e recupera a instância com o ID gerado pelo banco
    Compra compraSalva = compraDAO.salvarCompra(compra);

    // 2. Varre os itens adicionando ao estoque e vinculando a FK
    for (CompraProduto item : carrinho) {
        item.setCompra(compraSalva);
        
        // GARANTIA: Da mesma forma que o fornecedor, certifique-se de que o Produto 
        // associado ao item é uma instância válida e monitorada do banco
        Produto prod = new GenericDAO<>(Produto.class).buscarPorId(item.getProduto().getId());
        item.setProduto(prod);

        compraDAO.salvarItem(item);

        // Regra de Negócio: Soma a quantidade comprada ao estoque atual
        double estoqueAtualizado = prod.getQuantidade() + item.getQuantidade();
        prod.setQuantidade(estoqueAtualizado);

        // Salva o produto atualizado
        new GenericDAO<>(Produto.class).salvar(prod);
    }
}
}