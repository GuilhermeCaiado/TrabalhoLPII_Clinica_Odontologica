package clinica;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Pessoa {
    private List<Agendamento> agendamentos;

    public Cliente(String nome, String cpf, String telefone, String endereco) {
        super(nome, cpf, telefone, endereco);
        this.agendamentos = new ArrayList<>();
    }

    // Construtor padrão removido pois não era utilizado de forma consistente.

    public void agendar(Agendamento agendamento) {
        this.agendamentos.add(agendamento);
    }

    public void reagendar(Agendamento agendamento, String novaData, String novoHorario) {
        agendamento.setData(novaData);
        agendamento.setHorario(novoHorario);
    }

    public void cancelarAgendamento(Agendamento agendamento) {
        this.agendamentos.remove(agendamento);
    }

    public List<Agendamento> getAgendamentos() {
        return agendamentos;
    }

    public static void cadastrarCliente(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        String cpf;
        while (true) {
            System.out.print("CPF (11 dígitos): ");
            cpf = scanner.nextLine();
            if (cpf.length() == 11) {
                break;
            }
            System.out.println("CPF inválido! Deve conter 11 dígitos.");
        }
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        Main.clientes.add(new Cliente(nome, cpf, telefone, endereco));
        System.out.println("Cliente cadastrado com sucesso!");
    }

    public static void listarClientes() {
        if (Main.clientes.isEmpty()) {
            System.out.println("Nenhum cliente cadastrado.");
            return;
        }
        for (Cliente cliente : Main.clientes) {
            System.out.println("Nome: " + cliente.getNome() + ", CPF: " + cliente.getCpf());
        }
    }

    public static Cliente buscarClientePorCpf(String cpf) {
        for (Cliente c : Main.clientes) {
            if (c.getCpf().equals(cpf)) {
                return c;
            }
        }
        return null;
    }
}