package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class MemoryAsm extends InstructionAsm {
    public AsmOP op;
    public Register rs;
    public Register rt;
    public Register rd;
    public Integer imm;
    public Integer type;

    public MemoryAsm(AsmOP op , Register rs, Register rd , Integer imm) {
        this.op = op;
        this.rs = rs;
        this.rd = rd;
        this.imm = imm;
        this.type = 1;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(op).append(" ").append(rs).append(" ").append(imm).append("(").append(rd).append(")").append("\n");
        return sb.toString();
    }
}
