package stages;

import input.Memoria;
import input.Registradores;
import input.Instrucao;

public class B_Decodificar {
    public Instrucao instrucaoAtual;
    public int valorR1;
    public int valorR2;
    public int valorR3;


    public Instrucao run(Instrucao instrucao, C_Executar executar, Memoria memoria) {
        this.instrucaoAtual = instrucao;

        if (instrucaoAtual != null) {
            // Decodifica os registradores
            valorR1 = Registradores.getValor(instrucaoAtual.getReg1());
            valorR2 = Registradores.getValor(instrucaoAtual.getReg2());
            valorR3 = Registradores.getValor(instrucaoAtual.getReg3());

            System.out.println("Decodificar: " + instrucaoAtual + " R1: " + valorR1 + " R2: " + valorR2 + " R3: " + valorR3);
        } else {
            System.out.println("Nenhuma instrução para decodificar.");
        }

        return instrucaoAtual;
    }

}
