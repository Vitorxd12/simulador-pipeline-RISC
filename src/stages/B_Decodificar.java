package stages;

import input.Memoria;
import input.Registradores;
import input.Instrucao;

public class B_Decodificar {
    public Instrucao instrucaoAtual;
    public int valorR1;
    public int valorR2;
    public int valorR3;


    public Instrucao run(Instrucao instrucao, Registradores registradores) {
        this.instrucaoAtual = instrucao;

        if (instrucaoAtual != null) {
            // Decodifica os registradores
            valorR1 = registradores.getValor(instrucaoAtual.getReg1());
            valorR2 = registradores.getValor(instrucaoAtual.getReg2());
            valorR3 = registradores.getValor(instrucaoAtual.getReg3());

            System.out.println("[ID] Decodificado → Operação: " + instrucao.getInstrucao()
                    + " | Reg " + instrucaoAtual.getReg1() + " = " + valorR1
                    + " | Reg " + instrucaoAtual. getReg2() + " = " + valorR2
                    + " | Reg " + instrucaoAtual. getReg3() + " = " + valorR3);
        } else {
            System.out.println("Nenhuma instrução para decodificar.");
        }
        return instrucaoAtual;
    }

    public int getValorR1() {
        return valorR1;
    }
    public int getValorR2() {
        return valorR2;
    }
    public int getValorR3() {
        return valorR3;
    }
}
