package stages;

import input.Instrucao;
import input.Registradores;

public class E_Escrever {
    public Instrucao instrucaoAtual;
    public void run(Instrucao instrucoes, D_Acessar acessador, C_Executar executor, Registradores registradores) {
        this.instrucaoAtual = instrucoes;

        if (instrucaoAtual != null) {
            String operacao = instrucaoAtual.getInstrucao();

            switch (operacao) {
                case "add":
                case "sub":
                case "avg":
                case "rev":
                    int resultado = executor.getResultado();
                    registradores.setValor(instrucaoAtual.getReg1(), resultado);
                    System.out.println("[WB] Escrevendo resultado (" + resultado + ") no registrador R" + instrucaoAtual.getReg1());
                    break;

                case "lw":
                    int valorLido = acessador.getValorLido();
                    registradores.setValor(instrucaoAtual.getReg1(), valorLido);
                    System.out.println("[WB] Escrevendo valor da memoria (" + valorLido + ") no registrador R" + instrucaoAtual.getReg1());
                    break;

                case "sw":
                    System.out.println("[WB] SW não escreve em registradores.");
                    break;

                case "swap":
                    // Fazer logica de troca entre registradores
                    break;

                default:
                    System.out.println("[WB] Operação sem escrita em registrador: " + operacao);
            }
        } else {
            System.out.println("Nenhuma instrução para escrever nos registradores.");
        }
    }
}
