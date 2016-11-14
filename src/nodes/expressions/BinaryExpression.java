package nodes.expressions;

import nodes.OperatorKind;

public class BinaryExpression implements Expression {
    public Expression left;
    public OperatorKind operator;
    public Expression right;

    public BinaryExpression(Expression left, OperatorKind operator, Expression right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
}
