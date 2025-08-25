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
            switch (instrucaoAtual.getInstrucao()) {
                case ("add"):
                case ("sub"):
                case ("avg"):
                    valorR1 = registradores.getValor(instrucaoAtual.getReg1());
                    valorR2 = registradores.getValor(instrucaoAtual.getReg2());
                    valorR3 = registradores.getValor(instrucaoAtual.getReg3());

                    System.out.println("[ID] Decodificado → Operação: " + instrucao.getInstrucao()
                            + " | Reg " + instrucaoAtual.getReg1() + " = " + valorR1
                            + " | Reg " + instrucaoAtual. getReg2() + " = " + valorR2
                            + " | Reg " + instrucaoAtual. getReg3() + " = " + valorR3);
                    break;
                case ("lw"):
                case ("sw"):
                    valorR1 = registradores.getValor(instrucaoAtual.getReg1());;
                    valorR2 = instrucaoAtual.getReg2();
                    valorR3 = registradores.getValor(instrucaoAtual.getReg3());

                    System.out.println("[ID] Decodificado → Operação: " + instrucao.getInstrucao()
                            + " | Reg " + instrucaoAtual.getReg1() + " = " + valorR1
                            + " | Offset " + instrucaoAtual.getReg2() + " = " + valorR2
                            + " | Reg " + instrucaoAtual. getReg3() + " = " + valorR3);
                    break;

                case ("swap"):
                case ("rev"):
                    valorR1 = registradores.getValor(instrucaoAtual.getReg1());
                    valorR2 = registradores.getValor(instrucaoAtual.getReg2());

                    System.out.println("[ID] Decodificado → Operação: " + instrucao.getInstrucao()
                            + " | Reg " + instrucaoAtual.getReg1() + " = " + valorR1
                            + " | Reg " + instrucaoAtual. getReg2() + " = " + valorR2);
                    break;
                default:
                    System.out.println("Erro na decodificação da instrução");
            }

        } else {
            System.out.println("Nenhuma instrução para decodificar.");
        }
        return instrucaoAtual;
    }
}
