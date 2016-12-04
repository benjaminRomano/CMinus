package checker;


import nodes.OperatorKind;
import nodes.Program;
import nodes.declarations.*;
import nodes.expressions.*;
import nodes.statements.*;
import symbols.Symbol;
import symbols.SymbolTable;
import types.*;

public class Checker {

    public SymbolTable check(Program program) throws CheckerException {
        CheckerContext context = new CheckerContext();

        program.symbolTable = context.symbolTable;

        for (Declaration declaration : program.declarations) {
            checkDeclaration(context, declaration);
        }

        if (!context.symbolTable.hasVariable("main")) {
            throw new CheckerException("Program must contain a main function");
        }

        Symbol symbol = context.symbolTable.getVariable("main");

        if (symbol.getTypeKind() != TypeKind.Function) {
            throw new CheckerException("main must be a function");
        }

        FunctionType mainFunction = (FunctionType) symbol.getType();

        if (mainFunction.fields.size() != 0) {
            throw new CheckerException("main must be have 0 fields");
        }

        return context.symbolTable;
    }

    private void checkDeclaration(CheckerContext context, Declaration declaration) throws CheckerException {
        if (declaration instanceof VariableDeclaration) {
            checkVariableDeclaration(context, (VariableDeclaration) declaration);
        } else {
            checkFunctionDeclaration(context, (FunctionDeclaration) declaration);
        }
    }

    private void checkVariableDeclaration(CheckerContext context, VariableDeclaration variableDeclaration) throws CheckerException {
        for (Variable variable : variableDeclaration.variables) {
            if (variable.number != null) {
                int size = variable.number;

                ArrayType arrayType;

                if (variableDeclaration.typeKind == TypeKind.Integer) {
                    arrayType = new ArrayType(new LiteralType(TypeKind.Integer), size);
                } else {
                    arrayType = new ArrayType(new LiteralType(TypeKind.Boolean), size);
                }

                Symbol symbol = new Symbol(arrayType);
                context.symbolTable.addVariable(variable.name, symbol);
            } else {
                context.symbolTable.addVariable(variable.name, new Symbol(new LiteralType(variableDeclaration.typeKind)));
            }
        }
    }

    private void checkFunctionDeclaration(CheckerContext context, FunctionDeclaration functionDeclaration) throws CheckerException {
        FunctionType functionType = new FunctionType();

        SymbolTable newSymbolTable = new SymbolTable();
        newSymbolTable.parent = context.symbolTable;

        for (Parameter parameter : functionDeclaration.parameters) {
            Type type;

            if (parameter.isArray) {
                if (parameter.typeKind == TypeKind.Integer) {
                    type = new ArrayType(new LiteralType(TypeKind.Integer), 0);
                } else {
                    type = new ArrayType(new LiteralType(TypeKind.Boolean), 0);
                }

            } else {
                type = new LiteralType(parameter.typeKind);
            }

            newSymbolTable.addVariable(parameter.name, new Symbol(type, parameter.isAddr));
            functionType.fields.add(new FieldType(parameter.name, type, parameter.isAddr));
        }

        context.symbolTable.addVariable(functionDeclaration.name, new Symbol(functionType, false));
        context.symbolTable = newSymbolTable;
        functionDeclaration.symbolTable = context.symbolTable;

        checkBlockStatement(context, functionDeclaration.blockStatement, false);
    }

    private void checkBlockStatement(CheckerContext context, BlockStatement blockStatement, boolean createNewTable) throws CheckerException {
        if (createNewTable) {
            SymbolTable symbolTable = new SymbolTable();
            symbolTable.parent = context.symbolTable;
            context.symbolTable = symbolTable;
        }

        blockStatement.symbolTable = context.symbolTable;

        for (VariableDeclaration variableDeclaration : blockStatement.variableDeclarations) {
            checkVariableDeclaration(context, variableDeclaration);
        }


        for (Statement statement : blockStatement.statements) {
            checkStatement(context, statement);
        }


        context.symbolTable = context.symbolTable.parent;
    }

    private void checkStatement(CheckerContext context, Statement statement) throws CheckerException {
        if (statement instanceof BlockStatement) {
            checkBlockStatement(context, (BlockStatement) statement, true);
        } else if (statement instanceof IfStatement) {
            checkIfStatement(context, (IfStatement) statement);
        } else if (statement instanceof WhileStatement) {
            checkWhileStatement(context, (WhileStatement) statement);
        } else if (statement instanceof DoStatement) {
            checkDoStatement(context, (DoStatement) statement);
        } else if (statement instanceof FunctionCallStatement) {
            checkFunctionCallStatement(context, (FunctionCallStatement) statement);
        } else {
            checkAssignmentStatement(context, (AssignmentStatement) statement);
        }
    }

