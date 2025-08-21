package stages;

import input.Instrucoes;

import java.util.List;

public class A_Buscar {
    private Instrucoes instrucaoAtual;

    public Instrucoes run(List<Instrucoes> instrucoes, PC pc) {
        int endereco = pc.getValor() / 4;
        if (endereco < instrucoes.size()) {
            instrucaoAtual = instrucoes.get(endereco);
            System.out.println("Buscar: " + instrucaoAtual);
        }
        pc.incrementar();
        return instrucaoAtual;
    }
}
