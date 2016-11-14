package nodes.statements;

import nodes.declarations.VariableDeclaration;
import symbols.SymbolTable;

import java.util.List;

public class BlockStatement implements Statement {
    public List<VariableDeclaration> variableDeclarations;
    public List<Statement> statements;
    public SymbolTable symbolTable;

    public BlockStatement(List<VariableDeclaration> variableDeclarations, List<Statement> statements) {
        variableDeclarations = variableDeclarations;
        statements = statements;
    }
}
