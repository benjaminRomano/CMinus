package nodes.statements;

import nodes.expressions.Expression;

public class ForStatement extends LoopStatement {
    public AssignmentStatement initializer;
    public Expression condition;
    public AssignmentStatement incrementer;
    public Statement statement;

    public ForStatement(AssignmentStatement initializer, Expression condition, AssignmentStatement incrementer, Statement statement) {
        this.initializer = initializer;
        this.condition = condition;
        this.incrementer = incrementer;
        this.statement = statement;
    }
}