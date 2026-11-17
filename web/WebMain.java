import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

/**
 * Ponte para rodar o simulador ORIGINAL a partir do servidor web (web/Servidor.java).
 *
 * Não altera nem reimplementa nada do motor: só
 *   1) grava as entradas vindas da página em src/input/*.txt (onde o Parser lê),
 *   2) chama Main.main() do jeito que está,
 *   3) copia src/output/*.txt + tudo que foi impresso no console para uma pasta
 *      que o servidor lê e devolve para a página.
 *
 * Uso: WebMain <instrucoes> <memoria> <registradores> <pastaSaida>
 */
public class WebMain {
    public static void main(String[] args) throws Exception {
        String outDir = args.length > 3 ? args[3] : "web-out";

        Files.createDirectories(Paths.get("src/input"));
        Files.createDirectories(Paths.get("src/output"));
        Files.createDirectories(Paths.get(outDir));
        escrever("src/input/instrucoes.txt", args[0]);
        escrever("src/input/memoria.txt", args[1]);
        escrever("src/input/registradores.txt", args[2]);
        for (String f : new String[]{"simulacao_log.txt", "registradores_resultado.txt", "memoria_resultado.txt"}) {
            Files.deleteIfExists(Paths.get("src/output", f));
            Files.deleteIfExists(Paths.get(outDir, f));
        }

        // captura stdout + stderr inteiros (o que apareceria no terminal)
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        PrintStream oldOut = System.out, oldErr = System.err;
        PrintStream cap = new PrintStream(new Tee(buf, oldOut), true, StandardCharsets.UTF_8);
        System.setOut(cap);
        System.setErr(cap);
        String status = "OK";
        try {
            Main.main(new String[0]);
        } catch (Throwable t) {
            status = "EXCEPTION";
            t.printStackTrace();
        } finally {
            cap.flush();
            System.setOut(oldOut);
            System.setErr(oldErr);
        }

        Files.write(Paths.get(outDir, "console.txt"), buf.toByteArray());
        for (String f : new String[]{"simulacao_log.txt", "registradores_resultado.txt", "memoria_resultado.txt"}) {
            Path p = Paths.get("src/output", f);
            if (Files.exists(p)) Files.copy(p, Paths.get(outDir, f), StandardCopyOption.REPLACE_EXISTING);
        }
        Files.write(Paths.get(outDir, "status.txt"), status.getBytes(StandardCharsets.UTF_8));
    }

    private static void escrever(String caminho, String conteudo) throws IOException {
        Files.write(Paths.get(caminho), conteudo.getBytes(StandardCharsets.UTF_8));
    }

    private static class Tee extends OutputStream {
        private final OutputStream a, b;
        Tee(OutputStream a, OutputStream b) { this.a = a; this.b = b; }
        @Override public void write(int x) throws IOException { a.write(x); b.write(x); }
        @Override public void write(byte[] x, int o, int n) throws IOException { a.write(x, o, n); b.write(x, o, n); }
        @Override public void flush() throws IOException { a.flush(); b.flush(); }
    }
}
