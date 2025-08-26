package stages;

import input.Instrucao;
import input.Memoria;
import input.Registradores;

public class D_Acessar {

    public Instrucao run(Instrucao instrucao, Memoria memoria, Registradores registradores) {
        if (instrucao != null) {
            switch (instrucao.getInstrucao()) {
                case "lw":
                    //pegua o resultado do executar que é o endereço
                    int enderecoLW = instrucao.getResultado();
                    //le o valor da memoria nesse endereço e seta de volta no atributo instrução da classe resultado
                    instrucao.setResultado(memoria.getValor(enderecoLW));
                    //resultado agora é o valor lido da memoria
                    System.out.println("[MEM] Acessando memoria LW → Mem[" + enderecoLW + "] = " + instrucao.getResultado());
                    break;

                case "sw":
                    //pega o resultado do executar que é o endereço
                    int enderecoSW = instrucao.getResultado();
                    //pega o valor do registrador r1 que deve ser salvo na memoria
                    int valorASalvar = instrucao.getValorR1();
                    //salva o valor no endereço da memoria
                    memoria.setValor(enderecoSW, valorASalvar);
                    System.out.println("[MEM] Acessando memoria SW → Mem[" + enderecoSW + "] = " + valorASalvar);
                    break;

                case "add", "sub", "avg", "rev", "swap":
                    System.out.println("[MEM] Sem acesso à memória para a instrução: " + instrucao.getInstrucao());
                    break;
            }
        } else {
            System.out.println("Nenhuma instrução para acessar a memória.");
        }
        return instrucao;
    }
}
