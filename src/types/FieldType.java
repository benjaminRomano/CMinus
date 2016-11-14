package types;

public class FieldType extends Type {
    private String name;
    private boolean reference;
    private Type type;

    public FieldType(String name, Type type, boolean reference) {
        super(type.getTypeKind());
        this.name = name;
        this.type = type;
        this.reference = reference;
    }

    public boolean isReference() {
        return this.reference;
    }

    public String getName() {
        return this.name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FieldType)) {
            return false;
        }

        FieldType fieldType = (FieldType) o;

        return this.reference == fieldType.reference && this.name.equals(fieldType.name) && this.type.equals(fieldType.type);
    }
}
