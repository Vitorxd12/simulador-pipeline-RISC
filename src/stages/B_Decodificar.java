package stages;

import input.Registradores;
import input.Instrucao;

public class B_Decodificar {
    private int valorR1;
    private int valorR2;
    private int valorR3;


    public Instrucao run(Instrucao instrucao, Registradores registradores) {
        if (instrucao != null) {
            valorR1 = registradores.getValor(instrucao.getReg1());
            valorR2 = registradores.getValor(instrucao.getReg2());
            valorR3 = registradores.getValor(instrucao.getReg3());
            System.out.println("[ID] Decodificado → Operação: " + instrucao.getInstrucao()
                    + " | Reg " + instrucao.getReg1() + " = " + valorR1
                    + " | Reg " + instrucao.getReg2() + " = " + valorR2
                    + " | Reg " + instrucao.getReg3() + " = " + valorR3);
            instrucao.setVal(valorR1, valorR2, valorR3);
        }
        else {
            System.out.println("Nenhuma instrução para decodificar.");
        }
        return instrucao;
    }
}
