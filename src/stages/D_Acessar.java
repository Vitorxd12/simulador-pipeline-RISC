package stages;

import input.Instrucao;
import input.Memoria;
import input.Registradores;

public class D_Acessar {
    public Instrucao instrucaoAtual;
    private int valorLido;
    private int valorLido2;

    public Instrucao run(Instrucao instrucoes, Memoria memoria, C_Executar executor, Registradores registradores) {
        this.instrucaoAtual = instrucoes;
        if (instrucaoAtual != null) {

            switch (instrucaoAtual.getInstrucao()) {
                case "lw":
                    int enderecoLW = executor.getResultado();
                    if (enderecoLW >= 0 && enderecoLW < memoria.getMemoria().length) {
                        valorLido = memoria.getValor(enderecoLW);
                        System.out.println("[MEM] Acessando memoria LW → Mem[" + enderecoLW + "] = " + valorLido);
                    } else {
                        System.out.println("[MEM] Erro: Endereço de memória inválido para LW.");
                        valorLido = 0;
                    }
                    break;

                case "sw":
                    int enderecoSW = executor.getResultado();
                    int valorASalvar = registradores.getValor(instrucaoAtual.getReg1());
                    if (enderecoSW >= 0 && enderecoSW < memoria.getMemoria().length) {
                        memoria.setValor(enderecoSW, valorASalvar);
                        System.out.println("[MEM] Acessando memoria SW → Mem[" + enderecoSW + "] = " + valorASalvar);
                    } else {
                        System.out.println("[MEM] Erro: Endereço de memória inválido para SW.");
                    }
                    break;

                case "add":
                case "sub":
                case "avg":
                case "rev":
                    valorLido = executor.resultado;
                    System.out.println("[MEM] Sem acesso à memória para a instrução: " + instrucaoAtual.getInstrucao());
                    break;

                case "swap":
                    int[] valoresSwap = executor.getResultadoSwap();
                    valorLido = valoresSwap[0];
                    valorLido2 = valoresSwap[1];
                    System.out.println("[MEM] Sem acesso à memória para a instrução: " + instrucaoAtual.getInstrucao());
                    break;
            }
        } else {
            System.out.println("Nenhuma instrução para acessar a memória.");
        }
        return instrucoes;
    }

    public int getValorLido() {
        return valorLido;
    }

    public int getValorLido2() {
        return valorLido2;
    }
}
