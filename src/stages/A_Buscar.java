package stages;

import input.Instrucao;

import java.util.List;

public class A_Buscar {
    private Instrucao instrucaoAtual;

    public Instrucao run(List<Instrucao> instrucoes, PC pc) {
        int endereco = pc.getValor() / 4;
        if (endereco < instrucoes.size()) {
            instrucaoAtual = instrucoes.get(endereco);
            System.out.println("[IF] Instrução buscada → " + instrucaoAtual.toString() + " ");
            pc.incrementar();
            return instrucaoAtual;
        } else {
            pc.incrementar();
            System.out.println("    [NULL_IF] Nenhuma instrução encontrada no endereço " + endereco);
            return null;
        }
    }
}
