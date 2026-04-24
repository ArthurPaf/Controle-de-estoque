package venda.p2;

import java.util.Scanner;
import java.time.LocalDate;
import venda.p2.controller.*;
import venda.p2.model.*;
import venda.p2.dao.*; // IMPORTANTE: Importar os DAOs agora

public class MainMenu {

    private static Scanner teclado = new Scanner(System.in);
    
    // Controllers (Para salvar/regras)
    private static CategoriaController catCtrl = new CategoriaController();
    private static ProdutoController prodCtrl = new ProdutoController();
    private static VendaController vendaCtrl = new VendaController();
    
    // DAOs (Para pesquisar direto, já que o Controller não tem)
    private static CategoriaDAO catDAO = new CategoriaDAO();
    private static ProdutoDAO prodDAO = new ProdutoDAO();

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            exibirHeader("MENU PRINCIPAL");
            System.out.println("[1] CATEGORIAS | [2] PRODUTOS | [3] VENDAS | [0] SAIR");
            System.out.print("\nOpção: ");
            
            try {
                opcao = Integer.parseInt(teclado.nextLine());
                if (opcao == 1) abaCategorias();
                else if (opcao == 2) abaProdutos();
                else if (opcao == 3) abaVendas();
            } catch (Exception e) {
                System.out.println("Erro: Entrada inválida.");
            }
        }
    }

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

    public static void abaVendas() {
        exibirHeader("ABA: VENDAS");
        System.out.print("ID Cliente: "); int idCli = Integer.parseInt(teclado.nextLine());
        System.out.print("ID Produto: "); int idPr = Integer.parseInt(teclado.nextLine());
        System.out.print("Quantidade: "); double qtd = Double.parseDouble(teclado.nextLine());

        Venda v = new Venda();
        v.setDataVenda(LocalDate.now());
        // Aqui você usaria o idCli e idPr para montar o objeto completo antes de enviar
        
        String status = vendaCtrl.realizarVenda(v); 
        System.out.println("Status da Operação: " + status);
        System.out.println("(Simulação: Cliente " + idCli + " comprando " + qtd + " unidades do Produto " + idPr + ")");
        
        System.out.println("\nENTER para voltar..."); teclado.nextLine();
    }

    public static void exibirHeader(String t) {
        System.out.println("\n\n====================================");
        System.out.println(" ARTHUR'S SYSTEM | " + t);
        System.out.println("====================================");
    }
}