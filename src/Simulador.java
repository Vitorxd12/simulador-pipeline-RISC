import input.Instrucoes;
import input.Memoria;
import input.Registradores;
import stages.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class Simulador {
    private PC pc;
    private Memoria memoria;
    private Registradores registradores;
    private List<Instrucoes> instrucoes;

    private A_Buscar buscar;
    private B_Decodificar decodificar;
    private C_Executar executar;
    private D_Acessar acessar;
    private E_Escrever escrever;

    private instrucoes 

    public Simulador() {
        this.pc = new PC();
        this.memoria = new Memoria();
        this.registradores = new Registradores();
        this.instrucoes = new ArrayList<>();

        this.buscar = new Buscar();
        this.decodificar = new Decodificar();
        this.executar = new Executar();
        this.acessar = new Acessar();
        this.escrever = new Escrever();
    }

    //------------------- Run ------------------------

    public void run(){
        while (pc.getValor() < instrucoes.size() * 4) {
            escrita.executar(rescrita);
            rescrita = acesso.executar(racesso);
            racesso = executar.executar(rexecutar);
            rexecutar = decodificar.executar(rdecodificar);
            rdecodificar = buscar.executar();
        System.out.println("Simulação concluída.");

    }


    //--------------Carregar Progama------------------


    public void carregarProgama() {
        memoria.iniciar();
        registradores.iniciar();

        try (BufferedReader br = new BufferedReader(new FileReader("src/input/instrucoes.txt"))) {
            String linha;
            int i = 0;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.trim().split("\\s+");

                String op = partes[0];
                String p1 = partes[1];
                String p2 = partes[2];
                String p3 = partes.length > 3 ? partes[3] : null;

                Instrucoes instrucao = new Instrucoes(op, p1, p2, p3);
                instrucoes.add(instrucao);

                System.out.println("Instrução[" + i + "] = " + op + " " + p1 + " " + p2 + " " + p3);
                i++;
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar instruções: " + e.getMessage());
        }
    }
}


