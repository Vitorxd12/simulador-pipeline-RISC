package input;

import java.io.BufferedReader;
import java.io.FileReader;

public class Memoria {
    private int[] memoria = new int[32];

    public void iniciar(){
        try{
            BufferedReader br = new BufferedReader(new FileReader("src/input/memoria.txt"));
            String linha;
            int i = 0;
            while((linha = br.readLine()) != null){
                this.memoria[i] = Integer.parseInt(linha);
                System.out.println("Memoria[" + i + "] = " + this.memoria[i]);
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
