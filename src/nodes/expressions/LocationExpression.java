package nodes.expressions;

public class LocationExpression implements Expression {
    public String name;
    public Expression expression;

    public LocationExpression(String name, Expression expression) {
        this.name = name;
        this.expression = expression;
    }

    public LocationExpression(String name) {
        this.name = name;
    }
}
