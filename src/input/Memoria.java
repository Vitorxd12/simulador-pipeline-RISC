package input;

import java.io.*;

public class Memoria {
    private int[] memoria;

    public Memoria() {
        this.memoria = new int[32];
    }

    public void iniciar(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/input/memoria.txt"));
            String linha;
            System.out.println("\nIniciando Memoria");
            for (int j = 0; j < memoria.length; j++){
                System.out.printf("%3d ", j);
            }
            System.out.println();
            int i = 0;
            while((linha = br.readLine()) != null){
                this.memoria[i] = Integer.parseInt(linha);
                System.out.printf("%4d", this.memoria[i]);
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    // Getters e Setters ----------------------------------------
    public int[] getMemoria() {
        return this.memoria;
    }
    public void setMemoria(int[] memoria) {
        this.memoria = memoria;
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
