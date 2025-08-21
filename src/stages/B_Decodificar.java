package stages;

import input.Memoria;
import input.Registradores;
import input.Instrucoes;

public class B_Decodificar {
    private Registradores registradores;
    private Instrucoes instrucaoAtual;
    public int valorR1;
    public int valorR2;


    public Instrucoes run(Instrucoes instrucoes, C_Executar executar, Memoria memoria) {
        return instrucoes;
    }

}
