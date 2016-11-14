package generator;

import symbols.Location;

public class Instruction {
    private String label;
    private OpCode opCode;
    private String arg1;
    private String arg2;

    public Instruction(String label, OpCode opCode, String arg1, String arg2) throws GeneratorException {
        if (opCode == null) {
            throw new GeneratorException("OpCode cannot be null in instruction");
        }
        this.label = label;
        this.opCode = opCode;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public Instruction(String label, OpCode opCode, int arg1) throws GeneratorException {
        this(label, opCode, String.valueOf(arg1), null);
    }

    public Instruction(OpCode opCode, int arg1, int arg2) throws GeneratorException {
        this(null, opCode, String.valueOf(arg1), String.valueOf(arg2));
    }

    public Instruction(String label, OpCode opCode) throws GeneratorException {
        this(label, opCode, null, null);
    }

    public Instruction(OpCode opCode) throws GeneratorException {
        this(null, opCode, null, null);
    }

    public Instruction(OpCode opCode, String arg1) throws GeneratorException {
        this(null, opCode, arg1, null);
    }

    public Instruction(OpCode opCode, int arg1) throws GeneratorException {
        this(null, opCode, String.valueOf(arg1), null);
    }

    public Instruction(OpCode opCode, Location location) throws GeneratorException {
        this(null, opCode, String.valueOf(location.frame), String.valueOf(location.displacement));
    }

    @Override
    public String toString() {
        String label = (this.label == null) ? "" : this.label;
        String arg1 = (this.arg1 == null) ? "" : this.arg1;
        String arg2 = (this.arg2 == null) ? "" : this.arg2;

        return String.format("%-8s %-8s %-8s %-8s", label, opCode.name(), arg1, arg2);
    }
}
