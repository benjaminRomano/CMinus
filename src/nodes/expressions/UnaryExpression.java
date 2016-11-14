package nodes.expressions;

import nodes.OperatorKind;

public class UnaryExpression implements Expression {
    public OperatorKind operator;
    public Expression expression;

    public UnaryExpression(OperatorKind operator, Expression expression) {
        this.operator = operator;
        this.expression = expression;
    }
}
