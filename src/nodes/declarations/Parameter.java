package nodes.declarations;

import types.TypeKind;

public class Parameter {
    public TypeKind typeKind;
    public boolean isAddr;
    public String name;
    public boolean isArray;

    public Parameter(TypeKind typeKind, boolean isAddr, String name, boolean isArray) {
        this.typeKind = typeKind;
        this.isAddr = isAddr;
        this.name = name;
        this.isArray = isArray;
    }
}
