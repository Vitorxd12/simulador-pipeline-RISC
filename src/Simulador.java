import java.util.ArrayList;

public class Simulador {
    private ArrayList<Integer> registradores;
    private ArrayList<String> instrucoes;
    private ArrayList<Integer> memoria;

    public Simulador() {
        instrucoes = new ArrayList<>();
        memoria = new ArrayList<>();
        registradores = new ArrayList<>();
    }

    public void addRegistradores(String linha) {
        registradores.add(Integer.parseInt(linha));

    }

    public void addMemoria(String linha) {
        memoria.add(Integer.parseInt(linha));
    }

    public void addInstrucoes(String linha) {
        instrucoes.add(linha);
    }

    public void run() {
        System.out.println("Simulador iniciado com " +
                "Registradores: " + registradores.size() +
                ", Instruções: " + instrucoes.size() +
                ", Memória: " + memoria.size());
    }
}
