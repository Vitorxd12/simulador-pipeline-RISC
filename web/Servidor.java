import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Servidor HTTP mínimo (só JDK, sem dependências) para o deploy em Docker.
 *
 *   GET  /              -> arquivos estáticos do frontend (pasta STATIC_DIR, padrão ./docs)
 *   POST /api/simular   -> roda o simulador ORIGINAL (Main.main, de src/, sem alterações)
 *
 * Cada simulação roda num processo Java separado, numa pasta temporária própria,
 * porque o motor lê/escreve caminhos relativos (src/input, src/output) e usa System.out.
 * Assim execuções simultâneas não se misturam e um loop infinito é morto pelo timeout.
 */
public class Servidor {
    private static final int MAX_BODY = 256 * 1024;
    private static final long TIMEOUT_S = 15;
    private static final String[] SAIDAS = {"console.txt", "simulacao_log.txt",
            "registradores_resultado.txt", "memoria_resultado.txt", "status.txt"};

    private static Path staticDir;
    private static String classpath;
    private static final Semaphore vagas = new Semaphore(Integer.parseInt(env("MAX_CONCORRENTES", "4")));

    public static void main(String[] args) throws IOException {
        int porta = Integer.parseInt(env("PORT", "8080"));
        staticDir = Paths.get(env("STATIC_DIR", "docs")).toAbsolutePath().normalize();
        // absoluto: o subprocesso roda dentro de uma pasta temporária
        StringJoiner cp = new StringJoiner(File.pathSeparator);
        for (String p : System.getProperty("java.class.path").split(File.pathSeparator))
            cp.add(Paths.get(p).toAbsolutePath().toString());
        classpath = cp.toString();

        HttpServer server = HttpServer.create(new InetSocketAddress(porta), 0);
        server.createContext("/api/simular", Servidor::simular);
        server.createContext("/healthz", ex -> responder(ex, 200, "text/plain", "ok".getBytes(StandardCharsets.UTF_8)));
        server.createContext("/", Servidor::estatico);
        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();
        System.out.println("Simulador web em http://0.0.0.0:" + porta + "  (static: " + staticDir + ")");
    }

    // ------------------------------------------------------------------ API
    private static void simular(HttpExchange ex) throws IOException {
        try {
            if (!"POST".equals(ex.getRequestMethod())) {
                responder(ex, 405, "text/plain", "use POST".getBytes(StandardCharsets.UTF_8));
                return;
            }
            byte[] corpo = ex.getRequestBody().readNBytes(MAX_BODY + 1);
            if (corpo.length > MAX_BODY) {
                json(ex, 413, Map.of("erro", "entrada muito grande"));
                return;
            }
            Map<String, String> form = parseForm(new String(corpo, StandardCharsets.UTF_8));
            if (!vagas.tryAcquire(10, TimeUnit.SECONDS)) {
                json(ex, 503, Map.of("erro", "servidor ocupado, tente de novo"));
                return;
            }
            try {
                json(ex, 200, rodarSimulador(
                        form.getOrDefault("instrucoes", ""),
                        form.getOrDefault("memoria", ""),
                        form.getOrDefault("registradores", "")));
            } finally {
                vagas.release();
            }
        } catch (Exception e) {
            json(ex, 500, Map.of("erro", String.valueOf(e.getMessage())));
        } finally {
            ex.close();
        }
    }

    private static Map<String, String> rodarSimulador(String ins, String mem, String reg) throws Exception {
        Path dir = Files.createTempDirectory("sim-");
        try {
            Path out = dir.resolve("web-out");
            String java = Paths.get(System.getProperty("java.home"), "bin", "java").toString();
            ProcessBuilder pb = new ProcessBuilder(java, "-Xmx64m", "-Xss1m", "-cp", classpath,
                    "WebMain", ins, mem, reg, out.toString());
            pb.directory(dir.toFile());
            pb.redirectErrorStream(true);
            pb.redirectOutput(dir.resolve("proc.txt").toFile());
            long t0 = System.nanoTime();
            Process p = pb.start();
            boolean terminou = p.waitFor(TIMEOUT_S, TimeUnit.SECONDS);
            if (!terminou) p.destroyForcibly().waitFor();
            long ms = (System.nanoTime() - t0) / 1_000_000;

            Map<String, String> r = new LinkedHashMap<>();
            for (String f : SAIDAS) {
                Path pf = out.resolve(f);
                if (Files.exists(pf)) r.put(f, Files.readString(pf, StandardCharsets.UTF_8));
            }
            r.put("exitCode", terminou ? String.valueOf(p.exitValue()) : "timeout");
            r.put("ms", String.valueOf(ms));
            if (!terminou) r.put("erro", "tempo limite de " + TIMEOUT_S + "s excedido");
            else if (!r.containsKey("console.txt")) r.put("erro", Files.readString(dir.resolve("proc.txt")));
            return r;
        } finally {
            apagar(dir);
        }
    }

