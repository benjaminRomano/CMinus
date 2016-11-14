package nodes.declarations;

import types.TypeKind;

public class Parameter {
    public TypeKind type;
    public boolean isAddr;
    public String name;
    public boolean isArray;

    public Parameter(TypeKind type, boolean isAddr, String name, boolean isArray) {
        this.type = type;
        this.isAddr = isAddr;
        this.name = name;
        this.isArray = isArray;
    }
}
