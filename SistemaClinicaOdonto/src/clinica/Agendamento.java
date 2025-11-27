package clinica;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Arrays; // Import necessário
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.Set;

public class Agendamento {

    private Cliente cliente;
    private Servicos servico;     
    private Funcionario funcionario; 
    private String data;
    private String horario;

    public Agendamento(Cliente cliente, Servicos servico, Funcionario funcionario, String data, String horario) {
        this.cliente = cliente;
        this.servico = servico;
        this.funcionario = funcionario;
        this.data = data;
        this.horario = horario;
    }

    public Cliente getCliente() { return cliente; }
    public Servicos getServico() { return servico; }
    public Funcionario getFuncionario() { return funcionario; } 
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public static void agendar(Scanner scanner, Cliente cliente) {
        if (Main.servicos.isEmpty()) {
            System.out.println("Não há serviços cadastrados no catálogo.");
            return;
        }

        // ==========================================
        // 1. FILTRAR E SELECIONAR O SERVIÇO
        // ==========================================
        List<Servicos> servicosDisponiveis = new ArrayList<>();
        
        for (Servicos s : Main.servicos) {
            boolean temFuncionario = false;
            for (Funcionario f : Main.funcionarios) {
                if (f.realizaServico(s.getNome())) {
                    temFuncionario = true;
                    break;
                }
            }
            if (temFuncionario) {
                servicosDisponiveis.add(s);
            }
        }

        if (servicosDisponiveis.isEmpty()) {
            System.out.println("Nenhum serviço possui profissionais habilitados no momento.");
            return;
        }

        System.out.println("==== Serviços Disponíveis ====");
        for (int i = 0; i < servicosDisponiveis.size(); i++) {
            System.out.println((i + 1) + ". " + servicosDisponiveis.get(i).getNome());
        }

        System.out.print("Escolha o número do serviço: ");
        int escolhaServico = scanner.nextInt();
        scanner.nextLine();

        if (escolhaServico < 1 || escolhaServico > servicosDisponiveis.size()) {
            System.out.println("Opção inválida.");
            return;
        }
        
        Servicos servicoCatalogo = servicosDisponiveis.get(escolhaServico - 1);
        String nomeServico = servicoCatalogo.getNome();

        // Identifica funcionários habilitados
        List<Funcionario> habilitados = new ArrayList<>();
        for(Funcionario f : Main.funcionarios) {
            if(f.realizaServico(nomeServico)) {
                habilitados.add(f);
            }
        }

        if(habilitados.isEmpty()) {
            System.out.println("Erro inesperado: nenhum funcionário encontrado.");
            return;
        }

        // ==========================================
        // 2. SELECIONE A DATA
        // ==========================================
        Set<Integer> diasUteisGerais = new HashSet<>();
        for(Funcionario f : habilitados) {
            diasUteisGerais.addAll(f.getAgenda().getDiasTrabalho());
        }
        
        if (diasUteisGerais.isEmpty()) {
            System.out.println("Os funcionários deste serviço ainda não definiram dias de trabalho.");
            return;
        }

        System.out.println("\n==== Datas Disponíveis para " + nomeServico + " ====");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate hoje = LocalDate.now();
        List<LocalDate> datasValidas = new ArrayList<>();

        int diasEncontrados = 0;
        int checkDays = 0;
        
        while (checkDays < 30 && diasEncontrados < 15) { 
            LocalDate dataTeste = hoje.plusDays(checkDays);
            int diaSemanaInt = dataTeste.getDayOfWeek().getValue();

            if (diasUteisGerais.contains(diaSemanaInt)) {
                datasValidas.add(dataTeste);
                String diaSemanaNome = dataTeste.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("pt", "BR"));
                diaSemanaNome = diaSemanaNome.substring(0, 1).toUpperCase() + diaSemanaNome.substring(1);
                
                System.out.println((diasEncontrados + 1) + ". " + dataTeste.format(formatter) + " (" + diaSemanaNome + ")");
                diasEncontrados++;
            }
            checkDays++;
        }

        if (datasValidas.isEmpty()) {
            System.out.println("Sem datas disponíveis em breve.");
            return;
        }

        int escolhaData = 0;
        while (escolhaData < 1 || escolhaData > datasValidas.size()) {
            System.out.print("Escolha uma data (número): ");
            if (scanner.hasNextInt()) {
                escolhaData = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.next();
            }
        }
        LocalDate dataDesejada = datasValidas.get(escolhaData - 1);

        // ==========================================
        // 3. SELECIONE O HORÁRIO (AGORA COM OPÇÕES)
        // ==========================================
        
        // Lista de horários padrão da clínica
        List<String> todosHorarios = Arrays.asList(
            "08:00", "09:00", "10:00", "11:00", "12:00", 
            "13:00", "14:00", "15:00", "16:00", "17:00"
        );
        
        List<String> horariosDisponiveisNaData = new ArrayList<>();
        int diaSemanaInt = dataDesejada.getDayOfWeek().getValue();

