package nodes.statements;

import nodes.expressions.Expression;

public class DoStatement extends LoopStatement {
    public Statement statement;
    public Expression conditional;

    public DoStatement(Statement statement, Expression conditional) {
        this.statement = statement;
        this.conditional = conditional;
    }
}
