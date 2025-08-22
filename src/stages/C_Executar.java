package stages;

import input.Instrucao;

public class C_Executar {
    private Instrucao instrucaoAtual;

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
                    int enderecoLW = valR3 + valR2; // reg3 + offset
                    System.out.println("[EX]" + pc.getValor() + " Calculando endereço LW → Mem[" + enderecoLW + "]");
                    break;

                case "sw":
                    int enderecoSW = valR3 + valR2; // reg3 + offset
                    System.out.println("[EX]" + pc.getValor() + " Calculando endereço SW → Mem[" + enderecoSW + "] = " + valR1);
                    break;

                default:
                    System.out.println("[EX]" + pc.getValor() + " Operação não suportada: " + instrucaoAtual.getInstrucao());
            }
        } else {
            System.out.println("Nenhuma instrução para executar.");
        }
        return instrucaoAtual;
    }
}
