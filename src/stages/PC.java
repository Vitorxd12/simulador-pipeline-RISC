package stages;

public class PC {
    private int valor;

    public PC() {
        this.valor = 0;
    }
    public int incrementar() {
        return valor + 4;
    }
    public int getValor() {
        return valor;
    }
    public void setValor(int valor) {
        this.valor = valor;
    }
}
