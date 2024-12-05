package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class CmpAsm extends InstructionAsm {
    public Register rs;
    public Register rt;
    public Register rd;
    public AsmOP op;

    public CmpAsm(AsmOP op,Register rs,Register rt,Register rd) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
    }

    public String toString() {
        return "    " + op + " " + rs + " " + rt + " " + rd + "\n";
    }
}
