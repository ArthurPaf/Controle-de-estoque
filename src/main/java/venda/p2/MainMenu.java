package venda.p2;

import java.util.Scanner;
import java.time.LocalDate;
import venda.p2.controller.*;
import venda.p2.model.*;
import venda.p2.dao.*;

public class MainMenu {

    private static Scanner teclado = new Scanner(System.in);
    
    // Controllers
    private static CategoriaController catCtrl = new CategoriaController();
    private static ProdutoController prodCtrl = new ProdutoController();
    private static VendaController vendaCtrl = new VendaController();
    private static ClienteController cliCtrl = new ClienteController();
    private static FornecedorController forneCtrl = new FornecedorController();
    private static CompraController compraCtrl = new CompraController();
    
    
    // DAOs
    private static CategoriaDAO catDAO = new CategoriaDAO();
    private static ProdutoDAO prodDAO = new ProdutoDAO();
    private static ClienteDAO cliDAO = new ClienteDAO();
    private static VendaDAO vendaDAO = new VendaDAO();
    private static FornecedorDAO forneDAO = new FornecedorDAO();
    private static CompraDAO compraDAO = new CompraDAO();

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            exibirHeader("MENU PRINCIPAL");
            System.out.println("[1] CATEGORIAS | [2] PRODUTOS | [3] CLIENTES | [4] FORNECEDORES | [5] COMPRAS | [6] VENDAS | [0] SAIR");
            System.out.print("\nOpção: ");
            
