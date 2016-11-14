package types;

public class LiteralType extends Type {

    public LiteralType(TypeKind typeKind) {
        super(typeKind);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LiteralType)) {
            return false;
        }

        LiteralType literalType = (LiteralType) o;
        return this.getTypeKind() == literalType.getTypeKind();
    }
}
