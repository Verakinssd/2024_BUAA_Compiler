package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class BrAsm extends InstructionAsm {
    public Register rs;
    public Register rt;
    public AsmOP op;
    public String label;

    public BrAsm(AsmOP op, Register rs,Register rt, String label) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.label = label;
    }

    public String toString() {
        return "    " + op + " " + rs + " " + rt + " " + label + "\n";
    }
}
