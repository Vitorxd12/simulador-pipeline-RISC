package input;

import java.io.*;

public class Registradores {
    private int[] registradores;
    public Registradores() {
        this.registradores = new int[32];
    }

    public void salvar() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/output/registradores_resultado.txt"))) {
            for (int i = 0; i < registradores.length; i++) {
                writer.write(String.valueOf(registradores[i]));
                writer.newLine(); // Adiciona uma nova linha após cada valor
            }
            System.out.println("Registradores salvos com sucesso.");
        } catch (IOException e) {
            System.out.println("Erro ao salvar os registradores: " + e.getMessage());
        }
    }

    public int getTamanho() {
        return registradores.length;
    }
    //read write
    public int getValor(int endereco) {
        if (endereco < 0){
            throw new IndexOutOfBoundsException("Endereço fora dos limites do registrador.");
        }
        while (endereco >= registradores.length) {
            endereco = endereco - registradores.length;
            System.out.println("Endereço de memória ajustado para: " + endereco);
        }
        return registradores[endereco];
    }
    public void setValor(int endereco, int valor) {
        if (endereco < 0){
            throw new IndexOutOfBoundsException("Endereço fora dos limites do registrador.");
        }
        while (endereco >= registradores.length) {
            endereco = endereco - registradores.length;
            System.out.println("Endereço de memória ajustado para: " + endereco);
        }
        registradores[endereco] = valor;
    }
}
