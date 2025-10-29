package Menu;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import Arquivos.ArquivoLista;
import Arquivos.ArquivoUsuario;
import Entidades.Lista;
import Entidades.Usuario;
import aed3.HashExtensivel;
import aed3.ParCID;
import aed3.ArvoreBMais;
import aed3.ParUsuarioLista;

public class MenuLista {
    private ArquivoLista arqList;
    private ArquivoUsuario arqUsu;
    private Scanner console;
    private HashExtensivel<ParCID> iCode;
    private ArvoreBMais<ParUsuarioLista> arvoreLista;
    private MenuListaProduto mlp;

    public MenuLista() throws Exception {
        arqUsu = new ArquivoUsuario();
        mlp = new MenuListaProduto();
        arqList = new ArquivoLista();
        console = new Scanner(System.in);
        iCode = new HashExtensivel<>(ParCID.class.getConstructor(),
                4,
                ".\\Dados\\lista\\listaCodigo.d.db",
                ".\\Dados\\lista\\listaCodigo.c.db");

        arvoreLista = new ArvoreBMais<>(ParUsuarioLista.class.getConstructor(),
                4,
                ".\\Dados\\lista\\arvore_usuario_lista.db");
    }

    // Criar lista
    public void criarLista(int idUsuario) throws Exception {
        Usuario u = arqUsu.read(idUsuario);
        System.out.println("\n\n\n---------");
        System.out.println("> Listas - Criação de Listas");
        System.out.print("Nome da Lista: ");
        console.nextLine();
        String nomeList = console.nextLine();

        System.out.print("Descrição da Lista: ");
        String desq = console.nextLine();

        System.out.print("Data de exclusão (DD/MM/AAAA): ");
        String dataStr = console.nextLine();

        LocalDate dataExclusao;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dataExclusao = LocalDate.parse(dataStr, formatter);
        } catch (Exception e) {
            System.out.println("Data inválida! Usando hoje + 30 dias como padrão.");
            dataExclusao = LocalDate.now().plusDays(30);
        }

        Lista lista = new Lista(nomeList, desq, dataExclusao, u.getNome(), idUsuario);
        int id = arqList.create(lista);
        iCode.create(new ParCID(lista.getCodigoCompartilhavel(), id));
        arvoreLista.create(new ParUsuarioLista(lista.getIdUsuario(), id));

