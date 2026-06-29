package venda.p2.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.persistence.EntityManager;
import venda.p2.dao.VendaDAO;
import venda.p2.dao.GenericDAO;
import venda.p2.dao.ProdutoDAO;
import venda.p2.model.Venda;
import venda.p2.model.VendaProduto;
import venda.p2.model.Cliente;
import venda.p2.model.Produto;
import java.util.List;

public class VendaController {

    
    private static final Logger logger = LogManager.getLogger(VendaController.class);

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

    public List<Venda> consultarVendasComFiltros(java.time.LocalDate dataInicio, java.time.LocalDate dataFim, Integer idCliente) throws Exception {
        
        logger.info("Método consultarVendasComFiltros() executado.");

        if ((dataInicio != null && dataFim == null) || (dataInicio == null && dataFim != null)) {
            
            logger.warn("Tentativa de filtro com período incompleto.");
            throw new Exception("Para filtrar por período, você deve informar a Data Inicial E a Data Final.");
        }

       
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            java.time.LocalDate provisoria = dataInicio;
            dataInicio = dataFim;
            dataFim = provisoria;
        }

        
        return vendaDAO.listarVendasPorFiltros(dataInicio, dataFim, idCliente);
    }

    public List<VendaProduto> listarItensDaVenda(int idVenda) throws Exception {
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
            
            logger.warn("Tentativa de adicionar item ao carrinho com estoque insuficiente.");
            throw new Exception("Estoque insuficiente! Saldo atual: " + produto.getQuantidade());
        }

        VendaProduto item = new VendaProduto();
        item.setProduto(produto);
        item.setQuantidade(qtd);
        item.setValorUnitario(produto.getPreco());

        return item;
    }

    public void finalizarVenda(Cliente cliente, List<VendaProduto> carrinho) throws Exception {
        
        logger.info("Método finalizarVenda() executado.");

        if (cliente == null) {
            throw new Exception("Selecione um cliente válido.");
        }
        if (carrinho == null || carrinho.isEmpty()) {
            throw new Exception("Adicione pelo menos um produto para fechar a venda.");
        }

        
        String cpfNovoCli = cliente.getCpf().replaceAll("[^0-9]", "");
        
        
        java.time.LocalDate dataAtual = java.time.LocalDate.now();
        int mesAtual = dataAtual.getMonthValue();
        int anoAtual = dataAtual.getYear();

        
        List<Venda> historicoVendas = this.listarHistoricoVendas();
        long contagemVendasNoMes = 0;

        for (Venda v : historicoVendas) {
            if (v.getCliente() != null && v.getCliente().getCpf() != null) {
                String cpfHistorico = v.getCliente().getCpf().replaceAll("[^0-9]", "");
                java.time.LocalDate dataVendaHistorico = v.getDataVenda();

                
                if (cpfNovoCli.equals(cpfHistorico) && dataVendaHistorico != null 
                        && dataVendaHistorico.getMonthValue() == mesAtual 
                        && dataVendaHistorico.getYear() == anoAtual) {
                    
                    contagemVendasNoMes++;
                }
            }
        }

        
        if (contagemVendasNoMes >= 3) {
            logger.warn("Venda recusada. CPF: {} já atingiu o limite de {} compras neste mês.", cliente.getCpf(), contagemVendasNoMes);
            throw new Exception("Limite atingido! Este CPF já realizou " + contagemVendasNoMes + " compras no mês atual.");
        }
        

        try {
            
            Venda venda = new Venda();
            
            
            Cliente clienteBD = new GenericDAO<>(Cliente.class).buscarPorId(cliente.getId());
            if (clienteBD == null) {
                throw new Exception("Cliente não encontrado no banco de dados.");
            }
            venda.setCliente(clienteBD);
            
            
            venda.setDataVenda(java.time.LocalDate.now()); 

            
            double total = 0;
            for (VendaProduto item : carrinho) {
                total += item.getValorUnitario() * item.getQuantidade();
            }
            venda.setValorTotal(total);

            
            Venda vendaSalva = vendaDAO.salvarVenda(venda); 

            
            logger.info("Cabeçalho da venda salvo. ID: {}, Valor Total: {}", vendaSalva.getId(), total);

            
            for (VendaProduto item : carrinho) {
                item.setVenda(vendaSalva); 

                
                Produto prod = new GenericDAO<>(Produto.class).buscarPorId(item.getProduto().getId());
                if (prod == null) {
                    throw new Exception("Produto " + item.getProduto().getNome() + " não foi encontrado no banco.");
                }
                item.setProduto(prod); 

                
                vendaDAO.salvarItem(item);

                
                double estoqueAtualizado = prod.getQuantidade() - item.getQuantidade();
                prod.setQuantidade(estoqueAtualizado);
                
                
                new GenericDAO<>(Produto.class).salvar(prod);

                
                logger.info("Estoque atualizado para o produto ID: {}. Novo saldo: {}", prod.getId(), estoqueAtualizado);
            }
            
            
            logger.info("Venda ID: {} finalizada com sucesso.", vendaSalva.getId());

        } catch (Exception ex) {
            
            logger.error("Erro ao finalizar a venda: {}", ex.getMessage());
            throw ex;
        }
        
    }

}