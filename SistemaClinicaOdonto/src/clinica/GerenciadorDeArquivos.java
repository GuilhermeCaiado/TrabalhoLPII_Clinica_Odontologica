package clinica;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeArquivos {

    private static final String ARQUIVOS_DIR = "Arquivos";

    public static void salvarDados(List<Cliente> clientes, List<Funcionario> funcionarios, List<Servicos> servicos, List<Agendamento> agendamentos) {
        try {
            File dir = new File(ARQUIVOS_DIR);
            if (!dir.exists()) {
                dir.mkdir();
            }

            salvarClientes(clientes);
            salvarFuncionarios(funcionarios); // Salva dados básicos
            salvarAgendaFuncionarios(funcionarios); // Salva dias de trabalho (Seg, Ter...)
            
            // Novos métodos de salvar
            salvarCatalogoServicos(servicos);
            salvarVinculosServicos(funcionarios);
            
            salvarAgendamentos(agendamentos);

        } catch (IOException e) {
            System.out.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static void carregarDados(List<Cliente> clientes, List<Funcionario> funcionarios, List<Servicos> servicos, List<Agendamento> agendamentos) {
        try {
            carregarClientes(clientes);
            carregarFuncionarios(funcionarios);
            carregarAgendaFuncionarios(funcionarios);
            
            // Novos métodos de carregar
            carregarCatalogoServicos(servicos);
            carregarVinculosServicos(funcionarios);
            
            carregarAgendamentos(agendamentos, clientes, servicos, funcionarios);
            
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    // --- SALVAR ---

    private static void salvarClientes(List<Cliente> clientes) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOS_DIR + "/clientes.txt"))) {
            for (Cliente cliente : clientes) {
                writer.write(cliente.getNome() + ";" + cliente.getCpf() + ";" + cliente.getTelefone() + ";" + cliente.getEndereco());
                writer.newLine();
            }
        }
    }

    private static void salvarFuncionarios(List<Funcionario> funcionarios) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOS_DIR + "/funcionarios.txt"))) {
            for (Funcionario funcionario : funcionarios) {
                writer.write(funcionario.getNome() + ";" + funcionario.getCpf() + ";" + funcionario.getTelefone() + ";" + funcionario.getEndereco() + ";" + funcionario.getEspecialidade());
                writer.newLine();
            }
        }
    }

    private static void salvarAgendaFuncionarios(List<Funcionario> funcionarios) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOS_DIR + "/agenda_funcionarios.txt"))) {
            for (Funcionario funcionario : funcionarios) {
                List<Integer> dias = funcionario.getAgenda().getDiasTrabalho();
                if (!dias.isEmpty()) {
                    String diasStr = dias.toString().replace("[", "").replace("]", "").replace(" ", "");
                    writer.write(funcionario.getCpf() + ";" + diasStr);
                    writer.newLine();
                }
            }
        }
    }

    private static void salvarCatalogoServicos(List<Servicos> servicos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOS_DIR + "/servicos_catalogo.txt"))) {
            for (Servicos servico : servicos) {
                // Nome;Valor
                writer.write(servico.getNome() + ";" + servico.getValor());
                writer.newLine();
            }
        }
    }

    private static void salvarVinculosServicos(List<Funcionario> funcionarios) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOS_DIR + "/funcionario_servicos.txt"))) {
            for (Funcionario f : funcionarios) {
                if (!f.getServicosHabilitados().isEmpty()) {
                    // CPF;Servico1,Servico2,Servico3
                    String lista = String.join(",", f.getServicosHabilitados());
                    writer.write(f.getCpf() + ";" + lista);
                    writer.newLine();
                }
            }
        }
    }

    private static void salvarAgendamentos(List<Agendamento> agendamentos) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVOS_DIR + "/agendamentos.txt"))) {
            for (Agendamento agendamento : agendamentos) {
                // CPF_Cliente;Nome_Servico;CPF_Funcionario;Data;Hora
                writer.write(agendamento.getCliente().getCpf() + ";" + 
                             agendamento.getServico().getNome() + ";" +
                             agendamento.getFuncionario().getCpf() + ";" +
                             agendamento.getData() + ";" + 
                             agendamento.getHorario());
                writer.newLine();
            }
        }
    }

    // --- CARREGAR ---

    private static void carregarClientes(List<Cliente> clientes) throws IOException {
        File file = new File(ARQUIVOS_DIR + "/clientes.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length == 4) {
                        clientes.add(new Cliente(dados[0], dados[1], dados[2], dados[3]));
                    }
                }
            }
        }
    }

    private static void carregarFuncionarios(List<Funcionario> funcionarios) throws IOException {
        File file = new File(ARQUIVOS_DIR + "/funcionarios.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length == 5) {
                        funcionarios.add(new Funcionario(dados[0], dados[1], dados[2], dados[3], dados[4]));
                    }
                }
            }
        }
    }

    private static void carregarAgendaFuncionarios(List<Funcionario> funcionarios) throws IOException {
        File file = new File(ARQUIVOS_DIR + "/agenda_funcionarios.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length == 2) {
                        for (Funcionario f : funcionarios) {
                            if (f.getCpf().equals(dados[0])) {
                                String[] diasStr = dados[1].split(",");
                                List<Integer> dias = new ArrayList<>();
                                for(String d : diasStr) {
                                    try { dias.add(Integer.parseInt(d)); } catch(Exception e){}
                                }
                                f.getAgenda().adicionarDias(dias);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void carregarCatalogoServicos(List<Servicos> servicos) throws IOException {
        File file = new File(ARQUIVOS_DIR + "/servicos_catalogo.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(";");
                    if (dados.length == 2) {
                        double valor = Double.parseDouble(dados[1]);
                        servicos.add(new Servicos(dados[0], valor));
                    }
                }
            }
        }
    }

    private static void carregarVinculosServicos(List<Funcionario> funcionarios) throws IOException {
        File file = new File(ARQUIVOS_DIR + "/funcionario_servicos.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(";");
                    // 12345;Massagem,Drenagem
                    if (dados.length == 2) {
                        String cpf = dados[0];
                        for(Funcionario f : funcionarios) {
                            if(f.getCpf().equals(cpf)) {
                                String[] servicos = dados[1].split(",");
                                for(String s : servicos) f.adicionarServico(s);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private static void carregarAgendamentos(List<Agendamento> agendamentos, List<Cliente> clientes, List<Servicos> servicos, List<Funcionario> funcionarios) throws IOException {
        File file = new File(ARQUIVOS_DIR + "/agendamentos.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String linha;
                while ((linha = reader.readLine()) != null) {
                    String[] dados = linha.split(";");
                    // Formato novo: CPF_C; Servico; CPF_F; Data; Hora (5 campos)
                    if (dados.length == 5) {
                        Cliente c = null;
                        for(Cliente cl : clientes) if(cl.getCpf().equals(dados[0])) c = cl;

                        Servicos s = null;
                        for(Servicos sv : servicos) if(sv.getNome().equals(dados[1])) s = sv;
                        
                        Funcionario f = null;
                        for(Funcionario fn : funcionarios) if(fn.getCpf().equals(dados[2])) f = fn;

                        if (c != null && s != null && f != null) {
                            Agendamento a = new Agendamento(c, s, f, dados[3], dados[4]);
                            agendamentos.add(a);
                            c.agendar(a);
                        }
                    }
                }
            }
        }
    }
}