import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static void main(String[] args) {
        Simulador simulador = new Simulador();
        try (BufferedReader br = new BufferedReader(new FileReader("src/input/memoria.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                simulador.addMemoria(linha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/input/registradores.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                simulador.addRegistradores(linha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/input/instrucoes.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                simulador.addInstrucoes(linha);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        simulador.run(); // Assuming there's a run method in Simulador to execute the simulation
    }
}