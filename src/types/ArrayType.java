package types;

public class ArrayType extends Type {
    private int size;
    private Type elementType;

    public ArrayType(Type elementType, int size) {
        super(TypeKind.Array);
        this.elementType = elementType;
        this.size = size;
    }

    public Type getElementType() {
        return this.elementType;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ArrayType)) {
            return false;
        }

        ArrayType arrayType = (ArrayType) o;


        // Note: We don't care about size since an ArrayType nested inside a FieldType doesn't have a size
        return this.getTypeKind() == arrayType.getTypeKind() && this.elementType.equals(arrayType.elementType);
    }
}
