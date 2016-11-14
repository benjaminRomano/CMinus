package symbols;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    public SymbolTable parent;
    Map<String, Symbol> variables;

    public SymbolTable() {
        this.variables = new HashMap<>();
    }

    public void addVariable(String name, Symbol symbol) {
        if (this.variables.containsKey(name)) {
            throw new NullPointerException(String.format("`%s` already exists in scope", name));
        }

        this.variables.put(name, symbol);
   }

    public boolean hasVariable(String name) {

        SymbolTable currTable = this;

        while (currTable != null) {
            if (currTable.variables.containsKey(name)) {
                return true;
            }

            currTable = currTable.parent;
        }

        return false;
    }

    public Symbol getVariable(String name) {

        SymbolTable currTable = this;

        while (currTable != null) {
            if (currTable.variables.containsKey(name)) {
                return currTable.variables.get(name);
            }

            currTable = currTable.parent;
        }

        throw new NullPointerException(String.format("%s does not exist in scope", name));
    }
}
