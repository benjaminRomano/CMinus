package nodes.statements;

import nodes.expressions.Expression;

public class WhileStatement implements Statement {
    public Expression conditional;
    public Statement statement;

    public WhileStatement(Expression conditional, Statement statement) {
        this.conditional = conditional;
        this.statement = statement;
    }
}