    // ------------------------------------------------------------ estáticos
    private static final Map<String, String> TIPOS = Map.of(
            "html", "text/html; charset=utf-8", "css", "text/css; charset=utf-8",
            "js", "text/javascript; charset=utf-8", "png", "image/png", "svg", "image/svg+xml",
            "ico", "image/x-icon", "txt", "text/plain; charset=utf-8");

    private static void estatico(HttpExchange ex) throws IOException {
        try {
            String m = ex.getRequestMethod();
            if (!"GET".equals(m) && !"HEAD".equals(m)) {
                responder(ex, 405, "text/plain", new byte[0]);
                return;
            }
            String caminho = URLDecoder.decode(ex.getRequestURI().getPath(), StandardCharsets.UTF_8);
            if (caminho.endsWith("/")) caminho += "index.html";
            Path f = staticDir.resolve(caminho.substring(1)).normalize();
            if (!f.startsWith(staticDir) || !Files.isRegularFile(f)) {
                responder(ex, 404, "text/plain", "404".getBytes(StandardCharsets.UTF_8));
                return;
            }
            String nome = f.getFileName().toString();
            String ext = nome.contains(".") ? nome.substring(nome.lastIndexOf('.') + 1) : "";
            ex.getResponseHeaders().set("Cache-Control", "no-cache");
            responder(ex, 200, TIPOS.getOrDefault(ext, "application/octet-stream"),
                    "HEAD".equals(m) ? null : Files.readAllBytes(f));
        } finally {
            ex.close();
        }
    }

    // --------------------------------------------------------------- util
    private static void responder(HttpExchange ex, int code, String tipo, byte[] corpo) throws IOException {
        ex.getResponseHeaders().set("Content-Type", tipo);
        ex.getResponseHeaders().set("X-Content-Type-Options", "nosniff");
        if (corpo == null) {
            ex.sendResponseHeaders(code, -1);
            return;
        }
        ex.sendResponseHeaders(code, corpo.length == 0 ? -1 : corpo.length);
        if (corpo.length > 0) try (OutputStream os = ex.getResponseBody()) { os.write(corpo); }
    }

    private static void json(HttpExchange ex, int code, Map<String, String> m) throws IOException {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, String> e : m.entrySet()) {
            if (sb.length() > 1) sb.append(',');
            sb.append(jsonStr(e.getKey())).append(':').append(jsonStr(e.getValue()));
        }
        responder(ex, code, "application/json; charset=utf-8", sb.append('}').toString().getBytes(StandardCharsets.UTF_8));
    }

    private static String jsonStr(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20 || c == '<' || c == '>' || c == '&') sb.append(String.format("\\u%04x", (int) c));
                    else sb.append(c);
            }
        }
        return sb.append('"').toString();
    }

    private static Map<String, String> parseForm(String s) {
        Map<String, String> m = new HashMap<>();
        for (String par : s.split("&")) {
            if (par.isEmpty()) continue;
            int i = par.indexOf('=');
            String k = URLDecoder.decode(i < 0 ? par : par.substring(0, i), StandardCharsets.UTF_8);
            String v = i < 0 ? "" : URLDecoder.decode(par.substring(i + 1), StandardCharsets.UTF_8);
            m.put(k, v);
        }
        return m;
    }

    private static void apagar(Path dir) {
        try (var w = Files.walk(dir)) {
            w.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
        } catch (IOException ignored) {
        }
    }

    private static String env(String k, String def) {
        String v = System.getenv(k);
        return v == null || v.isEmpty() ? def : v;
    }
}
