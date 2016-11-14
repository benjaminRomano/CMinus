package types;

public abstract class Type {
    private TypeKind typeKind;

    public Type(TypeKind typeKind) {
        this.typeKind = typeKind;
    }

    public TypeKind getTypeKind() {
        return this.typeKind;
    }

    public abstract boolean equals(Object o);
}
