import java.io.*;
//esse aqui foi gpt q fez
public class SalvarLOG extends PrintStream {
    private final PrintStream fileOut;
    private final PrintStream consoleOut;

    public SalvarLOG(PrintStream fileOut, PrintStream consoleOut) {
        super(fileOut);
        this.fileOut = fileOut;
        this.consoleOut = consoleOut;
    }

    @Override
    public void println(String x) {
        fileOut.println(x);
        consoleOut.println(x);
    }

    @Override
    public void print(String x) {
        fileOut.print(x);
        consoleOut.print(x);
    }
}