import checker.Checker;
import generator.Generator;
import generator.Instruction;
import java_cup.runtime.ComplexSymbolFactory;
import nodes.Program;

import java.io.PrintWriter;
import java.util.List;

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
            new Checker().check(result);
            List<Instruction> instructions = new Generator().generate(result);

//            instructions.forEach(System.out::println);

            try (PrintWriter out = new PrintWriter("output.sm")) {
                instructions.forEach(out::println);
            }
        } catch (java.io.IOException e) {
            System.err.println("An I/O error occured while parsing : \n" + e);
            System.exit(1);
        }
    }
}




