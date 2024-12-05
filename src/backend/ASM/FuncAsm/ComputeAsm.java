package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class ComputeAsm extends InstructionAsm {
    public AsmOP op;
    public Register rs;
    public Register rt;
    public Register rd;
    public Integer imm;
    public Integer type;

    public ComputeAsm(AsmOP op, Register rs, Register rt, Integer imm) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.imm = imm;
        this.type = 1;
    }

    public ComputeAsm(AsmOP op, Register rs, Register rt, Register rd) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.rd = rd;
        this.type = 2;
    }

    public ComputeAsm(AsmOP op, Register rs, Register rt) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.type = 3;
    }

    public String toString() {
        if (type == 1) {
            return "    " + op + " " + rs + " " + rt + " " + imm + "\n";
        } else if (type == 2) {
            return "    " + op + " " + rs + " " + rt + " " + rd + "\n";
        } else {
            return "    " + op + " " + rs + " " + rt + "\n";
        }
    }
}
