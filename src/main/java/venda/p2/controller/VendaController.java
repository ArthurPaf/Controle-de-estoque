package venda.p2.controller;

// 1. IMPORTAÇÕES DOS LOGS ADICIONADAS
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

    // 2. DECLARAÇÃO DO LOGGER
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
        // LOG ADICIONADO
        logger.info("Método consultarVendasComFiltros() executado.");

        if ((dataInicio != null && dataFim == null) || (dataInicio == null && dataFim != null)) {
            // LOG ADICIONADO
            logger.warn("Tentativa de filtro com período incompleto.");
            throw new Exception("Para filtrar por período, você deve informar a Data Inicial E a Data Final.");
        }

        // --- BLINDAGEM COMPATÍVEL COM LOCALDATE ---
        // Se o usuário digitou ao contrário, o sistema inverte sozinho
        if (dataInicio != null && dataFim != null && dataInicio.isAfter(dataFim)) {
            java.time.LocalDate provisoria = dataInicio;
            dataInicio = dataFim;
            dataFim = provisoria;
        }

        // Envia as instâncias puras de LocalDate diretamente para o seu DAO atualizado
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
            // LOG ADICIONADO
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
        // LOG ADICIONADO
        logger.info("Método finalizarVenda() executado.");

        if (cliente == null) {
            throw new Exception("Selecione um cliente válido.");
        }
        if (carrinho == null || carrinho.isEmpty()) {
            throw new Exception("Adicione pelo menos um produto para fechar a venda.");
        }

        // =========================================================================
        // REGRA DE NEGÓCIO: TRAVA DE LIMITE DE 3 VENDAS POR CPF NO MESMO MÊS
        // =========================================================================
        // 1. Limpa o CPF removendo pontos e traços para garantir uma comparação perfeita
        String cpfNovoCli = cliente.getCpf().replaceAll("[^0-9]", "");
        
        // 2. Obtém os dados do período atual (mês e ano) utilizando LocalDate
        java.time.LocalDate dataAtual = java.time.LocalDate.now();
        int mesAtual = dataAtual.getMonthValue();
        int anoAtual = dataAtual.getYear();

        // 3. Varre o histórico de vendas para realizar a contagem em memória
        List<Venda> historicoVendas = this.listarHistoricoVendas();
        long contagemVendasNoMes = 0;

        for (Venda v : historicoVendas) {
            if (v.getCliente() != null && v.getCliente().getCpf() != null) {
                String cpfHistorico = v.getCliente().getCpf().replaceAll("[^0-9]", "");
                java.time.LocalDate dataVendaHistorico = v.getDataVenda();

                // Se o CPF bater e a venda ocorreu no mesmo mês e ano correntes
                if (cpfNovoCli.equals(cpfHistorico) && dataVendaHistorico != null 
                        && dataVendaHistorico.getMonthValue() == mesAtual 
                        && dataVendaHistorico.getYear() == anoAtual) {
                    
                    contagemVendasNoMes++;
                }
            }
        }

        // 4. Bloqueia imediatamente antes de qualquer persistência caso atinja o limite
        if (contagemVendasNoMes >= 3) {
            logger.warn("Venda recusada. CPF: {} já atingiu o limite de {} compras neste mês.", cliente.getCpf(), contagemVendasNoMes);
            throw new Exception("Limite atingido! Este CPF já realizou " + contagemVendasNoMes + " compras no mês atual.");
        }
        // =========================================================================

        try {
            // 1. Instancia a venda transiente
            Venda venda = new Venda();
            
            // GARANTIA 1: Garante que o cliente esteja atrelado ao contexto do Hibernate
            Cliente clienteBD = new GenericDAO<>(Cliente.class).buscarPorId(cliente.getId());
            if (clienteBD == null) {
                throw new Exception("Cliente não encontrado no banco de dados.");
            }
            venda.setCliente(clienteBD);
            
            // --- CORREÇÃO 1: INJETANDO A DATA ATUAL ---
            venda.setDataVenda(java.time.LocalDate.now()); 

            // --- CORREÇÃO 2: CALCULANDO O VALOR TOTAL DA VENDA ---
            double total = 0;
            for (VendaProduto item : carrinho) {
                total += item.getValorUnitario() * item.getQuantidade();
            }
            venda.setValorTotal(total);

            // 2. Salva e CAPTURA o objeto gerenciado pelo JPA
            Venda vendaSalva = vendaDAO.salvarVenda(venda); 

            // LOG ADICIONADO
            logger.info("Cabeçalho da venda salvo. ID: {}, Valor Total: {}", vendaSalva.getId(), total);

            // 3. Vincula os itens do carrinho à venda salva
            for (VendaProduto item : carrinho) {
                item.setVenda(vendaSalva); // <--- Garante que a FK venda_id não seja null

                // GARANTIA 2: Evita o erro de "null value in column produto_id"
                Produto prod = new GenericDAO<>(Produto.class).buscarPorId(item.getProduto().getId());
                if (prod == null) {
                    throw new Exception("Produto " + item.getProduto().getNome() + " não foi encontrado no banco.");
                }
                item.setProduto(prod); // Alinha o ID correto para a coluna produto_id

                // Salva o item da venda na tabela venda_produto
                vendaDAO.salvarItem(item);

                //  Atualização de estoque usando o objeto monitorado
                double estoqueAtualizado = prod.getQuantidade() - item.getQuantidade();
                prod.setQuantidade(estoqueAtualizado);
                
                // Persiste o estoque atualizado
                new GenericDAO<>(Produto.class).salvar(prod);

                // LOG ADICIONADO
                logger.info("Estoque atualizado para o produto ID: {}. Novo saldo: {}", prod.getId(), estoqueAtualizado);
            }
            
            // LOG ADICIONADO
            logger.info("Venda ID: {} finalizada com sucesso.", vendaSalva.getId());

        } catch (Exception ex) {
            // LOG ADICIONADO
            logger.error("Erro ao finalizar a venda: {}", ex.getMessage());
            throw ex;
        }
        
    }

}