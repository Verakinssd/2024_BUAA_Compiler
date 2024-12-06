package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class MemoryAsm extends InstructionAsm {
    public AsmOP op;
    public Register rs;
    public Register rt;
    public Integer imm;
    public Integer type;

    public MemoryAsm(AsmOP op , Register rs, Register rt , Integer imm) {
        this.op = op;
        this.rs = rs;
        this.rt = rt;
        this.imm = imm;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(op).append(" ").append(rs).append(" ").append(imm).append("(").append(rt).append(")").append("\n");
        return sb.toString();
    }
}
