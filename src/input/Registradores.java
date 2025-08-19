package input;

import java.io.BufferedReader;
import java.io.FileReader;

public class Registradores {
    private int[] registradores = new int[32];
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
}
