package stages;

public class PC {
    private int valor;

    public PC() {
        this.valor = 0;
    }
    public int incrementar() {
        this.valor += 4;
        return valor;
    }
    public int getValor() {
        return valor;
    }
}
