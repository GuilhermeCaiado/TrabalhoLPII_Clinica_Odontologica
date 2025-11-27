package clinica;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    // Listas globais
    public static List<Cliente> clientes = new ArrayList<>();
    public static List<Funcionario> funcionarios = new ArrayList<>();
    public static List<Servicos> servicos = new ArrayList<>(); // Catálogo
    public static List<Agendamento> agendamentos = new ArrayList<>();
    
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        GerenciadorDeArquivos.carregarDados(clientes, funcionarios, servicos, agendamentos);
        
        int opcao = 0;
        do{
            System.out.println("==== Menu Principal ====");
            System.out.println("1. Menu de Gerenciamento (Cadastros)");
            System.out.println("2. Menu Cliente (Agendamentos)");
            System.out.println("3. Menu Funcionario (Agenda de Trabalho)");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");
            if(scanner.hasNextInt()){
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.next();
                continue;
            }

            switch (opcao) {
                case 1:
                    menuCRUD();
                    break;
                case 2:
                    menuCliente();
                    break;
                case 3:
                    menuFuncionario();
                    break;
                case 4:
                    GerenciadorDeArquivos.salvarDados(clientes, funcionarios, servicos, agendamentos);
                    System.out.println("Dados salvos. Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }while(opcao != 4);
    }

    public static void menuCRUD() {
        System.out.println("==== Gerenciamento ====");
        System.out.println("1. Cadastrar Cliente");
        System.out.println("2. Cadastrar Funcionário");
        System.out.println("3. Cadastrar Serviço (Catálogo)");
        System.out.println("4. Vincular Serviço a Funcionário"); // Nova Opção
        System.out.println("5. Listar Clientes");
        System.out.println("6. Listar Funcionários");
        System.out.println("7. Listar Serviços");
        System.out.println("8. Voltar");
        System.out.print("Opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcao) {
            case 1: Cliente.cadastrarCliente(scanner); break;
            case 2: Funcionario.cadastrarFuncionario(scanner); break;
            case 3: Servicos.cadastrarServico(scanner); break;
            case 4: Funcionario.vincularServico(scanner); break; // Nova chamada
            case 5: Cliente.listarClientes(); break;
            case 6: Funcionario.listarFuncionarios(); break;
            case 7: Servicos.listarServicos(); break;
            case 8: break;
            default: System.out.println("Inválido!");
        }
    }

    public static void menuCliente() {
        System.out.print("CPF do cliente: ");
        String cpf = scanner.nextLine();
        Cliente cliente = Cliente.buscarClientePorCpf(cpf);

        if (cliente == null) {
            System.out.println("Cliente não encontrado!");
            return;
        }

        while (true) {
            System.out.println("==== Área do Cliente: " + cliente.getNome() + " ====");
            System.out.println("1. Agendar Serviço");
            System.out.println("2. Reagendar");
            System.out.println("3. Cancelar Agendamento");
            System.out.println("4. Meus Agendamentos");
            System.out.println("5. Voltar");
            System.out.print("Opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: Agendamento.agendar(scanner, cliente); break;
                case 2: Agendamento.reagendar(scanner, cliente); break;
                case 3: Agendamento.cancelarAgendamento(scanner, cliente); break;
                case 4: Agendamento.listarAgendamentos(cliente); break;
                case 5: return;
                default: System.out.println("Inválido!");
            }
        }
    }

    public static void menuFuncionario() {
        System.out.print("CPF do funcionário: ");
        String cpf = scanner.nextLine();
        Funcionario funcionario = Funcionario.buscarFuncionarioPorCpf(cpf);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado!");
            return;
        }

        while (true) {
            System.out.println("==== Área do Funcionário: " + funcionario.getNome() + " ====");
            System.out.println("1. Definir Dias de Trabalho");
            System.out.println("2. Visualizar Minha Agenda");
            System.out.println("3. Voltar");
            System.out.print("Opção: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1: AgendaFuncionario.definirAgenda(scanner, funcionario); break;
                case 2: AgendaFuncionario.visualizarAgenda(funcionario); break;
                case 3: return;
                default: System.out.println("Inválido!");
            }
        }
    }
}