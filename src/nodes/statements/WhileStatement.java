package nodes.statements;

import nodes.expressions.Expression;

public class WhileStatement extends LoopStatement {
    public Expression conditional;
    public Statement statement;

    public WhileStatement(Expression conditional, Statement statement) {
        this.conditional = conditional;
        this.statement = statement;
    }
}
