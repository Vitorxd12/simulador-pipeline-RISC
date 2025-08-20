package stages;

import input.Registradores;
import input.Instrucoes;

public class B_Decodificar {
    private Registradores registradores;
    private Instrucoes instrucaoAtual;
    public int valorR1;
    public int valorR2;

    public B_Decodificar() {
        this.instrucaoAtual = A_Buscar.getInstrucaoAtual();
    }

    public void executar() {
        valorR1 = registradores.getValor();
        valorR2 = registradores.getValor();
    }

}
