package input;

import java.io.BufferedReader;
import java.io.FileReader;

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
    // Getters e Setters ----------------------------------------
    public int[] getMemoria() {
        return this.memoria;
    }
    public void setMemoria(int[] memoria) {
        this.memoria = memoria;
    }
    public int getValor(int endereco) {
        if (endereco < 0 || endereco >= memoria.length) {
            throw new IndexOutOfBoundsException("Endereço fora dos limites da memória.");
        }
        return memoria[endereco];
    }
    public void setValor(int endereco, int valor) {
        if (endereco < 0 || endereco >= memoria.length) {
            throw new IndexOutOfBoundsException("Endereço fora dos limites da memória.");
        }
        memoria[endereco] = valor;
    }
}
