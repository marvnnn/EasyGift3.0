package Menu;

import java.util.Scanner;

import Entidades.Lista;
import Entidades.ListaProduto;
import Entidades.Produto;
import aed3.ArvoreBMais;
import aed3.HashExtensivel;
import aed3.ParIDGTIN;
import aed3.ParUsuarioLista;
import Arquivos.ArquivoLista;
import Arquivos.ArquivoListaProduto;
import Arquivos.ArquivoProduto;
import Arquivos.ArquivoUsuario;

public class MenuProduto {
    public Scanner console;
    public ArquivoProduto arqProduto;
    public ArquivoUsuario arqUsu;
    public ArquivoListaProduto arqListaProduto;
    public ArquivoLista arqList;
    public HashExtensivel<ParIDGTIN> iCode;
    public ArvoreBMais<ParUsuarioLista> arvoreLista;

    public MenuProduto() throws Exception {
        arqUsu = new ArquivoUsuario();
        arqList = new ArquivoLista();
        arqListaProduto = new ArquivoListaProduto(); // ✅ inicializa aqui
        arqProduto = new ArquivoProduto(); // Inicializando o arquivo de produtos
        console = new Scanner(System.in);

        iCode = new HashExtensivel<>(ParIDGTIN.class.getConstructor(),
                4,
                ".\\Dados\\produto\\produtoCodigo.d.db",
                ".\\Dados\\produto\\produtoCodigo.c.db");

        arvoreLista = new ArvoreBMais<>(ParUsuarioLista.class.getConstructor(),
                4,
                ".\\Dados\\produto\\arvore_lista_produto.db");
    }

