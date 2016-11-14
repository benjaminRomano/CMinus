package types;

import java.util.ArrayList;
import java.util.List;

public class FunctionType extends Type {
    public List<FieldType> fields;

    public FunctionType() {
        super(TypeKind.Function);
        fields = new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FunctionType)) {
            return false;
        }

        FunctionType primaryType = (FunctionType) o;

        if (primaryType.fields.size() != this.fields.size()) {
            return false;
        }

        for (int i = 0; i < this.fields.size(); i++) {
            if (!this.fields.get(i).equals(primaryType.fields.get(i))) {
                return false;
            }
        }

        return true;
    }
}
