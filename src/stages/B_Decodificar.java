package stages;

import input.Registradores;
import input.Instrucao;

public class B_Decodificar {
    private Instrucao instrucaoAtual;
    private int valorR1;
    private int valorR2;
    private int valorR3;


    public Instrucao run(Instrucao instrucao, Registradores registradores, PC pc) {
        this.instrucaoAtual = instrucao;

        if (instrucaoAtual != null) {
            // Decodifica os registradores
            valorR1 = registradores.getValor(instrucaoAtual.getReg1());
            valorR2 = registradores.getValor(instrucaoAtual.getReg2());
            valorR3 = registradores.getValor(instrucaoAtual.getReg3());

            System.out.println("[ID]" + pc.getValor() +  " Decodificado → Operação: " + instrucao.getInstrucao()
                    + " | Reg " + instrucaoAtual.getReg1() + " = " + valorR1
                    + " | Reg " + instrucaoAtual. getReg2() + " = " + valorR2
                    + " | Reg " + instrucaoAtual. getReg3() + " = " + valorR3);
            instrucaoAtual.setVal(valorR1, valorR2, valorR3);
        } else {
            System.out.println("Nenhuma instrução para decodificar.");
        }
        return instrucaoAtual;
    }
}
