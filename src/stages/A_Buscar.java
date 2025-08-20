package stages;

import input.Instrucoes;
import input.Memoria;
import input.Registradores;

import java.util.List;

public class A_Buscar {
    private PC pc;
    private Memoria memoria;
    private Registradores registradores;
    private Instrucoes instrucaoAtual;

    public A_Buscar(PC pc) {
        this.pc = pc;
    }

    public void executar(List<Instrucoes> instrucoes) {
        int endereco = pc.getValor() / 4;
        if (endereco < instrucoes.size()) {
            instrucaoAtual = instrucoes.get(endereco);
            System.out.println("Buscar: " + instrucaoAtual);
        }
    }

    public Instrucoes getInstrucaoAtual() {
        return instrucaoAtual;
    }
}
