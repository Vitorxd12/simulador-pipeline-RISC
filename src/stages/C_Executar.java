package stages;

import input.Instrucao;
import input.Registradores;

public class C_Executar {
    public Instrucao run(Instrucao instrucao, PC pc) {
        if (instrucao != null) {
            int valR1 = instrucao.getValorR1();
            int valR2 = instrucao.getValorR2();
            int valR3 = instrucao.getValorR3();

            switch (instrucao.getInstrucao()) {
                case "add":
                    instrucao.setResultado(valR2 + valR3);

                    System.out.println("[EX]" + pc.getValor() + " Executando ADD → x" + instrucao.getReg1()
                            + " = " + valR2 + " + " + valR3
                            + " | Resultado =" + instrucao.getResultado());
                    break;

                case "sub":
                    instrucao.setResultado(valR2 - valR3);

                    System.out.println("[EX]" + pc.getValor() + " Executando SUB → x" + instrucao.getReg1()
                            + " = " + valR2 + " - " + valR3
                            + " | Resultado =" + instrucao.getResultado());
                    break;

                case "lw":
                    int enderecoLW = valR3 + valR2;
                    System.out.println("[EX] Calculando endereço LW → Mem[" + enderecoLW + "]");
                    instrucao.setResultado(enderecoLW);
                    break;

                case "sw":
                    int enderecoSW = valR3 + valR2;
                    System.out.println("[EX] Calculando endereço SW → Mem[" + enderecoSW + "] = " + valR1);
                    instrucao.setResultado(enderecoSW);
                    break;

                case "swap":
                    System.out.println("swap não implementado");
                    //int resultadoSwap1 = valR1;
                    //int resultadoSwap2 = valR2;
                    //System.out.println("[EX] Executando SWAP -> Valores: " + valR1 + " e " + valR2);
                    //instrucao.setVal(resultadoSwap2, resultadoSwap1, 0);
                    //break;

                case "avg":
                    int resultado = (valR2 + valR3) / 2;
                    System.out.println("[EX] Executando AVG -> r" + instrucao.getReg1() + " = (" + valR2 + " + " + valR3 + ") / 2 | Resultado inteiro = " + resultado);
                    instrucao.setResultado(resultado);
                    break;

                case "rev":
                    int original = valR2;
                    int reverso = 0;
                    for (int i = 0; i < 4; i++) {
                        reverso <<= 8;
                        reverso |= (original & 0xFF);
                        original >>= 8;
                    }
                    instrucao.setResultado(reverso);
                    System.out.println("[EX] Executando REV -> revertendo bits de r" + instrucao.getReg2() + " = " + valR2 + " | Resultado = " + instrucao.getResultado());
                    break;

                default:
                    System.out.println("[EX]" + pc.getValor() + " Operação não suportada: " + instrucao.getInstrucao());
            }
        } else {
            System.out.println("Nenhuma instrução para executar.");
        }
        return instrucao;
    }
}
