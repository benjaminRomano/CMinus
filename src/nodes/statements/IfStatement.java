package nodes.statements;

import nodes.expressions.Expression;

public class IfStatement implements Statement {
    public Expression conditional;
    public Statement statement;
    public Statement elseStatement;

    public IfStatement(Expression conditional, Statement statement, Statement elseStatement) {
        this.conditional = conditional;
        this.statement = statement;
        this.elseStatement = elseStatement;
    }
}
