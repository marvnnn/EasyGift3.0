package Menu;


import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        try {
            MenuUsuario menuUsu = new MenuUsuario();

            int option;
            do {
                System.out.println("\n\nEasyGift 2.0");
                System.out.println("---------");
                System.out.println("> Início");

                System.out.println("\n0 - Sair");
                System.out.println("1 - Login");
                System.out.println("2 - Registrar-se");

                System.out.print("\nOpção: ");
                option = Integer.parseInt(console.nextLine());

                switch (option) {
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    case 1:
                        menuUsu.logar();
                        break;
                    case 2:
                        menuUsu.registrar();
                        break;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }

            } while(option != 0);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        console.close();
    }
}
