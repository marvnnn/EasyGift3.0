package Menu;

import java.util.*;
import Entidades.Lista;
import Entidades.ListaProduto;
import Entidades.Produto;
import aed3.*;
import Arquivos.*;

public class MenuProduto {
    public Scanner console;
    public ArquivoProduto arqProduto;
    public ArquivoUsuario arqUsu;
    public ArquivoListaProduto arqListaProduto;
    public ArquivoLista arqList;
    public HashExtensivel<ParIDGTIN> iCode;
    public ArvoreBMais<ParUsuarioLista> arvoreLista;
    public ListaInvertida listaInvertida;

    public MenuProduto() throws Exception {
        arqUsu = new ArquivoUsuario();
        listaInvertida = new ListaInvertida(10, "arqPalavras.db", "arqBlocos.db");
        arqList = new ArquivoLista();
        arqListaProduto = new ArquivoListaProduto();
        arqProduto = new ArquivoProduto();
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
            System.out.println("\n\nEasyGift 3.0");
            System.out.println("---------");
            System.out.println("> Produtos - Autenticado");
            System.out.println("\n0 - Voltar");
            System.out.println("1 - Buscar produtos por GTIN");
            System.out.println("2 - Listar todos os Produtos");
            System.out.println("3 - Cadastrar um novo Produto");
            System.out.println("4 - Buscar produtos por palavra");
            System.out.print("\nOpção: ");

            opcao = console.nextInt();

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
                case 4:
                    buscarProdutoPorPalavra(idUsuario);
                    break;
                default:
                    System.out.println("Opção inválida.");
                    break;
            }
        } while (opcao != 0);
    }

    public void cadastrarProduto(int idUsuario) throws Exception {
        System.out.println("\n\n\n---------");
        System.out.println("> Produtos - Cadastro de Novo Produto");

        console.nextLine();
        String gtin;
        do {
            System.out.print("GTIN-13: (13 caracteres): ");
            gtin = console.nextLine();
        } while (gtin.length() != 13);

        System.out.print("Nome do Produto: ");
        String nome = console.nextLine();

        System.out.print("Descrição do Produto: ");
        String descricao = console.nextLine();

        Produto produto = new Produto(gtin, nome, descricao);
        int id = arqProduto.create(produto);
        iCode.create(new ParIDGTIN(produto.getGtin13(), id));

        // --- Inserir no índice invertido ---
        String[] palavras = nome.toLowerCase().split("\\s+");
        Map<String, Integer> freq = new HashMap<>();
        for (String p : palavras) {
            p = p.replaceAll("[^a-z0-9áéíóúãõç]", "");
            if (p.isEmpty()) continue;
            freq.put(p, freq.getOrDefault(p, 0) + 1);
        }

        for (Map.Entry<String, Integer> entry : freq.entrySet()) {
            String termo = entry.getKey();
            float tf = entry.getValue();
            ElementoLista el = new ElementoLista(produto.getId(), tf);
            listaInvertida.create(termo, el);
        }

        System.out.println("\n✅ Produto cadastrado com sucesso! (ID = " + id + ")");
    }

    public int listarProdutos(int idUsuario) throws Exception {
        System.out.println("\n\n---------");
        System.out.println("> Produtos - Listagem de Todos os Produtos");
        System.out.println("---------");

        int index = 1;
        int escolhido = -1;

        for (int i = 1; i <= arqProduto.tamanho(); i++) {
            Produto p = arqProduto.read(i);
            if (p != null) {
                if (p.isAtivo()) {
                    System.out.println("(" + index + ") " + p.getNome() + " - " + p.getDescricao());
                } else {
                    System.out.println("(" + index + ") " + p.getNome() + " - " + p.getDescricao() + " (INATIVO)");
                }
                index++;
            }
        }

        if (index == 1) {
            System.out.println("Nenhum produto cadastrado.");
            return -1;
        }

        System.out.print("Digite o número do produto desejado: ");
        int opcao = console.nextInt();

        if (opcao >= 1 && opcao < index) {
            int count = 0;
            for (int i = 1; i <= arqProduto.tamanho(); i++) {
                Produto p = arqProduto.read(i);
                if (p != null) {
                    count++;
                    if (count == opcao) {
                        escolhido = i;
                        break;
                    }
                }
            }
        } else {
            System.out.println("Opção inválida.");
        }

        return escolhido;
    }

    public void verProduto(int idProduto, int idUsuario) throws Exception {
        Produto produto = arqProduto.read(idProduto);
        if (produto != null) {
            System.out.println("\nEasy Gift 3.0");
            System.out.println("-----------------");
            System.out.println("> Início > Produtos > Listagem > " + produto.getNome() + "\n");

            System.out.println("NOME.......: " + produto.getNome());
            System.out.println("GTIN-13....: " + produto.getGtin13());
            System.out.println("DESCRIÇÃO..: " + produto.getDescricao());
            System.out.println();

            int listasUsuario = 0;
            int listasOutros = 0;

            try {
                arqListaProduto.arquivo.seek(12);
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
                            Lista lista = arqList.read(lp.getIdLIsta());
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

    public void buscarProdutoPorPalavra(int idUsuario) throws Exception {
        console = new Scanner(System.in);
        System.out.println("\n\n---------");
        System.out.println("> Produtos - Buscar por Palavra");
        System.out.print("Digite a palavra ou frase de busca: ");
        String consulta = console.nextLine().toLowerCase();

        String[] termos = consulta.split("\\s+");
        Map<Integer, Float> scores = new HashMap<>();
        int N = arqProduto.tamanho();

        for (String termo : termos) {
            ElementoLista[] lista = listaInvertida.read(termo);
            if (lista.length == 0) continue;

            int df = lista.length;
            float idf = (float) Math.log((float) N / (1 + df));

            for (ElementoLista el : lista) {
                float tf = el.getFrequencia();
                float tfidf = tf * idf;
                scores.put(el.getId(), scores.getOrDefault(el.getId(), 0f) + tfidf);
            }
        }

        if (scores.isEmpty()) {
            System.out.println("\nNenhum produto encontrado para '" + consulta + "'.");
            return;
        }

        List<Map.Entry<Integer, Float>> ordenados = new ArrayList<>(scores.entrySet());
        ordenados.sort((a, b) -> Float.compare(b.getValue(), a.getValue()));

        System.out.println("\nResultados de busca:");
        for (Map.Entry<Integer, Float> e : ordenados) {
            Produto p = arqProduto.read(e.getKey());
            if (p != null && p.isAtivo()) {
                System.out.printf( p.getId() + "- %s (%.3f)\n", p.getNome(), e.getValue());
            }
        }
    }

    public void editarProduto(int idProduto) throws Exception {
        Produto produto = arqProduto.read(idProduto);
        if (produto != null) {
            System.out.println("\n--- Editar Produto ---");
            System.out.println("Produto atual: " + produto.getNome() + " - " + produto.getDescricao());

            console.nextLine();
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

            produto.setAtivo(false);
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
