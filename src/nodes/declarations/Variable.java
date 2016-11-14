package nodes.declarations;

public class Variable {
    public String name;
    public Integer number;


    public Variable(String name, int number) {
        this.name = name;
        this.number = number;
    }

    public Variable(String name) {
        this.name = name;
        this.number = null;
    }
}
