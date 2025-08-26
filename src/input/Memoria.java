package input;

import java.io.*;

public class Memoria {
    private int[] memoria;

    public Memoria() {
        this.memoria = new int[32];
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/output/memoria_resultado.txt"))) {
            for (int i = 0; i < memoria.length; i++) {
                writer.write(String.valueOf(memoria[i]));
                writer.newLine();
            }
            System.out.println("Memoria salva com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar a memoria: " + e.getMessage());
        }
    }
    public int[] getMemoria() {
        return memoria;
    }
    public int getTamanho() {
        return memoria.length;
    }
    //read write
    public int getValor(int endereco) {
        if (endereco < 0){
            throw new IndexOutOfBoundsException("Endereço fora dos limites da memória.");
        }
        while (endereco >= memoria.length) {
            endereco = endereco - memoria.length;
            System.out.println("Endereço de memória ajustado para: " + endereco);
        }
        return memoria[endereco];
    }
    public void setValor(int endereco, int valor) {
        if (endereco < 0){
            throw new IndexOutOfBoundsException("Endereço fora dos limites da memória.");
        }
        while (endereco >= memoria.length) {
            endereco = endereco - memoria.length;
            System.out.println("Endereço de memória ajustado para: " + endereco);
        }
        memoria[endereco] = valor;
    }
}
