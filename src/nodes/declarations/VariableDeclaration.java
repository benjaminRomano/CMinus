package nodes.declarations;

import types.TypeKind;

import java.util.List;

public class VariableDeclaration implements Declaration {
    public TypeKind typeKind;
    public List<Variable> variables;

    public VariableDeclaration(TypeKind typeKind, List<Variable> variables) {
        this.typeKind = typeKind;
        this.variables = variables;
    }
}
