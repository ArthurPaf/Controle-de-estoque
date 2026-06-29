package venda.p2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import venda.p2.dao.CompraDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.model.Compra;
import venda.p2.model.CompraProduto;
import venda.p2.model.Produto;
import venda.p2.model.Fornecedor;
import java.time.LocalDate;
import java.util.List;

public class CompraController {

    
    private static final Logger logger = LogManager.getLogger(CompraController.class);

    private CompraDAO compraDAO;
    private GenericDAO<Produto> produtoDAO;

    public CompraController() {
        this.compraDAO = new CompraDAO();
        this.produtoDAO = new GenericDAO<>(Produto.class);
    }

    public List<Produto> listarProdutos() throws Exception {
        return produtoDAO.listarTodos();
    }

    public List<Fornecedor> listarFornecedores() throws Exception {
        return new GenericDAO<>(Fornecedor.class).listarTodos();
    }

    public List<Compra> listarHistoricoCompras() throws Exception {
        return compraDAO.listarTodasCompras();
    }

    public List<CompraProduto> listarItensDaCompra(int idCompra) throws Exception {
        return compraDAO.listarItensPorCompra(idCompra);
    }

    
    public List<Compra> consultarComprasComFiltros(LocalDate dataInicio, LocalDate dataFim, Integer idFornecedor) throws Exception {
        
        logger.info("Método consultarComprasComFiltros() executado.");
        return compraDAO.consultarComprasComFiltros(dataInicio, dataFim, idFornecedor);
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
            
            logger.warn("Tentativa de adicionar item com valores numéricos inválidos na Compra.");
            throw new Exception("Valores numéricos inválidos.");
        }

        if (qtd <= 0 || precoCusto <= 0) throw new Exception("Os valores devem ser maiores que zero.");

        CompraProduto item = new CompraProduto();
        item.setProduto(produto);
        item.setQuantidade(qtd);
        item.setValorUnitario(precoCusto);

        return item;
    }

    public void finalizarCompra(Fornecedor fornecedor, List<CompraProduto> carrinho) throws Exception {
        
        logger.info("Método finalizarCompra() executado.");

        if (fornecedor == null) {
            throw new Exception("Selecione um fornecedor válido.");
        }
        if (carrinho == null || carrinho.isEmpty()) {
            throw new Exception("Adicione pelo menos um produto para fechar a compra.");
        }

        try {
            Compra compra = new Compra();
            
            Fornecedor fornecedorBD = new GenericDAO<>(Fornecedor.class).buscarPorId(fornecedor.getId());
            if (fornecedorBD == null) {
                throw new Exception("Fornecedor não encontrado no banco de dados.");
            }
            compra.setFornecedor(fornecedorBD); 
            compra.setDataCompra(java.time.LocalDate.now());

            double total = 0;
            for (CompraProduto item : carrinho) {
                total += item.getValorUnitario() * item.getQuantidade();
            }
            compra.setValorTotal(total);

            Compra compraSalva = compraDAO.salvarCompra(compra);

            
            logger.info("Cabeçalho da compra salvo. ID: {}, Valor Total: {}", compraSalva.getId(), total);

            for (CompraProduto item : carrinho) {
                item.setCompra(compraSalva);
                
                Produto prod = new GenericDAO<>(Produto.class).buscarPorId(item.getProduto().getId());
                item.setProduto(prod);

                compraDAO.salvarItem(item);

                double estoqueAtualizado = prod.getQuantidade() + item.getQuantidade();
                prod.setQuantidade(estoqueAtualizado);

                new GenericDAO<>(Produto.class).salvar(prod);

                
                logger.info("Estoque atualizado (entrada) para o produto ID: {}. Novo saldo: {}", prod.getId(), estoqueAtualizado);
            }
            
            
            logger.info("Compra ID: {} finalizada com sucesso.", compraSalva.getId());

        } catch (Exception ex) {
            
            logger.error("Erro ao finalizar a compra: {}", ex.getMessage());
            throw ex;
        }
    }
}