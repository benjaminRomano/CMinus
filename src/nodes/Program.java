package nodes;

import nodes.declarations.Declaration;
import symbols.SymbolTable;

import java.util.List;

public class Program implements ParserNode {
    public List<Declaration> declarations;
    public SymbolTable symbolTable;


    public Program(List<Declaration> declarations) {
        this.declarations = declarations;
    }
}