    private void checkFunctionCallStatement(CheckerContext context, FunctionCallStatement functionCallStatement) throws CheckerException {
        if (!context.symbolTable.hasVariable(functionCallStatement.name)) {
            throw new CheckerException("Variable, " + functionCallStatement.name+ " does not exist in this scope");
        }

        Symbol symbol = context.symbolTable.getVariable(functionCallStatement.name);

        if (symbol.getTypeKind() != TypeKind.Function) {
            throw new CheckerException("Cannot use variable, " + functionCallStatement.name + " with type " + symbol.getType() + " as a function");
        }

        FunctionType functionPrimary = (FunctionType) symbol.getType();

        if (functionPrimary.fields.size() != functionCallStatement.arguments.size()) {
            throw new CheckerException(String.format("Argument mismatch. Expected %d arguments, received %d", functionPrimary.fields.size(), functionCallStatement.arguments.size()));

        }

        for (int i = 0; i < functionCallStatement.arguments.size(); i++) {
            Type type = checkExpression(context, functionCallStatement.arguments.get(i));

            if (functionPrimary.fields.get(i).isReference() && !(functionCallStatement.arguments.get(i) instanceof LocationExpression)) {
                throw new CheckerException("Expected locationExpression for pass-by-reference parameter");
            }

            if (!(type.equals(functionPrimary.fields.get(i).getType()))) {
                throw new CheckerException("Expected parameter of type, " + functionPrimary.fields.get(i).getTypeKind() + ", but given type " + type.getTypeKind());
            }
        }
   }

    private void checkAssignmentStatement(CheckerContext context, AssignmentStatement assignmentStatement) throws CheckerException {
        Type location = checkLocationExpression(context, assignmentStatement.locationExpression);

        Type value = checkExpression(context, assignmentStatement.expression);

        if (location.getTypeKind() == TypeKind.Array) {
            throw new CheckerException("Cannot assign value to an array type");
        } else if (!location.equals(value)) {
            throw new CheckerException("TypeKind mismatch on assignment. Given " + value.getTypeKind() + ", expected " + location.getTypeKind());
        }
    }

    private void checkDoStatement(CheckerContext context, DoStatement doStatement) throws CheckerException {
        checkStatement(context, doStatement.statement);

        Type type = checkExpression(context, doStatement.conditional);

        if (type.getTypeKind() != TypeKind.Boolean) {
            throw new CheckerException("Conditional returned " + type.getTypeKind() + " expected boolean");
        }
    }

    private void checkWhileStatement(CheckerContext context, WhileStatement whileStatement) throws CheckerException {
        Type type = checkExpression(context, whileStatement.conditional);

        if (type.getTypeKind() != TypeKind.Boolean) {
            throw new CheckerException("Conditional returned " + type.getTypeKind() + " expected boolean");
        }

        checkStatement(context, whileStatement.statement);
    }

    private void checkIfStatement(CheckerContext context, IfStatement ifStatement) throws CheckerException {
        Type type = checkExpression(context, ifStatement.conditional);

        if (type.getTypeKind() != TypeKind.Boolean) {
            throw new CheckerException("Conditional returned " + type.getTypeKind() + " expected boolean");
        }

        checkStatement(context, ifStatement.statement);
        if (ifStatement.elseStatement != null) {
            checkStatement(context, ifStatement.elseStatement);
        }
    }

    private Type checkExpression(CheckerContext context, Expression expression) throws CheckerException {
        if (expression instanceof UnaryExpression) {
            return checkUnaryExpression(context, (UnaryExpression) expression);
        } else if (expression instanceof BinaryExpression) {
            return checkBinaryExpression(context, (BinaryExpression) expression);
        } else if (expression instanceof LocationExpression) {
            return checkLocationExpression(context, (LocationExpression) expression);
        } else if (expression instanceof IntegerExpression) {
            return new LiteralType(TypeKind.Integer);
        } else {
            return new LiteralType(TypeKind.Boolean);
        }
    }

