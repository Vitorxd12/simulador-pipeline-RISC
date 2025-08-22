package stages;

import input.Instrucao;
import input.Memoria;

public class E_Escrever {
    public void run(Instrucao instrucao, Memoria memoria, PC pc) {
        if (instrucao != null) {
            System.out.println("[WB] Escrevendo o valor " + instrucao.getResultado());
        } else {
            System.out.println("Nenhuma instrução para escrever");
        }
    }
}
