package nodes.statements;

public abstract class LoopStatement implements Statement {
    public String startLabel;
    public String exitLabel;
    public int level;
}
