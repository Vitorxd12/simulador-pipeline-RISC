package input;

import java.io.*;

public class Registradores {
    private int[] registradores;
    public Registradores() {
        this.registradores = new int[32];
    }
    public void iniciar(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/input/registradores.txt"));
            String linha;
            System.out.println("\nIniciando Registradores");
            for (int j = 0; j < registradores.length; j++){
                System.out.printf("%4d", j);
            }
            System.out.println();
            int i = 0;
            while((linha = br.readLine()) != null){
                this.registradores[i] = Integer.parseInt(linha);
                System.out.printf("%4d", this.registradores[i]);
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // Getters e Setters ----------------------------------------
    public int[] getRegistradores() {
        return this.registradores;
    }
    public void setRegistradores(int[] memoria) {
        this.registradores = memoria;
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
