package stages;

import input.Instrucao;
import input.Registradores;

public class E_Escrever {
    public void run(Instrucao instrucao, Registradores registradores) {
        if (instrucao != null) {
            switch (instrucao.getInstrucao()) {
                case "add", "sub", "lw", "avg", "rev":
                    int valorLido = instrucao.getResultado();
                    int endereco = instrucao.getReg1();
                    registradores.setValor(endereco, valorLido);
                    System.out.println("[WB] Operação "+ instrucao.getInstrucao() +" escrevendo valor da memoria (" + valorLido + ") no registrador R" + endereco + ".");
                    break;
                case "swap":
                    int reg1 = instrucao.getReg1();
                    int reg2 = instrucao.getReg2();
                    int valor1 = instrucao.getValorR1();
                    int valor2 = instrucao.getValorR2();
                    registradores.setValor(reg1, valor2);
                    registradores.setValor(reg2, valor1);
                    System.out.println("[WB] Swap: trocou R" + reg1 + " (" + valor1 + ") com R" + reg2 + " (" + valor2 + ")");
                    break;
                default:
                    System.out.println("[WB] Operação sem escrita em registrador: " + instrucao.getInstrucao());
            }
        } else {
            System.out.println("    [NULL_WB] Nenhuma instrução para escrever nos registradores.");
        }
    }
}
