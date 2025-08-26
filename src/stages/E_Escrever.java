package stages;

import input.Instrucao;
import input.Registradores;

public class E_Escrever {
    public Instrucao instrucaoAtual;
    public void run(Instrucao instrucoes, Registradores registradores) {
        this.instrucaoAtual = instrucoes;

        if (instrucaoAtual != null) {
            String operacao = instrucaoAtual.getInstrucao();

            switch (operacao) {
                case "add":
                case "sub":
                case "avg":
                case "rev":
                    int result = acessador.getValorLido();
                    registradores.setValor(instrucaoAtual.getReg1(), result);
                    System.out.println("[WB] Escrevendo resultado (" + result + ") no registrador R" + instrucaoAtual.getReg1());
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
                    registradores.setValor(instrucoes.getReg1(), acessador.getValorLido2());
                    registradores.setValor(instrucoes.getReg2(), acessador.getValorLido());
                    System.out.println("[WB] Finalizando SWAP. r" + instrucoes.getReg1() + " agora eh " + acessador.getValorLido2() + " e r" + instrucoes.getReg2() + " agora eh " + acessador.getValorLido() + ".");
                    break;

                default:
                    System.out.println("[WB] Operação sem escrita em registrador: " + operacao);
            }
        } else {
            System.out.println("Nenhuma instrução para escrever nos registradores.");
        }
    }
}
