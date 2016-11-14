package nodes.statements;

import nodes.expressions.Expression;

import java.util.ArrayList;
import java.util.List;

public class FunctionCallStatement implements Statement {
    public String name;
    public List<Expression> arguments;

    public FunctionCallStatement(String name, List<Expression> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public FunctionCallStatement(String name) {
        this.name = name;
        this.arguments = new ArrayList<>();
    }
}
