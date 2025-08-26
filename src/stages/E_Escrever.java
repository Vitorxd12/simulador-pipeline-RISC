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
                    System.out.println("[WB] "+ instrucao.getInstrucao() +" Escrevendo valor da memoria (" + valorLido + ") no registrador R" + endereco + ".");
                    break;
                case "swap":
                    System.out.println("logica escrever do swap não ta pront");
                    //registradores.setValor(instrucoes.getReg1(), acessador.getValorLido2());
                    //registradores.setValor(instrucoes.getReg2(), acessador.getValorLido());
                    //System.out.println("[WB] Finalizando SWAP. r" + instrucoes.getReg1() + " agora eh " + acessador.getValorLido2() + " e r" + //instrucoes.getReg2() + " agora eh " + acessador.getValorLido() + ".");
                    //break;

                default:
                    System.out.println("[WB] Operação sem escrita em registrador: " + instrucao.getInstrucao());
            }
        } else {
            System.out.println("    [NULL_WB] Nenhuma instrução para escrever nos registradores.");
        }
    }
}
