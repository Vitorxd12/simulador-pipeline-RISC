package stages;

import input.Instrucao;

public class C_Executar {
    public Instrucao instrucaoAtual;
    public int resultado;
    public int[] resultadoSwap = new int[2];

    public Instrucao run(Instrucao instrucao, PC pc) {
        this.instrucaoAtual = instrucao;

        if (instrucaoAtual != null) {
            int valR1 = instrucao.getValorR1();
            int valR2 = instrucao.getValorR2();
            int valR3 = instrucao.getValorR3();

            switch (instrucaoAtual.getInstrucao()) {
                case "add":
                    System.out.println("[EX]" + pc.getValor() + " Executando ADD → x" + instrucaoAtual.getReg1()
                            + " = " + valR2 + " + " + valR3
                            + " | Resultado=" + instrucao.getResultado());
                    break;

                case "sub":
                    instrucao.setResultado(valR2 - valR3);
                    System.out.println("[EX]" + pc.getValor() + " Executando SUB → x" + instrucaoAtual.getReg1()
                            + " = " + valR2 + " - " + valR3
                            + " | Resultado=" + instrucao.getResultado());
                    break;

                case "lw":
                    int enderecoLW = valR3 + valR2;
                    System.out.println("[EX] Calculando endereço LW → Mem[" + enderecoLW + "]");
                    resultado = enderecoLW;
                    break;

                case "sw":
                    int enderecoSW = valR3 + valR2;
                    System.out.println("[EX] Calculando endereço SW → Mem[" + enderecoSW + "] = " + valR1);
                    resultado = enderecoSW;
                    break;

                case "swap":
                    resultadoSwap[0] = valR1;
                    resultadoSwap[1] = valR2;
                    System.out.println("[EX] Executando SWAP -> Valores: " + valR1 + " e " + valR2);
                    break;

                case "avg":
                    resultado = (valR2 + valR3) / 2;
                    System.out.println("[EX] Executando AVG -> r" + instrucaoAtual.getReg1() + " = (" + valR2 + " + " + valR3 + ") / 2 | Resultado inteiro = " + resultado);
                    break;

                case "rev":
                    int original = valR2;
                    int reverso = 0;
                    for (int i = 0; i < 4; i++) {
                        reverso <<= 8;
                        reverso |= (original & 0xFF);
                        original >>= 8;
                    }
                    resultado = reverso;
                    System.out.println("[EX] Executando REV -> revertendo bits de r" + instrucaoAtual.getReg2() + " = " + registradores.getValor(instrucaoAtual.getReg2()) + " | Resultado = " + resultado);
                    break;

                default:
                    System.out.println("[EX]" + pc.getValor() + " Operação não suportada: " + instrucaoAtual.getInstrucao());
            }
        } else {
            System.out.println("Nenhuma instrução para executar.");
        }
        return instrucaoAtual;
    }

    public int getResultado() {
        return resultado;
    }

    public int[] getResultadoSwap() {
        return resultadoSwap;
    }
}