        System.out.println("\nLista criada com sucesso! (ID = " + id + ")");
    }

    public int listarListasUsuario(int idUsuario) throws Exception {
        System.out.println("\n\n---------");
        System.out.println("> Listas > Minhas Listas\n");

        Map<Integer, Integer> mapaEscolhas = new HashMap<>();
        int index = 1;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (int i = 1; i <= arqList.tamanho() + 1; i++) {
            Lista lista = arqList.read(i);
            if (lista != null && lista.getIdUsuario() == idUsuario) {
                String dataStr = (lista.getDataLimite() != null)
                        ? lista.getDataLimite().format(formatter)
                        : "Sem data limite";

                System.out.println("(" + index + ") " + lista.getNome() + " - " + dataStr);
                mapaEscolhas.put(index, lista.getId());
                index++;
            }
        }

        if (mapaEscolhas.isEmpty()) {
            System.out.println("Você não possui Listas cadastradas.");
            return -1;
        }

        System.out.println("Digite a opção: ");
        int escolha = console.nextInt();

        return mapaEscolhas.getOrDefault(escolha, -1);
    }

    public int listarListas(int idUsuario) throws Exception {
        System.out.println("\n\n---------");
        System.out.println("> Listas > Todas as Listas");
        System.out.println("---------");

        int count = 0;
        int index = 1; // contador para exibição numerada

        for (int i = 0; i < arqList.tamanho(); i++) {
            Lista lista = arqList.read(i + 1); // IDs começam em 1
            if (lista != null && lista.getIdUsuario() != idUsuario) {
                String dataStr = lista.getDataLimite() != null
                        ? lista.getDataLimite().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        : "Sem data limite";
                System.out
                        .println("(" + index + ") " + lista.getNome() + " - " + lista.getNomeAutor() + " - " + dataStr);
                index++;
                count++;
            }
        }

        if (count == 0) {
            System.out.println("Você não possui Listas cadastradas.");
        }

        System.out.println("Digite a opção: ");
        int id = console.nextInt();

        return id;
    }

    // Ver detalhes de uma lista
    public void verLista(int id,int idUsuario) throws Exception {
        Lista lista = arqList.read(id);
        if (lista != null) {
            System.out.println("\n--- Detalhes da Lista ---");
            System.out.println("Nome: " + lista.getNome());
            System.out.println("Descrição: " + lista.getDescricao());
            System.out.println("Data de Criação: " + lista.getDataCriacao());
            System.out.println("Data de Encerramento: " + lista.getDataLimite());
            System.out.println("Código compartilhável: " + lista.getCodigoCompartilhavel());
            System.out.println("\n 1 - Gerenciar produtos");
            System.out.println(" 0 - Voltar");
            int op = console.nextInt();
            if (op == 1) {
                mlp.menu(id,idUsuario);
            }

        } else {
            System.out.println("\nLista não encontrada.");
        }
    }

    // Editar lista
    public void editarLista(int id) throws Exception {
        Lista lista = arqList.read(id);
        if (lista == null) {
            System.out.println("\nLista não encontrada.");
            return;
        }

        System.out.println("\n--- Editando Lista ---");
        System.out.println("Nome atual: " + lista.getNome());
        System.out.print("Novo nome (ou Enter p/ manter): ");
        console.nextLine();
        String novoNome = console.nextLine();
        if (!novoNome.isEmpty())
            lista.setNome(novoNome);

        System.out.println("Descrição atual: " + lista.getDescricao());
        System.out.print("Nova descrição (ou Enter p/ manter): ");
        String novaDesc = console.nextLine();
        if (!novaDesc.isEmpty())
            lista.setDescricao(novaDesc);

        System.out.println("Data limite atual: " + lista.getDataLimite());
        System.out.print("Nova data limite (DD/MM/AAAA ou Enter p/ manter): ");
        String novaDataStr = console.nextLine();
        if (!novaDataStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate novaData = LocalDate.parse(novaDataStr, formatter);
                lista.setDataLimite(novaData);
            } catch (Exception e) {
                System.out.println("⚠️ Data inválida! Mantendo a anterior.");
            }
        }

        if (arqList.update(lista)) {
            System.out.println("\n✅ Lista atualizada com sucesso!");
        } else {
            System.out.println("\n⚠️ Erro ao atualizar lista.");
        }
    }

    // Excluir lista
    public void excluirLista(int id) throws Exception {
        Lista lista = arqList.read(id);
        if (lista != null) {
            iCode.delete(lista.getCodigoCompartilhavel().hashCode());
            arvoreLista.delete(new ParUsuarioLista(lista.getIdUsuario(), id));
            boolean resp = arqList.delete(id);

            if (resp) {
                System.out.println("Lista excluída com sucesso! ");
            } else {
                System.out.println("Não foi possível excluir sua Lista, tente novamente mais tarde.");
            }
        } else {
            System.out.println("\nLista não encontrada.");
        }
    }

    public void menu(int idUsuario) throws Exception {
        console = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("\n\nEasyGift 2.0");
            System.out.println("---------");
            System.out.println("> Listas - Autenticado");
            System.out.println("\n0 - Voltar");
            System.out.println("1 - Criar Lista");
            System.out.println("2 - Buscar Lista");
            System.out.println("3 - Excluir Lista");
            System.out.println("4 - Editar dados da Lista");
            System.out.println("5 - Ver minhas Listas");
            System.out.print("\nOpção: ");

            opcao = console.nextInt();
            int id;

            switch (opcao) {
                case 0:
                    System.out.println("Voltando...");
                    break;
                case 1:
                    criarLista(idUsuario);
                    break;
                case 2:
                    buscarLista();
                    break;
                case 3:
                    id = listarListasUsuario(idUsuario);
                    excluirLista(id);
                    break;
                case 4:
                    id = listarListasUsuario(idUsuario);
                    editarLista(id);
                    break;
                case 5:
                    id = listarListasUsuario(idUsuario);
                    verLista(id,idUsuario);
                    break;
                default:
                    break;
            }
        } while (opcao != 0);
    }

    public void buscarLista() throws Exception {
        console = new Scanner(System.in);
        System.out.println("\n\n---------");
        System.out.println("> Listas - Buscar Lista");
        System.out.print("Digite o código compartilhável: ");
        String codigoComp = console.nextLine();
        try {
            ParCID pcid = iCode.read(new ParCID(codigoComp, -1).hashCode());
            if (pcid != null) {
                Lista lista = arqList.read(pcid.getId());
                if (lista != null) {
                    System.out.println("Lista encontrada!");
                    mostrarLista(lista);
                }
            }
        } catch (Exception e) {
            System.out.println("Código inválido.");
        }
        ;

    }

    public void mostrarLista(Lista lista) {
        console = new Scanner(System.in);
        System.out.println("Nome da Lista: " + lista.getNome());
        System.out.println("Nome do Criador: " + lista.getNomeAutor());
        System.out.println("Descrição: " + lista.getDescricao());
        System.out.println("Data de Encerramento: " + lista.getDataLimite());
        System.out.println("\nAperte ENTER para voltar. ");
        console.nextLine();
    }
}
