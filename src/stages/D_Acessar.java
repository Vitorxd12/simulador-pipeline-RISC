package stages;

import input.Instrucao;
import input.Memoria;
import input.Registradores;

public class D_Acessar {

    public Instrucao run(Instrucao instrucao, Memoria memoria, Registradores registradores) {
        if (instrucao != null) {
            switch (instrucao.getInstrucao()) {
                case "lw":
                    int enderecoLW = instrucao.getResultado();
                    int valorNoEnderecoLW = memoria.getValor(enderecoLW);
                    System.out.println("[MEM] Acessando memoria LW → Mem[" + enderecoLW + "] = " + valorNoEnderecoLW);
                    break;

                case "sw":
                    int enderecoSW = instrucao.getResultado();
                    int valorASalvar = instrucao.getValorR1();
                    if (enderecoSW >= 0 && enderecoSW < memoria.getMemoria().length) {
                        memoria.setValor(enderecoSW, valorASalvar);
                        System.out.println("[MEM] Acessando memoria SW → Mem[" + enderecoSW + "] = " + valorASalvar);
                    } else {
                        System.out.println("[MEM] Erro ao acessar memória SW → Endereço inválido: " + enderecoSW);
                    }
                    break;

                case "add":
                case "sub":
                case "avg":
                case "rev":
                    System.out.println("rev não implementado no acessar");
                    //valorLido = instrucao.getResultado();
                    //System.out.println("[MEM] Sem acesso à memória para a instrução: " + instrucao.getInstrucao());
                    break;
                case "swap":
                    System.out.println("swap não implementado no acessar");
                    //int[] valoresSwap = executor.getResultadoSwap();
                    //valorLido = valoresSwap[0];
                    //valorLido2 = valoresSwap[1];
                    //System.out.println("[MEM] Sem acesso à memória para a instrução: " + instrucao.getInstrucao());
                    break;
            }
        } else {
            System.out.println("Nenhuma instrução para acessar a memória.");
        }
        return instrucao;
    }
}
