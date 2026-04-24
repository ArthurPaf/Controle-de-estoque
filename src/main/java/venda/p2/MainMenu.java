package venda.p2;

import java.util.Scanner;
import java.time.LocalDate;
import venda.p2.controller.*;
import venda.p2.model.*;
import venda.p2.dao.*;

public class MainMenu {

    private static Scanner teclado = new Scanner(System.in);
    
    // Controllers (Para salvar/regras)
    private static CategoriaController catCtrl = new CategoriaController();
    private static ProdutoController prodCtrl = new ProdutoController();
    private static VendaController vendaCtrl = new VendaController();
    private static ClienteController cliCtrl = new ClienteController();
    private static FornecedorController forneCtrl = new FornecedorController();
    private static CompraController compraCtrl = new CompraController();
    
    
    // DAOs (Para pesquisar direto, já que o Controller não tem)
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
            System.out.println("[1] CATEGORIAS | [2] PRODUTOS | [3] CLIENTES | [4] FORNECEDORES | [5] COMPRAS | [0] SAIR");
            System.out.print("\nOpção: ");
            
            try {
                opcao = Integer.parseInt(teclado.nextLine());
                if (opcao == 1) abaCategorias();
                else if (opcao == 2) abaProdutos();
                else if (opcao == 3) abaClientes();
                else if (opcao == 4) abaFornecedores();
                else if (opcao == 5) abaCompras();

            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida.");
            }
        }
    }


    // Switch case da aba categoria


    public static void abaCategorias() {
    exibirHeader("GESTÃO DE CATEGORIAS (AUTO-ID)");
    System.out.println("1. Adicionar | 2. Alterar | 3. Excluir | 4. Listar/Buscar | 0. Voltar");
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
            System.out.println(catCtrl.salvar(cNova)); // O DAO fará o ID automático
            break;

        case 2: // ALTERAR
            System.out.print("ID da categoria que deseja alterar: ");
            int idAlt = Integer.parseInt(teclado.nextLine());
            Categoria cAlt = catDAO.pesquisar(idAlt); // Busca o dado atual no DAO
            
            if (cAlt != null) {
                System.out.print("Novo nome (Atual: " + cAlt.getNome() + "): ");
                cAlt.setNome(teclado.nextLine());
                System.out.println(catDAO.alterar(cAlt)); // Chama o método alterar
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
        exibirHeader("GESTÃO DE PRODUTOS (AUTO-ID)");
        System.out.println("1. Adicionar Novo | 2. Alterar Nome/Categoria | 3. Excluir | 4. Consultar Estoque | 0. Voltar");
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

                    // IMPORTANTE: Busca a categoria no DAO para associar
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

                case 4: // CONSULTAR (READ)
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
        System.out.println("1. Novo | 2. Alterar | 3. Excluir | 4. Buscar | 0. Voltar");
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
        System.out.println("1. Novo | 2. Alterar | 3. Excluir | 4. Buscar | 0. Voltar");
        System.out.print("Escolha: ");
        
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
        System.out.println("1. Registrar Nova Compra | 2. Consultar Compra | 3. Listar Compras | 0. Voltar");
        System.out.print("Escolha: ");
        
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

    // 1. Criamos a Compra e setamos o básico
    Compra c = new Compra();
    c.setFornecedor(forn);
    c.setDataCompra(LocalDate.now());
    c.setValorTotal(qtd * valorUnitario); // Calcula o total aqui

    // 2. Criamos o Item (CompraProduto)
    CompraProduto cp = new CompraProduto();
    cp.setProduto(prod);
    cp.setQuantidade(qtd);
    cp.setValorUnitario(valorUnitario);
    cp.setCompra(c); // Vincula o item à compra

    // 3. O PULO DO GATO: Adiciona o item na lista da compra
    // Se o método não existir, crie um: public void addItem(CompraProduto item) { this.lista.add(item); }
    c.getCompraProdutos().add(cp); 

    // Agora sim, manda pro Controller
    String resultado = compraCtrl.realizarCompra(c); 
    System.out.println(resultado);
    break;

                case 2: // CONSULTAR
                    System.out.print("ID da Compra: ");
                    int idBusca = Integer.parseInt(teclado.nextLine());
                    Compra comp = compraDAO.pesquisar(idBusca);
                    CompraProduto cpBusca = compraDAO.pesquisarCompraProduto(idBusca);
                    
                    if (comp != null) {
                        System.out.println("\n--- DETALHES DA COMPRA #" + comp.getId() + " ---");
                        System.out.println("Data:       " + comp.getDataCompra());
                        System.out.println("Fornecedor: " + comp.getFornecedor().getNomeFantasia());
                        System.out.println("Produto:    " + cpBusca.getProduto().getNome());
                        System.out.println("Quantidade: " + cpBusca.getQuantidade());
                        System.out.println("Preço Unit: R$ " + cpBusca.getValorUnitario());
                        System.out.println("Total:      R$ " + (cpBusca.getQuantidade() * cpBusca.getValorUnitario()));
                    } else {
                        System.out.println(">> Compra não encontrada.");
                    }
                    break;

                case 3: 
                    System.out.println(">> Funcionalidade de listagem em desenvolvimento...");
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
        System.out.println("1. Realizar Nova Venda | 2. Consultar Venda por ID | 3. Cancelar Venda | 0. Voltar");
        System.out.print("Escolha: ");
        
        try {
            op = Integer.parseInt(teclado.nextLine());
            
            switch (op) {
                case 1: // REALIZAR VENDA
                    System.out.println("\n--- Nova Venda ---");
                    System.out.print("ID do Cliente: ");
                    int idCli = Integer.parseInt(teclado.nextLine());
                    
                    // Valida se o cliente existe
                    Cliente cli = cliDAO.pesquisar(idCli);
                    if (cli == null) {
                        System.out.println(">> [!] Erro: Cliente não encontrado.");
                        break;
                    }

                    System.out.print("ID do Produto: ");
                    int idProd = Integer.parseInt(teclado.nextLine());
                    
                    // Valida se o produto existe
                    Produto prod = prodDAO.pesquisar(idProd);
                    if (prod == null) {
                        System.out.println(">> [!] Erro: Produto não encontrado.");
                        break;
                    }

                    System.out.print("Quantidade: ");
                    double qtd = Double.parseDouble(teclado.nextLine());

                    // Verifica se tem estoque
                    if (prod.getQuantidade() < qtd) {
                        System.out.println(">> [!] Erro: Estoque insuficiente (Atual: " + prod.getQuantidade() + ")");
                        break;
                    }

                    // Monta o objeto Venda
                    Venda v = new Venda();
                    VendaProduto vp = new VendaProduto();
                    v.setCliente(cli);
                    vp.setProduto(prod);
                    vp.setQuantidade(qtd);
                    v.setDataVenda(LocalDate.now()); // Data de hoje automática
                    v.setValorTotal(prod.getPreco() * qtd);

                    // Salva no banco (O Controller/DAO deve cuidar de baixar o estoque)
                    System.out.println(vendaCtrl.realizarVenda(v));
                    break;

                case 2: // CONSULTAR
                    System.out.print("Digite o ID da Venda: ");
                    int idVenda = Integer.parseInt(teclado.nextLine());
                    Venda vBusca = vendaDAO.pesquisar(idVenda);
                    VendaProduto vpBusca = vendaDAO.pesquisarVendaProduto(idVenda);
                    
                    if (vBusca != null) {
                        System.out.println("\n--- DETALHES DA VENDA #" + vBusca.getId() + " ---");
                        System.out.println("Data:    " + vBusca.getDataVenda());
                        System.out.println("Cliente: " + vBusca.getCliente().getNome());
                        System.out.println("Produto: " + vpBusca.getProduto().getNome());
                        System.out.println("Qtd:     " + vpBusca.getQuantidade());
                        System.out.println("Total:   R$ " + vBusca.getValorTotal());
                    } else {
                        System.out.println(">> Venda não encontrada.");
                    }
                    break;

                case 3: // CANCELAR/EXCLUIR
                    System.out.print("ID da Venda para cancelar: ");
                    int idExc = Integer.parseInt(teclado.nextLine());
                    System.out.print("Isso devolverá o estoque. Confirmar? (S/N): ");
                    if (teclado.nextLine().equalsIgnoreCase("S")) {
                        System.out.println(vendaDAO.excluir(idExc));
                    }
                    break;
            }
        } catch (Exception e) {
            System.out.println("Erro: Entrada inválida ou dados incompletos.");
        }
        
        if (op != 0) {
            System.out.println("\nPressione ENTER para continuar...");
            teclado.nextLine();
        }
    }
}

    public static void exibirHeader(String t) {
        System.out.println("\n\n====================================");
        System.out.println(" ARTHUR'S SYSTEM | " + t);
        System.out.println("====================================");
    }
}