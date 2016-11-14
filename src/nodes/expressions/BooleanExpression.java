package nodes.expressions;

public class BooleanExpression implements Expression {
    public Boolean value;

    public BooleanExpression(Boolean value) {
        this.value = value;
    }
}
