package input;


public class Instrucao {
    private String instrucao;
    private int reg1, reg2, reg3;

    public Instrucao(String operador, String r1, String r2, String r3) {
        switch (operador) {
            case ("add"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = registradorParaInt(r2);
                reg3 = registradorParaInt(r3);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2 + " " + reg3);
                break;
            case ("sub"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = registradorParaInt(r2);
                reg3 = registradorParaInt(r3);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2 + " " + reg3);
                break;
            case ("sw"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = offsetParaInt(r2);
                reg3 = enderecoParaInt(r2);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2 + " " + reg3);
                break;
            case ("lw"):
                this.instrucao = operador;
                reg1 = registradorParaInt(r1);
                reg2 = offsetParaInt(r2);
                reg3 = enderecoParaInt(r2);
                System.out.println("\n" + instrucao + " " + reg1 + " " + reg2 + " " + reg3);
                break;
            default:
                System.out.println("Erro na criação da instrução");
        }
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
        reg = reg.replaceAll(regex, "");
        String regex2 = "\\)";
        reg = reg.replaceAll(regex2, "");
        return Integer.parseInt(reg);
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

