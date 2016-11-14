package nodes.declarations;

import nodes.statements.BlockStatement;
import symbols.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class FunctionDeclaration implements Declaration {
    public String name;
    public List<Parameter> parameters;
    public BlockStatement blockStatement;
    public SymbolTable symbolTable;

    public FunctionDeclaration(String name, List<Parameter> parameters, BlockStatement blockStatement) {
        this.name = name;
        this.parameters = parameters;
        this.blockStatement = blockStatement;
    }

    public FunctionDeclaration(String name, BlockStatement blockStatement) {
        this.name = name;
        this.parameters = new ArrayList<>();
        this.blockStatement = blockStatement;
    }
}
