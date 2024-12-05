package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class HiLoAsm extends InstructionAsm {
    public Register dest;
    public AsmOP op;

    public HiLoAsm(AsmOP op, Register dest) {
        this.op = op;
        this.dest = dest;
    }

    @Override
    public String toString() {
        return "    " + op.toString() + " " + dest.toString() + "\n";
    }
}
