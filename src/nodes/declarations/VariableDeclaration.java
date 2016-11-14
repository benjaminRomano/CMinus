package nodes.declarations;

import types.TypeKind;

import java.util.List;

public class VariableDeclaration implements Declaration {
    public TypeKind type;
    public List<Variable> variables;

    public VariableDeclaration(TypeKind type, List<Variable> variables) {
        this.type = type;
        this.variables = variables;
    }
}
