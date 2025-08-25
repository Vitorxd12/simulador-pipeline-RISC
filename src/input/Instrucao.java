package input;


public class Instrucao {
    private String instrucao;
    private int reg1, reg2, reg3;
    private int val1, val2, val3;
    private int resultado;

    public Instrucao(String operador, String r1, String r2, String r3) {
        switch (operador) {
            case ("add"):
            case ("sub"):
            case ("avg"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = registradorParaInt(r2);
                reg3 = registradorParaInt(r3);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2 + " " + reg3);
                break;
            case ("lw"):
            case ("sw"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = offsetParaInt(r2);
                reg3 = enderecoParaInt(r2);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2 + " " + reg3);
                break;

            case ("swap"):
            case ("rev"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = registradorParaInt(r2);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2);
                break;
            default:
                System.out.println("Erro na criação da instrução");
        }
    }

    public void setVal(int valorR1, int valorR2, int valorR3) {
        this.val1 = valorR1;
        this.val2 = valorR2;
        this.val3 = valorR3;
    }
    public void setResultado(int resultado) {
        this.resultado = resultado;
    }
    public int getResultado() {
        return this.resultado;
    }

    public int getValorR1() {
        return val1;
    }
    public int getValorR2() {
        return val2;
    }
    public int getValorR3() {
        return val3;
    }

    public int registradorParaInt(String reg) {
        String regex = "\\D";
        return Integer.parseInt(reg.replaceAll(regex, ""));
    }
    public int offsetParaInt(String reg) {
        String regex = "\\(x\\d+\\)";
        return Integer.parseInt(reg.replaceAll(regex, ""));
    }
    public int enderecoParaInt(String reg) {
        String regex = "\\d+\\(x";
        String nreg = reg.replaceAll(regex, "");
        String regex2 = "\\)";
        nreg = nreg.replaceAll(regex2, "");
        return Integer.parseInt(nreg);
    }
    public int getReg1(){
        return reg1;
    };
    public int getReg2(){
        return reg2;
    };
    public int getReg3(){
        return reg3;
    };
    public String getInstrucao() {
        if (instrucao == null) {
            return "null";
        }
        return instrucao;
    }
    public String toString(){
        return "\"" +
                "" + instrucao +
                " " + reg1 +
                " " + reg2 +
                " " + reg3 +
                '\"';
    }
}

