import input.Instrucao;
import input.Memoria;
import input.Registradores;
import stages.*;

import java.util.List;


public class Simulador {
    private PC pc;
    private Memoria memoria;
    private Registradores registradores;
    private List<Instrucao> instrucoes;

    private Parser parser = new Parser();

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
        this.memoria = parser.iniciarMemoria();;
        this.registradores = parser.iniciarRegistradores();
        this.instrucoes = parser.iniciarInstrucoes();

        this.buscar = new A_Buscar();
        this.decodificar = new B_Decodificar();
        this.executar = new C_Executar();
        this.acessar = new D_Acessar();
        this.escrever = new E_Escrever();

    }
    //------------------- Run ------------------------
    public void run(){
        System.out.println("\nSimulador iniciado.");
        while (pc.getValor() < instrucoes.size() * 4 + 16) {
            System.out.println("------" + pc.getValor() + "------");
            escrever.run(rescrever, registradores);
            rescrever = acessar.run(racessar, memoria);
            racessar = executar.run(rexecutar);
            rexecutar = decodificar.run(rdecodificar, registradores);
            rdecodificar = buscar.run(instrucoes, pc);
        }
        registradores.salvar();
        memoria.salvar();
        System.out.println("Simulação concluída.");
    }
}


