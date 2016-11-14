import java_cup.runtime.ComplexSymbolFactory;
import nodes.Program;

public class Main {

    public static void main(String argv[]) throws java.io.IOException, java.lang.Exception {
        Lexer scanner = null;
        ComplexSymbolFactory csf = new ComplexSymbolFactory();

        try {
            scanner = new Lexer(new java.io.FileReader(argv[0]), csf);
        } catch (java.io.FileNotFoundException e) {
            System.err.println("File not found : \"" + argv[0] + "\"");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Usage : java -jar Compiler.jar <inputfile>");
            System.exit(1);
        }

        try {
            Parser p = new Parser(scanner, csf);
            Program result = (Program) p.parse().value;

            System.out.println("Parsed successfully!");
//            MiniJVMGenerator jvm = new MiniJVMGenerator(argv[0]);
//            result.accept(jvm);
//            jvm.flush();
        } catch (java.io.IOException e) {
            System.err.println("An I/O error occured while parsing : \n" + e);
            System.exit(1);
        }
    }
}




