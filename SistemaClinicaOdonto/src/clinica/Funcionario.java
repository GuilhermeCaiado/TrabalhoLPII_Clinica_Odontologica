package clinica;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Funcionario extends Pessoa {
    private String especialidade;
    private AgendaFuncionario agenda;
    // Lista de nomes de serviços que este funcionário realiza
    private List<String> servicosHabilitados; 

    public Funcionario(String nome, String cpf, String telefone, String endereco, String especialidade) {
        super(nome, cpf, telefone, endereco);
        this.especialidade = especialidade;
        this.agenda = new AgendaFuncionario(this);
        this.servicosHabilitados = new ArrayList<>();
    }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
    public AgendaFuncionario getAgenda() { return agenda; }
    public List<String> getServicosHabilitados() { return servicosHabilitados; }

    // Adiciona um serviço à lista de habilidades do funcionário
    public void adicionarServico(String nomeServico) {
        boolean jaTem = false;
        for(String s : servicosHabilitados) {
            if(s.equalsIgnoreCase(nomeServico)) jaTem = true;
        }
        if (!jaTem) {
            servicosHabilitados.add(nomeServico);
        }
    }

    public boolean realizaServico(String nomeServico) {
        for (String s : servicosHabilitados) {
            if (s.equalsIgnoreCase(nomeServico)) return true;
        }
        return false;
    }
    
    // --- Menus e Cadastros ---

    public static void cadastrarFuncionario(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        String cpf;
        while (true) {
            System.out.print("CPF (11 dígitos): ");
            cpf = scanner.nextLine();
            if (cpf.length() == 11) break;
            System.out.println("CPF inválido! Deve conter 11 dígitos.");
        }
        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();
        System.out.print("Endereço: ");
        String endereco = scanner.nextLine();
        System.out.print("Especialidade Principal: ");
        String especialidade = scanner.nextLine();
        
        Main.funcionarios.add(new Funcionario(nome, cpf, telefone, endereco, especialidade));
        System.out.println("Funcionário cadastrado com sucesso!");
    }

    // Novo menu para dizer quais serviços o funcionário faz
    public static void vincularServico(Scanner scanner) {
        System.out.print("Digite o CPF do funcionário: ");
        String cpf = scanner.nextLine();
        Funcionario func = buscarFuncionarioPorCpf(cpf);
        
        if(func == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }
        
        if(Main.servicos.isEmpty()) {
            System.out.println("Nenhum serviço no catálogo. Cadastre serviços primeiro.");
            return;
        }

        System.out.println("Escolha um serviço para adicionar a " + func.getNome() + ":");
        for(int i=0; i<Main.servicos.size(); i++) {
            System.out.println((i+1) + ". " + Main.servicos.get(i).getNome());
        }
        
        int opcao = scanner.nextInt();
        scanner.nextLine();
        
        if(opcao < 1 || opcao > Main.servicos.size()) {
            System.out.println("Inválido.");
            return;
        }
        
        String servicoEscolhido = Main.servicos.get(opcao-1).getNome();
        func.adicionarServico(servicoEscolhido);
        System.out.println("Serviço '" + servicoEscolhido + "' vinculado a " + func.getNome() + "!");
    }
    
    public static void listarFuncionarios() {
        if (Main.funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }
        for (Funcionario f : Main.funcionarios) {
            System.out.println("Nome: " + f.getNome() + " | CPF: " + f.getCpf());
            System.out.println("   -> Serviços Habilitados: " + f.getServicosHabilitados());
        }
    }
    
    public static Funcionario buscarFuncionarioPorCpf(String cpf) {
        for (Funcionario f : Main.funcionarios) {
            if (f.getCpf().equals(cpf)) return f;
        }
        return null;
    }
}