package nodes.statements;

import nodes.expressions.Expression;
import nodes.expressions.LocationExpression;

public class AssignmentStatement implements Statement {
    public LocationExpression locationExpression;
    public Expression expression;

    public AssignmentStatement(LocationExpression locationExpression, Expression expression) {
        this.locationExpression = locationExpression;
        this.expression = expression;
    }
}