            try {
                opcao = Integer.parseInt(teclado.nextLine());
                if (opcao == 1) abaCategorias();
                else if (opcao == 2) abaProdutos();
                else if (opcao == 3) abaClientes();
                else if (opcao == 4) abaFornecedores();
                else if (opcao == 5) abaCompras();
                else if (opcao == 6) abaVendas();

            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida.");
            }
        }
    }


    // Switch case da aba categoria


    public static void abaCategorias() {
    exibirHeader("GESTÃO DE CATEGORIAS");
    System.out.println("1. Adicionar | 2. Alterar | 3. Excluir | 4. Buscar | 0. Voltar");
    System.out.print("Ação: ");
    int op = Integer.parseInt(teclado.nextLine());

    switch (op) {
        case 1: // ADICIONAR
            System.out.print("Nome da nova categoria: ");
            String nomeNovo = teclado.nextLine();
            System.out.print("ID da nova categoria: ");
            int id = Integer.parseInt(teclado.nextLine());
            Categoria cNova = new Categoria();
            cNova.setNome(nomeNovo);
            cNova.setId(id);
            System.out.println(catCtrl.salvar(cNova));
            break;

        case 2: // ALTERAR
            System.out.print("ID da categoria que deseja alterar: ");
            int idAlt = Integer.parseInt(teclado.nextLine());
            Categoria cAlt = catDAO.pesquisar(idAlt); 
            
            if (cAlt != null) {
                System.out.print("Novo nome (Atual: " + cAlt.getNome() + "): ");
                cAlt.setNome(teclado.nextLine());
                System.out.println(catDAO.alterar(cAlt));
            } else {
                System.out.println("Categoria não encontrada.");
            }
            break;

        case 3: // EXCLUIR
            System.out.print("ID da categoria para excluir: ");
            int idEx = Integer.parseInt(teclado.nextLine());
            System.out.print("Tem certeza? (S/N): ");
            if (teclado.nextLine().equalsIgnoreCase("S")) {
                System.out.println(catCtrl.excluir(idEx));
            }
            break;

        case 4: // BUSCAR
            System.out.print("ID para busca: ");
            int idBusca = Integer.parseInt(teclado.nextLine());
            Categoria res = catDAO.pesquisar(idBusca);
            if(res != null) System.out.println("ID: " + res.getId() + " | Nome: " + res.getNome());
            else System.out.println("Não encontrado.");
            break;
    }
    System.out.println("\nPressione ENTER...");
    teclado.nextLine();
}


      
    // Switch case da aba produto


    public static void abaProdutos() {
    int op = -1;
    while (op != 0) {
        exibirHeader("GESTÃO DE PRODUTOS");
        System.out.println("1. Adicionar | 2. Alterar | 3. Excluir | 4. Buscar | 0. Voltar");
        System.out.print("Ação: ");
        
        try {
            op = Integer.parseInt(teclado.nextLine());
            
            switch (op) {
                case 1: // ADICIONAR
                    System.out.println("\n--- Novo Produto ---");
                    System.out.print("Nome: ");
                    String nome = teclado.nextLine();
                    System.out.print("ID da Categoria: ");
                    int idCat = Integer.parseInt(teclado.nextLine());
                    System.out.print("ID da Produto: ");
                    int idProd = Integer.parseInt(teclado.nextLine());

                    Categoria cat = catDAO.pesquisar(idCat);
                    if (cat != null) {
                        Produto p = new Produto();
                        p.setNome(nome);
                        p.setId(idProd);
                        p.setCategoria(cat);
                        p.setQuantidade(0.0); // Estoque inicial zerado
                        p.setPreco(0.0);      // Preço inicial zerado
                        p.setValor_ultima_compra(0.0);
                        p.setValor_ultima_venda(0.0);
                        System.out.println(prodCtrl.salvar(p));
                    } else {
                        System.out.println(">> [ERRO] Categoria não encontrada!");
                    }
                    break;

                case 2: // ALTERAR
                    System.out.print("ID do Produto que deseja alterar: ");
                    int idAlt = Integer.parseInt(teclado.nextLine());
                    Produto pAlt = prodDAO.pesquisar(idAlt);
                    
                    if (pAlt != null) {
                        System.out.print("Novo Nome (Atual: " + pAlt.getNome() + "): ");
                        pAlt.setNome(teclado.nextLine());
                    
                    
                            System.out.println(prodDAO.alterar(pAlt));
                        }
                     else {
                        System.out.println(">> Produto não encontrado.");
                    }
                    break;

                case 3: // EXCLUIR
                    System.out.print("ID do Produto para excluir: ");
                    int idEx = Integer.parseInt(teclado.nextLine());
                    System.out.print("Confirmar exclusão? (S/N): ");
                    if (teclado.nextLine().equalsIgnoreCase("S")) {
                        System.out.println(prodCtrl.excluir(idEx));
                    }
                    break;

                case 4: // Pesquisar
                    System.out.print("ID do Produto: ");
                    int idBusca = Integer.parseInt(teclado.nextLine());
                    Produto res = prodDAO.pesquisar(idBusca);
                    if (res != null) {
                        System.out.println("\n--- INFO PRODUTO ---");
                        System.out.println("ID: " + res.getId());
                        System.out.println("Nome: " + res.getNome());
                        System.out.println("Categoria: " + res.getCategoria().getNome());
                        System.out.println("Estoque: " + res.getQuantidade());
                        System.out.println("Preço Médio: R$ " + res.getPreco());
                        System.out.println("Valor Última Compra: R$ " + res.getValor_ultima_compra());
                        System.out.println("Valor Última Venda: R$ " + res.getValor_ultima_venda());
                    } else {
                        System.out.println(">> Produto não encontrado.");
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro: Entrada inválida.");
        }
        
        if (op != 0) {
            System.out.println("\nPresione ENTER para continuar...");
            teclado.nextLine();
        }
    }
}


    // Switch case da aba Clientes


    public static void abaClientes() {
    int op = -1;
    while (op != 0) {
        exibirHeader("GESTÃO DE CLIENTES");
        System.out.println("1. Adicionar | 2. Alterar | 3. Excluir | 4. Buscar | 0. Voltar");
        System.out.print("Opção: ");
        
        try {
            op = Integer.parseInt(teclado.nextLine());
            switch (op) {
                case 1: // ADICIONAR
                    Cliente novo = new Cliente();
                    System.out.print("ID: "); novo.setId(Integer.parseInt(teclado.nextLine()));
                    System.out.print("Nome: "); novo.setNome(teclado.nextLine());
                    System.out.print("CPF: "); novo.setCpf(teclado.nextLine());
                    System.out.print("RG: "); novo.setRg(teclado.nextLine());
                    System.out.print("Fone: "); novo.setTelefone(teclado.nextLine());
                    System.out.print("Endereço: "); novo.setEndereco(teclado.nextLine());
                    System.out.println(cliCtrl.salvar(novo));
                    break;

                case 2: // ALTERAR
                    System.out.print("ID do Cliente: ");
                    int idAlt = Integer.parseInt(teclado.nextLine());
                    Cliente cAlt = cliDAO.pesquisar(idAlt);
                    if (cAlt != null) {
                        System.out.print("Novo Nome ("+cAlt.getNome()+"): ");
                        cAlt.setNome(teclado.nextLine());
                        System.out.print("Novo CPF ("+cAlt.getCpf()+"): ");
                        cAlt.setCpf(teclado.nextLine());
                        System.out.print("Novo RG ("+cAlt.getRg()+"): ");
                        cAlt.setRg(teclado.nextLine());
                        System.out.print("Novo Fone ("+cAlt.getTelefone()+"): ");
                        cAlt.setTelefone(teclado.nextLine());
                        System.out.print("Novo Endereço ("+cAlt.getEndereco()+"): ");
                        cAlt.setEndereco(teclado.nextLine());
                        System.out.println(cliDAO.alterar(cAlt));
                    } else System.out.println("Não encontrado.");
                    break;

                case 3: // EXCLUIR
                    System.out.print("ID para excluir: ");
                    int idEx = Integer.parseInt(teclado.nextLine());
                    System.out.println(cliDAO.excluir(idEx));
                    break;

                case 4: // BUSCAR
                    System.out.print("ID: ");
                    int idB = Integer.parseInt(teclado.nextLine());
                    Cliente res = cliDAO.pesquisar(idB);
                    if (res != null) {
                        System.out.println("\nNome: " + res.getNome() + " | CPF: " + res.getCpf() + " | RG: " + res.getRg() + " | Fone: " + res.getTelefone() + " | Endereço: " + res.getEndereco());
                    } else System.out.println("Cliente não existe.");
                    break;
            }
        } catch (Exception e) { System.out.println("Erro na entrada."); }
        
        if (op != 0) { System.out.println("\nENTER..."); teclado.nextLine(); }
    }
}


    // Switch case da aba Fornecedores

    public static void abaFornecedores() {
    int op = -1;
    while (op != 0) {
        exibirHeader("GESTÃO DE FORNECEDORES");
        System.out.println("1. Adicionar | 2. Alterar | 3. Excluir | 4. Buscar | 0. Voltar");
        System.out.print("Opção: ");
        
        try {
            op = Integer.parseInt(teclado.nextLine());
            switch (op) {
                case 1: // ADICIONAR
                    Fornecedor f = new Fornecedor();
                    System.out.print("ID: "); f.setId(Integer.parseInt(teclado.nextLine()));
                    System.out.print("Nome Fantasia: "); f.setNomeFantasia(teclado.nextLine());
                    System.out.print("CNPJ: "); f.setCnpj(teclado.nextLine());
                    System.out.print("Razão Social: "); f.setRazaoSocial(teclado.nextLine());
                    System.out.println(forneCtrl.salvar(f));
                    break;

                case 2: // ALTERAR
                    System.out.print("ID do Fornecedor: ");
                    int idAlt = Integer.parseInt(teclado.nextLine());
                    Fornecedor fAlt = forneDAO.pesquisar(idAlt);
                    if (fAlt != null) {
                        System.out.print("Novo Nome Fantasia (" + fAlt.getNomeFantasia() + "): ");
                        fAlt.setNomeFantasia(teclado.nextLine());
                        System.out.println(forneDAO.alterar(fAlt));
                    } else System.out.println("Fornecedor não encontrado.");
                    break;

                case 3: // EXCLUIR
                    System.out.print("ID para excluir: ");
                    int idEx = Integer.parseInt(teclado.nextLine());
                    System.out.println(forneDAO.excluir(idEx));
                    break;

                case 4: // BUSCAR
                    System.out.print("ID: ");
                    int idB = Integer.parseInt(teclado.nextLine());
                    Fornecedor res = forneDAO.pesquisar(idB);
                    if (res != null) {
                        System.out.println("\nFornecedor: " + res.getNomeFantasia());
                        System.out.println("CNPJ: " + res.getCnpj() + " | Razão Social: " + res.getRazaoSocial());
                    } else System.out.println("Não encontrado.");
                    break;
            }
        } catch (Exception e) { System.out.println("Erro na entrada."); }
        
        if (op != 0) { System.out.println("\nPressione ENTER..."); teclado.nextLine(); }
    }
}


    // Switch case da aba Compras

    public static void abaCompras() {
    int op = -1;
    while (op != 0) {
        exibirHeader("ENTRADA DE ESTOQUE (COMPRAS)");
        System.out.println("1. Realizar Compra | 2. Buscar | 3. Excluir | 0. Voltar");
        System.out.print("Opção: ");
        
        try {
            op = Integer.parseInt(teclado.nextLine());
            
            switch (op) {
                case 1: // Realizar compra
    System.out.println("\n--- Nova Entrada de Mercadoria ---");

    System.out.print("ID do Fornecedor: ");
    int idForn = Integer.parseInt(teclado.nextLine());
    Fornecedor forn = forneDAO.pesquisar(idForn);
    
    if (forn == null) {
        System.out.println(">> [!] Erro: Fornecedor não cadastrado.");
        break;
    }

    System.out.print("ID do Produto: ");
    int idProd = Integer.parseInt(teclado.nextLine());
    Produto prod = prodDAO.pesquisar(idProd);
    
    if (prod == null) {
        System.out.println(">> [!] Erro: Produto não encontrado.");
        break;
    }

    System.out.print("Quantidade Comprada: ");
    double qtd = Double.parseDouble(teclado.nextLine());
    
    System.out.print("Preço de Custo (Unitário): ");
    double valorUnitario = Double.parseDouble(teclado.nextLine());

    
    Compra c = new Compra();
    c.setFornecedor(forn);
    c.setDataCompra(LocalDate.now());
    

    
    CompraProduto cp = new CompraProduto();
    cp.setProduto(prod);
    cp.setQuantidade(qtd);
    cp.setValorUnitario(valorUnitario);
    cp.setCompra(c); 

    
    c.getCompraProdutos().add(cp); 

    
    String resultado = compraCtrl.realizarCompra(c); 
    System.out.println(">> " + resultado);
    break;
                case 2: // Consultar Compra
    System.out.print("Digite o ID da Compra: ");
    try {
        int idBusca = Integer.parseInt(teclado.nextLine());
        Compra cEnc = compraDAO.pesquisar(idBusca); 

        if (cEnc != null) {
            System.out.println("\n--- DETALHES DA COMPRA #" + cEnc.getId() + " ---");
            System.out.println("Data:       " + cEnc.getDataCompra());
            System.out.println("Fornecedor: " + cEnc.getFornecedor().getNomeFantasia());
            System.out.println("Valor Total: R$ " + cEnc.getValorTotal());
            System.out.println("------------------------------------");
            System.out.println("PRODUTO:");

           
            if (cEnc.getCompraProdutos() != null && !cEnc.getCompraProdutos().isEmpty()) {
                for (CompraProduto item : cEnc.getCompraProdutos()) {
                    System.out.printf("- %s | Qtd: %.2f | Unit: R$ %.2f\n",
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getValorUnitario());
                }
            } else {
                System.out.println(">> [!] Nenhum item encontrado para esta compra.");
            }
        } else {
            System.out.println(">> [!] Compra não encontrada.");
        }
    } catch (NumberFormatException e) {
        System.out.println(">> [!] Erro: Você precisa digitar um número válido para o ID.");
    } catch (Exception e) {
    
        System.out.println(">> [!] Erro inesperado: " + e.getMessage());
        e.printStackTrace(); 
    }
    break;

    case 3: // EXCLUIR
                    System.out.print("ID da Compra para excluir: ");
                    int idExc = Integer.parseInt(teclado.nextLine());
                    
                    
                    Compra cConfirma = compraDAO.pesquisar(idExc);
                    if (cConfirma == null) {
                        System.out.println(">> [!] Compra não encontrada.");
                        break;
                    }
                    
                
                    System.out.print("Confirmar exclusão? (S/N): ");
                    
                    if (teclado.nextLine().equalsIgnoreCase("S")) {
                        
                        System.out.println(compraDAO.excluir(idExc)); 
                    } else {
                        System.out.println(">> Operação abortada.");
                    }
                    break;

    
            }
        } catch (Exception e) {
            System.out.println("Erro: Verifique os dados digitados (IDs devem ser números).");
        }
        
        if (op != 0) {
            System.out.println("\nENTER para continuar...");
            teclado.nextLine();
        }
    }
}

    // Switch case aba de vendas

    public static void abaVendas() {
    int op = -1;
    while (op != 0) {
        exibirHeader("SISTEMA DE VENDAS (REGISTRO)");
        System.out.println("1. Realizar Venda | 2. Buscar | 3. Cancelar Venda | 0. Voltar");
        System.out.print("Escolha: ");
        
        try {
            String entrada = teclado.nextLine();
            if (entrada.isEmpty()) continue; 
            op = Integer.parseInt(entrada);
            
            switch (op) {
                case 1: // REALIZAR VENDA
    System.out.println("\n--- Nova Venda ---");
    System.out.print("ID do Cliente: ");
    int idCli = Integer.parseInt(teclado.nextLine());
    
    Cliente cli = cliDAO.pesquisar(idCli);
    if (cli == null) {
        System.out.println(">> [!] Erro: Cliente não encontrado.");
        break;
    }

    System.out.print("ID do Produto: ");
    int idProd = Integer.parseInt(teclado.nextLine());
    
    Produto prod = prodDAO.pesquisar(idProd);
    
    System.out.print("Quantidade: ");
    double qtd = Double.parseDouble(teclado.nextLine());

    

    
    System.out.print("Valor Unitário da Venda (Preço Sugerido: " + prod.getPreco() + "): ");
    double precoVenda = Double.parseDouble(teclado.nextLine());

    Venda v = new Venda();
    v.setCliente(cli);
    v.setDataVenda(LocalDate.now()); 
    v.setValorTotal(precoVenda * qtd);

    VendaProduto vp = new VendaProduto();
    vp.setProduto(prod);
    vp.setQuantidade(qtd);
    vp.setValorUnitario(precoVenda);
    
    v.getVendaProdutos().add(vp); 

    
    String resultado = vendaCtrl.realizarVenda(v);
    System.out.println(">> " + resultado);
    break;

                case 2: // CONSULTAR
                    System.out.print("Digite o ID da Venda: ");
                    int idVenda = Integer.parseInt(teclado.nextLine());
                    
                   
                    Venda vBusca = vendaDAO.pesquisar(idVenda);
                    
                    if (vBusca != null) {
                        System.out.println("\n--- DETALHES DA VENDA #" + vBusca.getId() + " ---");
                        System.out.println("Data:    " + vBusca.getDataVenda());
                        System.out.println("Cliente: " + vBusca.getCliente().getNome());
                        System.out.println("Total:   R$ " + vBusca.getValorTotal());
                        System.out.println("------------------------------------");
                        System.out.println("ITENS DA VENDA:");
                        
                        for (VendaProduto item : vBusca.getVendaProdutos()) {
                            System.out.printf("- %s | Qtd: %.2f | Unit: R$ %.2f\n",
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getValorUnitario());
                        }
                    } else {
                        System.out.println(">> [!] Venda não encontrada.");
                    }
                    break;

                case 3: // CANCELAR/EXCLUIR
                    System.out.print("ID da Venda para cancelar: ");
                    int idExc = Integer.parseInt(teclado.nextLine());
                    
                    
                    Venda vConfirma = vendaDAO.pesquisar(idExc);
                    if (vConfirma == null) {
                        System.out.println(">> [!] Venda não encontrada.");
                        break;
                    }
                    
                    System.out.println("Venda de R$ " + vConfirma.getValorTotal() + " para " + vConfirma.getCliente().getNome());
                    System.out.print("Confirmar cancelamento? (S/N): ");
                    
                    if (teclado.nextLine().equalsIgnoreCase("S")) {
                        
                        System.out.println(vendaDAO.excluir(idExc)); 
                    } else {
                        System.out.println(">> Operação abortada.");
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println(">> [!] Erro: Digite apenas números para IDs, quantidades e preços.");
        } catch (Exception e) {
            System.out.println(">> [!] Erro inesperado: " + e.getMessage());
            e.printStackTrace(); 
        }
        
        if (op != 0) {
            System.out.println("\nPressione ENTER para continuar...");
            teclado.nextLine();
        }
    }
}

    public static void exibirHeader(String t) {
        System.out.println("\n\n====================================");
        System.out.println(" Controle de Estoque | " + t);
        System.out.println("====================================");
    }
}