    private Type checkLocationExpression(CheckerContext context, LocationExpression locationExpression) throws CheckerException {
        if (!context.symbolTable.hasVariable(locationExpression.name)) {
            throw new CheckerException("Variable, " + locationExpression.name + " does not exist in this scope");
        }

        Symbol symbol = context.symbolTable.getVariable(locationExpression.name);

        // Not an arrayType
        if (locationExpression.expression == null) {
            return symbol.getType();
        }

        if (symbol.getTypeKind() != TypeKind.Array) {
            throw new CheckerException("Cannot use array indexing on a " + symbol.getTypeKind());
        }

        Type accessorType = checkExpression(context, locationExpression.expression);

        if (accessorType.getTypeKind() != TypeKind.Integer) {
            throw new CheckerException("Cannot use " + accessorType.getTypeKind() + " to access array");
        }

        ArrayType arrayType = (ArrayType) symbol.getType();

        if (arrayType.getElementType().getTypeKind() == TypeKind.Integer) {
            return new LiteralType(TypeKind.Integer);
        } else {
            return new LiteralType(TypeKind.Boolean);
        }
   }

    private Type checkBinaryExpression(CheckerContext context, BinaryExpression binaryExpression) throws CheckerException {
        OperatorKind kind = binaryExpression.operator;
        Type leftExpression = checkExpression(context, binaryExpression.left);
        Type rightExpression = checkExpression(context, binaryExpression.right);

        if (kind == OperatorKind.NotEquals || kind == OperatorKind.Equals) {
            if (!leftExpression.equals(rightExpression)) {
                throw new CheckerException("TypeKind mismatch for, " + binaryExpression.operator + " operator");
            }

            return new LiteralType(TypeKind.Boolean);
        } else if (kind == OperatorKind.LessThan || kind == OperatorKind.LessThanEquals || kind == OperatorKind.GreaterThan || kind == OperatorKind.GreaterThanEquals) {
            if (leftExpression.getTypeKind() != TypeKind.Integer) {
                throw new CheckerException("TypeKind mismatch for left-hand side of " + binaryExpression.operator + " operator. Expected an Integer");
            } else if (rightExpression.getTypeKind() != TypeKind.Integer) {
                throw new CheckerException("TypeKind mismatch for right-hand side of " + binaryExpression.operator + " operator. Expected an Integer");
            }

            return new LiteralType(TypeKind.Boolean);
        } else if (kind == OperatorKind.Add || kind == OperatorKind.Subtract || kind == OperatorKind.Multiply || kind == OperatorKind.Divide || kind == OperatorKind.Mod) {
            if (leftExpression.getTypeKind() != TypeKind.Integer) {
                throw new CheckerException("TypeKind mismatch for left-hand side of " + binaryExpression.operator + " operator. Expected an Integer");
            } else if (rightExpression.getTypeKind() != TypeKind.Integer) {
                throw new CheckerException("TypeKind mismatch for right-hand side of " + binaryExpression.operator + " operator. Expected an Integer");
            }

            return new LiteralType(TypeKind.Integer);
        } else {
            // && and ||
            if (leftExpression.getTypeKind() != TypeKind.Boolean) {
                throw new CheckerException("TypeKind mismatch for left-hand side of " + binaryExpression.operator + " operator. Expected a Boolean");
            } else if (rightExpression.getTypeKind() != TypeKind.Boolean) {
                throw new CheckerException("TypeKind mismatch for right-hand side of " + binaryExpression.operator + " operator. Expected a Boolean");
            }

            return new LiteralType(TypeKind.Boolean);
        }
    }

    private Type checkUnaryExpression(CheckerContext context, UnaryExpression unaryExpression) throws CheckerException {
        if (unaryExpression.operator == OperatorKind.Add || unaryExpression.operator  == OperatorKind.Minus) {
            Type type = checkExpression(context, unaryExpression.expression);

            if (type.getTypeKind() != TypeKind.Integer) {
                throw new CheckerException("Cannot use " + unaryExpression.operator + " with " + type.getTypeKind());
            }

            return type;
        } else if (unaryExpression.operator == OperatorKind.Not) {
            Type type = checkExpression(context,unaryExpression.expression);

            if (type.getTypeKind() != TypeKind.Boolean) {
                throw new CheckerException("Cannot use " + unaryExpression.operator + " with " + type.getTypeKind());
            }

            return type;
        }

        throw new CheckerException("Could not recognize operator " + unaryExpression.operator);
    }
}
