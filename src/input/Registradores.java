package input;

import java.io.BufferedReader;
import java.io.FileReader;

public class Registradores {
    private int[] registradores;
    public Registradores() {
        this.registradores = new int[32];
    }
    public void iniciar(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/input/registradores.txt"));
            String linha;
            int i = 0;
            while((linha = br.readLine()) != null){
                this.registradores[i] = Integer.parseInt(linha);
                System.out.println("Registrador[" + i + "] = " + this.registradores[i]);
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Getters e Setters ----------------------------------------
    public int[] getRegistradores() {
        return this.registradores;
    }
    public void setRegistradores(int[] memoria) {
        this.registradores = memoria;
    }
    public int getValor(int endereco) {
        if (endereco < 0 || endereco >= registradores.length) {
            throw new IndexOutOfBoundsException("Endereço fora dos limites dos registradores.");
        }
        return registradores[endereco];
    }
    public void setValor(int endereco, int valor) {
        if (endereco < 0 || endereco >= registradores.length) {
            throw new IndexOutOfBoundsException("Endereço fora dos limites dos registradores.");
        }
        registradores[endereco] = valor;
    }
}
