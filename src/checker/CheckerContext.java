package checker;

import symbols.Symbol;
import symbols.SymbolTable;
import types.FieldType;
import types.FunctionType;
import types.LiteralType;
import types.TypeKind;

public class CheckerContext {
    SymbolTable symbolTable;

    public CheckerContext() throws CheckerException {
        initializeSymbolTable();
    }

    private void initializeSymbolTable() throws CheckerException {
        symbolTable = new SymbolTable();
        FunctionType scanFunction = new FunctionType();
        FieldType scanFunctionParameter = new FieldType("value", new LiteralType(TypeKind.Integer), true);
        scanFunction.fields.add(scanFunctionParameter);
        Symbol scanSymbol = new Symbol(scanFunction);

        symbolTable.addVariable("scan", scanSymbol);

        FunctionType printFunction = new FunctionType();
        FieldType printFunctionParameter = new FieldType("value", new LiteralType(TypeKind.Integer), false);
        printFunction.fields.add(printFunctionParameter);

        Symbol printSymbol = new Symbol(printFunction);

        symbolTable.addVariable("print", printSymbol);
    }
}
