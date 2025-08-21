package input;


public class Instrucoes {
    private String instrucao;
    private String r1;
    private String r2;
    private String r3;

    public Instrucoes(String instrucao, String r1, String r2, String r3) {
        switch (instrucao) {
            case ("add"):
                break;
            case ("sub"):
                break;
            case ("sw"):
                break;
            case ("lw"):
                break;
            default:
                System.out.println("Erro na criação da instrução");
        }
    }
}