    public void menu(int idUsuario) throws Exception {
        console = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n\nEasyGift 2.0");
            System.out.println("---------");
            System.out.println("> Produtos - Autenticado");
            System.out.println("\n0 - Voltar");
            System.out.println("1 - Buscar produtos por GTIN");
            System.out.println("2 - Listar todos os Produtos");
            System.out.println("3 - Cadastrar um novo Produto");
            System.out.print("\nOpção: ");

            opcao = console.nextInt();
            int id;

            switch (opcao) {
                case 0:
                    System.out.println("Voltando...");
                    break;
                case 1:
                    buscarProduto(idUsuario);
                    break;
                case 2:
                    verProduto(listarProdutos(idUsuario), idUsuario); 
                    break;
                case 3:
                    cadastrarProduto(idUsuario);
                    break;
                default:
                    break;
            }
        } while (opcao != 0);
    }

    public void cadastrarProduto(int idUsuario) throws Exception {
        System.out.println("\n\n\n---------");
        System.out.println("> Produtos - Cadastro de Novo Produto");

        System.out.print("GTIN-13: ");
        console.nextLine(); // consumir enter pendente
        String gtin = console.nextLine();

        System.out.print("Nome do Produto: ");
        String nome = console.nextLine();

        System.out.print("Descrição do Produto: ");
        String descricao = console.nextLine();

        Produto produto = new Produto(gtin, nome, descricao);
        int id = arqProduto.create(produto);
        iCode.create(new ParIDGTIN(produto.getGtin13(), id));

        // Se tiver índice, você pode criar aqui
        // iCode.create(new ParIDGTIN(produto.getCodigoCompartilhavel(), id));
        // arvoreProduto.create(new ParUsuarioProduto(idUsuario, id));

        System.out.println("\n✅ Produto cadastrado com sucesso! (ID = " + id + ")");
    }

    public int listarProdutos(int idUsuario) throws Exception {
        System.out.println("\n\n---------");
        System.out.println("> Produtos - Listagem de Todos os Produtos");
        System.out.println("---------");

        int index = 1; // contador para exibição numerada
        int escolhido = -1;

        for (int i = 1; i <= arqProduto.tamanho(); i++) { // IDs começam em 1
            Produto p = arqProduto.read(i);
            if (p != null) {
                if(p.isAtivo()) {
                    System.out.println("(" + index + ") " + p.getNome() + " - " + p.getDescricao());
                }
                else {
                    System.out.println("(" + index + ") " + p.getNome() + " - " + p.getDescricao() + " - (INATIVO)");
                }
                index++;
            }
        }

        if (index == 1) { // não encontrou nenhum produto
            System.out.println("Nenhum produto cadastrado.");
            return -1;
        }

        System.out.print("Digite o número do produto desejado: ");
        int opcao = console.nextInt();

        if (opcao >= 1 && opcao < index) {
            // Encontrar o ID real do produto selecionado
            int count = 0;
            for (int i = 1; i <= arqProduto.tamanho(); i++) {
                Produto p = arqProduto.read(i);
                if (p != null) {
                    count++;
                    if (count == opcao) {
                        escolhido = i; // ID real do produto
                        break;
                    }
                }
            }
        } else {
            System.out.println("Opção inválida.");
        }

        return escolhido;
    }

    // Ver detalhes de um produto
    public void verProduto(int idProduto, int idUsuario) throws Exception {
        Produto produto = arqProduto.read(idProduto);
        if (produto != null) {
            System.out.println("\nEasy Gift 2.0");
            System.out.println("-----------------");
            System.out.println("> Início > Produtos > Listagem > " + produto.getNome() + "\n");

            System.out.println("NOME.......: " + produto.getNome());
            System.out.println("GTIN-13....: " + produto.getGtin13());
            System.out.println("DESCRIÇÃO..: " + produto.getDescricao());
            System.out.println();

            int listasUsuario = 0;
            int listasOutros = 0;

            try {
                // Percorre o arquivo ListaProduto manualmente
                arqListaProduto.arquivo.seek(12); // pula cabeçalho
                System.out.println("Aparece nas minhas listas:");
                while (arqListaProduto.arquivo.getFilePointer() < arqListaProduto.arquivo.length()) {
                    long pos = arqListaProduto.arquivo.getFilePointer();
                    byte lapide = arqListaProduto.arquivo.readByte();
                    short tam = arqListaProduto.arquivo.readShort();

                    if (lapide == ' ') {
                        byte[] ba = new byte[tam];
                        arqListaProduto.arquivo.read(ba);

                        ListaProduto lp = new ListaProduto();
                        lp.fromByteArray(ba);

                        if (lp.getIdProduto() == idProduto) {
                            Lista lista = arqList.read(lp.getIdLIsta()); // arqList = arquivo de Listas
                            if (lista != null) {
                                if (lista.getIdUsuario() == idUsuario) {
                                    System.out.println("- " + lista.getNome());
                                    listasUsuario++;
                                } else {
                                    listasOutros++;
                                }
                            }
                        }
                    } else {
                        // pula registros excluídos
                        arqListaProduto.arquivo.skipBytes(tam);
                    }
                }

                System.out.println("\nAparece também em mais " + listasOutros + " lista(s) de outras pessoas.");

            } catch (Exception e) {
                System.err.println("Erro ao verificar listas do produto: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("\n(1) Alterar os dados do produto");
            System.out.println("(2) Inativar o produto");
            System.out.println("\n(R) Retornar ao menu anterior");
            System.out.print("\nOpção: ");

            String opcao = console.next();
            switch (opcao.toUpperCase()) {
                case "1":
                    editarProduto(idProduto);
                    break;
                case "2":
                    // implementar inativarProduto(idProduto)
                      inativarProduto(idProduto);
                    break;
                case "R":
                    return;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }

        } else {
            System.out.println("\nProduto não encontrado.");
        }
    }

    public void buscarProduto(int idUsuario) throws Exception {
        console = new Scanner(System.in);
        System.out.println("\n\n---------");
        System.out.println("> Produtos - Buscar Produto");
        System.out.print("Digite o GTIN-13 do produto: ");
        String gtin = console.nextLine();

        try {
            // Buscar no índice hash pelo GTIN-13
            ParIDGTIN pcid = iCode.read(new ParIDGTIN(gtin, -1).hashCode());
            if (pcid != null) {
                Produto produto = arqProduto.read(pcid.getId());
                if (produto != null) {
                    System.out.println("Produto encontrado!");
                    verProduto(produto.getId(), idUsuario);
                } else {
                    System.out.println("Produto não encontrado no arquivo.");
                }
            } else {
                System.out.println("Produto não encontrado.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar o produto.");
            e.printStackTrace();
        }
    }

    public void editarProduto(int idProduto) throws Exception {
        Produto produto = arqProduto.read(idProduto);
        if (produto != null) {
            System.out.println("\n--- Editar Produto ---");
            System.out.println("Produto atual: " + produto.getNome() + " - " + produto.getDescricao());

            console.nextLine(); // consumir enter pendente
            System.out.print("Novo nome (Enter para manter atual): ");
            String novoNome = console.nextLine();
            if (!novoNome.isEmpty()) {
                produto.setNome(novoNome);
            }

            System.out.print("Nova descrição (Enter para manter atual): ");
            String novaDescricao = console.nextLine();
            if (!novaDescricao.isEmpty()) {
                produto.setDescricao(novaDescricao);
            }

            boolean atualizado = arqProduto.update(produto);
            if (atualizado) {
                System.out.println("\n✅ Produto atualizado com sucesso!");
            } else {
                System.out.println("\n❌ Falha ao atualizar o produto.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    public void inativarProduto(int idProduto) throws Exception {
        Produto produto = arqProduto.read(idProduto);
        if (produto != null) {
            if (!produto.isAtivo()) {
                System.out.println("Produto já está inativado.");
                return;
            }

            produto.setAtivo(false); // marca como inativo
            boolean atualizado = arqProduto.update(produto);
            if (atualizado) {
                System.out.println("✅ Produto inativado com sucesso!");
            } else {
                System.out.println("❌ Falha ao inativar o produto.");
            }
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

}
