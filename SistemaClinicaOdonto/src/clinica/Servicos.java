package clinica;

import java.util.Scanner;

public class Servicos {
    private String nome;
    private double valor;

    public Servicos(String nome, double valor) {
        this.nome = nome;
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    // Método para cadastrar apenas no Catálogo Geral
    public static void cadastrarServico(Scanner scanner) {
        System.out.print("Nome do serviço (ex: Massagem): ");
        String nome = scanner.nextLine();
        
        System.out.print("Valor base (ex: 80,00): ");
        double valor = scanner.nextDouble();
        scanner.nextLine(); // Limpar buffer
        
        // Verifica se já existe
        for(Servicos s : Main.servicos) {
            if(s.getNome().equalsIgnoreCase(nome)) {
                System.out.println("Serviço já existe no catálogo!");
                return;
            }
        }
        
        Main.servicos.add(new Servicos(nome, valor));
        System.out.println("Serviço adicionado ao catálogo!");
    }

    public static void listarServicos() {
        if (Main.servicos.isEmpty()) {
            System.out.println("Nenhum serviço no catálogo.");
            return;
        }
        System.out.println("==== Catálogo de Serviços ====");
        for (Servicos servico : Main.servicos) {
            System.out.println("- " + servico.getNome() + " (R$ " + servico.getValor() + ")");
        }
    }
}