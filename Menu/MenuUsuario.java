package Menu;

import java.util.Scanner;
import Arquivos.ArquivoUsuario;
import Entidades.Usuario;
import aed3.*;

public class MenuUsuario {
    private ArquivoUsuario arqUsu;
    private static Scanner console;
    private MenuLista menuLista;
    private MenuProduto menuProduto;
    private HashExtensivel<ParEmailID> indiceEmail;
    private int idUsuario;

    public MenuUsuario() throws Exception {
        arqUsu = new ArquivoUsuario();
        console = new Scanner(System.in);
        menuLista = new MenuLista();
        menuProduto = new MenuProduto();
        indiceEmail = new HashExtensivel<>(ParEmailID.class.getConstructor(), 4, ".\\Dados\\usuario\\indiceEmail.d.db",
                ".\\Dados\\usuario\\indiceEmail.c.db");
    }

    public void logar() throws Exception {

        System.out.println("Digite o Email: ");
        String email = console.nextLine();
        System.out.println("Digite a senha: ");
        String senha = console.nextLine();

        try {
            ParEmailID par = indiceEmail.read(email.hashCode());
            idUsuario = par.getId();
            Usuario u = arqUsu.read(idUsuario);
            if (u != null && u.verificarSenha(senha)) {
                System.out.println("Login bem sucedido! Bem-vindo, " + u.getNome());
                menuUsuario();
            } else {
                System.out.println("Nome de usuário ou senha incorretos.");
            }
        } catch (Exception e) {
            System.out.println("E-mail ou senha inválido.");
        }
        ;
    }

    public void registrar() {
        console = new Scanner(System.in);
        System.out.println("Digite o nome do usuário: ");
        String name = console.nextLine();
        System.out.println("Digite o e-mail: ");
        String email = console.nextLine();
        System.out.println("Digite a senha: ");
        String senha = console.nextLine();
        System.out.println("Digite a pergunta de segurança: ");
        String pergunta = console.nextLine();
        System.out.println("Digite a resposta da pergunta de segurança: ");
        String resposta = console.nextLine();

        Usuario u = new Usuario(name, email, senha, pergunta, resposta);

        try {
            int id = arqUsu.create(u);
            indiceEmail.create(new ParEmailID(email, id));
            System.out.println("Usuário cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    public void menuUsuario() throws Exception {
        int opcao;
        console = new Scanner(System.in);
        do {
            System.out.println("\n\nEasyGift 3.0");
            System.out.println("---------");
            System.out.println("> Início - Autenticado");

            System.out.println("\n0 - Sair");
            System.out.println("1 - Menu Listas");
            System.out.println("2 - Menu Produtos");
            System.out.println("3 - Menu Usuário");

            System.out.print("\nOpção: ");
            opcao = console.nextInt();

            switch (opcao) {
                case 0:
                    System.out.println("\nDeslogando...\n");
                    break;
                case 1:
                    menuLista.menu(idUsuario);
                    break;
                case 2:
                    menuProduto.menu(idUsuario);
                    break;
                case 3:
                    menuUsuario1();
                    break;
                default:
                    break;
            }
        } while (opcao != 0);
    }

    public void atualizarDados() throws Exception {
        console = new Scanner(System.in);
        System.out.println("\n\nEasyGift 3.0");
        System.out.println("---------");
        System.out.println("> Alteração de Dados - Autenticação de Segurança");
        System.out.println("Digite o email cadastrado na sua conta: ");
        String email = console.nextLine();
        System.out.println("Digite sua senha atual: ");
        String senha = console.nextLine();

        try {
            ParEmailID par = indiceEmail.read(email.hashCode());

            Usuario u = arqUsu.read(par.getId());

            if (u != null && u.verificarSenha(senha)) {
                System.out.println("Autenticação feita com sucesso!");

                System.out.println("\n\nDigite sua nova senha: (ENTER PARA MANTER)");
                String newSenha = console.nextLine();

                if(!newSenha.isEmpty()) {
                    u.alterarSenha(newSenha);
                }

                System.out.println("Digite sua nova pergunta de segurança: (ENTER PARA MANTER)");
                String newQuest = console.nextLine();

                if(!newQuest.isEmpty()) {
                    u.setPerguntaSecreta(newQuest);
                }

                System.out.println("Digite sua nova resposta de segurança: (ENTER PARA MANTER)");
                String newAnsw = console.nextLine();

                if(!newAnsw.isEmpty()) {
                    u.setRespostaSecreta(newAnsw);
                }
                
                boolean resultado = arqUsu.update(u);

                if (resultado) {
                    System.out.println("Dados atualizados com sucesso!");
                } else {
                    System.out.println("Houve um problema ao atualizar seus dados, tente novamente.");
                }
            } else {
                System.out.println("E-mail ou senha incorretos.");
            }
        } catch (Exception e) {
            System.out.println("E-mail ou senha incorretos.");
        }
        ;

    }

    public void deletarUsuario() throws Exception {
        console = new Scanner(System.in);
        System.out.println("\n\nEasyGift 3.0");
        System.out.println("---------");
        System.out.println("> Exclusão de Usuário - Autenticação de Segurança");
        System.out.println("\nDigite o email cadastrado na sua conta: ");
        String email = console.nextLine();
        System.out.println("Digite sua senha: ");
        String senha = console.nextLine();

        ParEmailID par = indiceEmail.read(email.hashCode());

        Usuario u = arqUsu.read(par.getId());

        if (u != null && u.verificarSenha(senha)) {
            System.out.println("Verificação feita com sucesso!");
            System.out.println("Você tem certeza que deseja excluir sua conta? (S/N)");
            String resp = console.nextLine();
            if (resp.equals("S")) {
                boolean resposta = arqUsu.delete(par.getId());
                if (resposta) {
                    System.out.println("Conta excluída com sucesso!");
                    return;
                } else {
                    System.out.println("Houve um erro ao excluir sua conta, tente novamente mais tarde.");
                }

            } else {
                return;
            }
        }
    }

    public void menuUsuario1() throws Exception {
        System.out.println("\n\nEasy Gift 3.0");
        System.out.println("> Autenticado > Menu Usuário");
        System.out.println("\n 0 - Voltar");
        System.out.println(" 1 - Excluir usuário");
        System.out.println(" 2 - Alterar dados");
        System.out.print("\nOpção: ");

        int opcao = console.nextInt();
        switch (opcao) {
            case 0:
                System.out.println("\n\nVoltando...");
                break;
            case 1:
                deletarUsuario();
                break;
            case 2:
                atualizarDados();
                break;
            default:
                break;
        }
        
    }
}
