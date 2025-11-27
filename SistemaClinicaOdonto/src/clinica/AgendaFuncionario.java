package clinica;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AgendaFuncionario {
    private Funcionario funcionario;
    // Lista de inteiros representando os dias: 1=Segunda, 2=Terça ... 7=Domingo
    private List<Integer> diasTrabalho;

    public AgendaFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
        this.diasTrabalho = new ArrayList<>();
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public List<Integer> getDiasTrabalho() {
        return diasTrabalho;
    }
    
    public void adicionarDias(List<Integer> dias) {
        for(Integer dia : dias) {
            if(!this.diasTrabalho.contains(dia)) {
                this.diasTrabalho.add(dia);
            }
        }
    }

    public boolean trabalhaNoDia(int diaDaSemana) {
        return diasTrabalho.contains(diaDaSemana);
    }

    public static void definirAgenda(Scanner scanner, Funcionario funcionario) {
        System.out.println("==== Definir Dias de Trabalho ====");
        System.out.println("1 - Segunda-feira");
        System.out.println("2 - Terça-feira");
        System.out.println("3 - Quarta-feira");
        System.out.println("4 - Quinta-feira");
        System.out.println("5 - Sexta-feira");
        System.out.println("6 - Sábado");
        System.out.println("7 - Domingo");
        System.out.println("Digite os dias que você trabalha separados por vírgula (Ex: 1,3,5): ");
        
        String entrada = scanner.nextLine();
        String[] partes = entrada.split(",");
        
        List<Integer> novosDias = new ArrayList<>();
        
        try {
            for (String parte : partes) {
                int dia = Integer.parseInt(parte.trim());
                if (dia >= 1 && dia <= 7) {
                    novosDias.add(dia);
                } else {
                    System.out.println("Aviso: " + dia + " não é um dia válido (1-7) e será ignorado.");
                }
            }
            
            // Limpa dias anteriores e define os novos
            funcionario.getAgenda().getDiasTrabalho().clear();
            funcionario.getAgenda().adicionarDias(novosDias);
            
            System.out.println("Agenda atualizada! Dias de trabalho: " + novosDias);
            
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida! Digite apenas números separados por vírgula.");
        }
    }

    public static void visualizarAgenda(Funcionario funcionario) {
        System.out.println("==== Agenda de " + funcionario.getNome() + " ====");
        System.out.println("Dias de trabalho: " + funcionario.getAgenda().getDiasTrabalho().toString()
                .replace("1", "Seg").replace("2", "Ter").replace("3", "Qua")
                .replace("4", "Qui").replace("5", "Sex").replace("6", "Sab").replace("7", "Dom"));
        
        System.out.println("\n--- Agendamentos Marcados ---");
        boolean encontrou = false;
        for (Agendamento agendamento : Main.agendamentos) {
            
            // CORREÇÃO AQUI:
            // Antes: agendamento.getServico().getFuncionario().equals(...)
            // Agora: agendamento.getFuncionario().equals(...)
            if (agendamento.getFuncionario().getCpf().equals(funcionario.getCpf())) {
                System.out.println("Cliente: " + agendamento.getCliente().getNome() + 
                        " | Serviço: " + agendamento.getServico().getNome() +
                        " | Data: " + agendamento.getData() + 
                        " | Horário: " + agendamento.getHorario());
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhum cliente agendado.");
        }
    }
}