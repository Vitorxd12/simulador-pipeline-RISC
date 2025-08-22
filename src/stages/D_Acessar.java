package stages;

import input.Instrucao;


public class D_Acessar {
    public Instrucao run(Instrucao instrucao, PC pc) {
        if (instrucao != null) {
            String tipoInstrucao = instrucao.getInstrucao();
            if (tipoInstrucao.equals("sw") || tipoInstrucao.equals("lw")) {
                System.out.println("[MA] Acessando SW ou LW");
            } else {
                System.out.println("[MA] Instrução não requer acesso (ADD ou SUB)");
            }
        } else {
            System.out.println("Nenhuma instrução para acessar");
        }
        return instrucao;
    }
}
