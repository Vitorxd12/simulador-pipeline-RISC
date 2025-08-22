package stages;

import input.Instrucao;
import input.Registradores;

public class C_Executar {
    public Instrucao instrucaoAtual;
    public int resultado;

    public Instrucao run(Instrucao instrucao, B_Decodificar decodificador, Registradores registradores) {
        this.instrucaoAtual = instrucao;
        resultado = 0;

        if (instrucaoAtual != null) {
            int valR1 = decodificador.getValorR1();
            int valR2 = decodificador.getValorR2();
            int valR3 = decodificador.getValorR3();

            switch (instrucaoAtual.getInstrucao()) {
                case "add":
                    resultado = valR2 + valR3;
                    System.out.println("[EX] Executando ADD → x" + instrucaoAtual.getReg1()
                            + " = " + valR2 + " + " + valR3
                            + " | Resultado=" + resultado);
                    break;

                case "sub":
                    resultado = valR2 - valR3;
                    System.out.println("[EX] Executando SUB → x" + instrucaoAtual.getReg1()
                            + " = " + valR2 + " - " + valR3
                            + " | Resultado=" + resultado);
                    break;

                case "lw":
                    int enderecoLW = valR3 + valR2; // reg3 + offset
                    System.out.println("[EX] Calculando endereço LW → Mem[" + enderecoLW + "]");
                    break;

                case "sw":
                    int enderecoSW = valR3 + valR2; // reg3 + offset
                    System.out.println("[EX] Calculando endereço SW → Mem[" + enderecoSW + "] = " + valR1);
                    break;

                default:
                    System.out.println("[EX] Operação não suportada: " + instrucaoAtual.getInstrucao());
            }
        } else {
            System.out.println("Nenhuma instrução para executar.");
        }

        return instrucaoAtual;
    }

    public int getResultado() {
        return resultado;
    }
}
