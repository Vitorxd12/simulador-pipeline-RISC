import input.Instrucao;
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
    private List<Instrucao> instrucoes;

    private A_Buscar buscar;
    private B_Decodificar decodificar;
    private C_Executar executar;
    private D_Acessar acessar;
    private E_Escrever escrever;

    private Instrucao rdecodificar;
    private Instrucao rexecutar;
    private Instrucao racessar;
    private Instrucao rescrever;

    //cada classe vai receber o re da classe anterior e vai retornar um objeto da classe instrução


    public Simulador() {
        this.pc = new PC();
        this.memoria = new Memoria();
        this.registradores = new Registradores();
        this.instrucoes = new ArrayList<>();

        this.buscar = new A_Buscar();
        this.decodificar = new B_Decodificar();
        this.executar = new C_Executar();
        this.acessar = new D_Acessar();
        this.escrever = new E_Escrever();

    }

    //------------------- Run ------------------------
    public void run(){
        System.out.println("Simulador iniciado.");
        while (pc.getValor() < instrucoes.size() * 4 + 16) {
            escrever.run(rescrever, acessar, registradores);
            rescrever = acessar.run(racessar, memoria, executar, registradores);
            racessar = executar.run(rexecutar, pc, registradores);
            rexecutar = decodificar.run(rdecodificar, registradores);
            rdecodificar = buscar.run(instrucoes, pc);
        }
        System.out.println();
        registradores.salvar();
        memoria.salvar();
        System.out.println("Simulação concluída.");
    }


    //--------------Carregar Programa------------------


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
                String p2 = partes.length > 2 ? partes[2] : " ";
                String p3 = partes.length > 3 ? partes[3] : " ";
                System.out.print("\nInstrução[" + i + "] = " + op + " " + p1 + " " + p2 + " " + p3);

                Instrucao instrucao = new Instrucao(op, p1, p2, p3);
                instrucoes.add(instrucao);

                i++;
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar instruções: " + e.getMessage());
        }
    }
}


