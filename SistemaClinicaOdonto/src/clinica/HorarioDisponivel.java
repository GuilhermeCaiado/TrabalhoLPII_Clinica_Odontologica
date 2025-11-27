package clinica;

public class HorarioDisponivel {
    private String data;
    private String horario;

    public HorarioDisponivel(String data, String horario) {
        this.data = data;
        this.horario = horario;
    }

    public String getData() {
        return data;
    }

    public String getHorario() {
        return horario;
    }

    @Override
    public String toString() {
        return "Data: " + data + ", Horário: " + horario;
    }
}