        // Filtra quais horários têm pelo menos 1 funcionário livre
        for (String slot : todosHorarios) {
            boolean algumLivreNesseSlot = false;
            
            for (Funcionario f : habilitados) {
                // O funcionário trabalha nesse dia da semana?
                if (f.getAgenda().trabalhaNoDia(diaSemanaInt)) {
                    // O funcionário está livre nessa hora?
                    if (isFuncionarioLivre(f, dataDesejada, slot)) {
                        algumLivreNesseSlot = true;
                        break; // Se achou um, esse horário é válido para mostrar
                    }
                }
            }
            
            if (algumLivreNesseSlot) {
                horariosDisponiveisNaData.add(slot);
            }
        }

        if (horariosDisponiveisNaData.isEmpty()) {
            System.out.println("Desculpe, a agenda está lotada para esta data.");
            return;
        }

        System.out.println("\n==== Horários Disponíveis ====");
        for (int i = 0; i < horariosDisponiveisNaData.size(); i++) {
            System.out.println((i + 1) + ". " + horariosDisponiveisNaData.get(i));
        }

        System.out.print("Escolha o horário: ");
        int escolhaHorario = scanner.nextInt();
        scanner.nextLine();

        if (escolhaHorario < 1 || escolhaHorario > horariosDisponiveisNaData.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        String horarioDesejado = horariosDisponiveisNaData.get(escolhaHorario - 1);

        // ==========================================
        // 4. RETORNA FUNCIONÁRIOS DISPONÍVEIS NAQUELA DATA/HORA
        // ==========================================
        System.out.println("\n==== Profissionais Disponíveis ====");
        List<Funcionario> disponiveisFinal = new ArrayList<>();

        for (Funcionario f : habilitados) {
            // Verifica novamente para filtrar a lista final de profissionais
            if (f.getAgenda().trabalhaNoDia(diaSemanaInt)) {
                if (isFuncionarioLivre(f, dataDesejada, horarioDesejado)) {
                    disponiveisFinal.add(f);
                }
            }
        }

        // Não deve estar vazio aqui, pois filtramos antes, mas por segurança:
        if (disponiveisFinal.isEmpty()) {
            System.out.println("Erro ao confirmar disponibilidade.");
            return;
        }

        for (int i = 0; i < disponiveisFinal.size(); i++) {
            Funcionario f = disponiveisFinal.get(i);
            System.out.println((i + 1) + ". " + f.getNome() + " | Valor Base: R$ " + servicoCatalogo.getValor());
        }

        System.out.print("Escolha o profissional: ");
        int escolhaProf = scanner.nextInt();
        scanner.nextLine();

        if (escolhaProf < 1 || escolhaProf > disponiveisFinal.size()) {
            System.out.println("Opção inválida.");
            return;
        }

        Funcionario funcionarioEscolhido = disponiveisFinal.get(escolhaProf - 1);
        
        // Finaliza
        String dataFinalStr = dataDesejada.format(formatter);
        
        Agendamento novoAgendamento = new Agendamento(cliente, servicoCatalogo, funcionarioEscolhido, dataFinalStr, horarioDesejado);
        
        cliente.agendar(novoAgendamento);
        Main.agendamentos.add(novoAgendamento);
        
        System.out.println("\n=== Agendamento Confirmado! ===");
        System.out.println("Serviço: " + servicoCatalogo.getNome());
        System.out.println("Profissional: " + funcionarioEscolhido.getNome());
        System.out.println("Data: " + dataFinalStr + " às " + horarioDesejado);
        System.out.println("Valor: R$ " + servicoCatalogo.getValor());
    }

    private static boolean isFuncionarioLivre(Funcionario f, LocalDate data, String horario) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataStr = data.format(dtf);
        
        for (Agendamento a : Main.agendamentos) {
            if (a.getFuncionario().getCpf().equals(f.getCpf()) && 
                a.getData().equals(dataStr) && 
                a.getHorario().equals(horario)) {
                return false;
            }
        }
        return true;
    }

    public static void listarAgendamentos(Cliente cliente) {
        System.out.println("==== Meus Agendamentos ====");
        List<Agendamento> agendamentosCliente = cliente.getAgendamentos();
        if (agendamentosCliente.isEmpty()) {
            System.out.println("Nenhum agendamento encontrado.");
            return;
        }
        for (int i = 0; i < agendamentosCliente.size(); i++) {
            Agendamento a = agendamentosCliente.get(i);
            System.out.println((i + 1) + ". " + a.getServico().getNome() + 
                    " com " + a.getFuncionario().getNome() +
                    " | Data: " + a.getData() + " " + a.getHorario());
        }
    }
    
    public static void cancelarAgendamento(Scanner scanner, Cliente cliente) {
        listarAgendamentos(cliente);
        if (cliente.getAgendamentos().isEmpty()) return;
        
        System.out.print("Digite o número do agendamento para cancelar: ");
        int index = scanner.nextInt();
        scanner.nextLine();

        if (index < 1 || index > cliente.getAgendamentos().size()) {
            System.out.println("Inválido!");
            return;
        }
        Agendamento a = cliente.getAgendamentos().get(index - 1);
        cliente.cancelarAgendamento(a);
        Main.agendamentos.remove(a);
        System.out.println("Cancelado!");
    }
    
    public static void reagendar(Scanner scanner, Cliente cliente) {
        System.out.println("Para reagendar, cancele o atual e faça um novo agendamento.");
    }
}