package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.BrAsm;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.JumpAsm;
import backend.ASM.FuncAsm.MemoryAsm;

import static backend.ASM.AsmBuilder.offsetMap;
import static backend.ASM.AsmOP.*;
import static backend.ASM.AsmOP.J;
import static backend.ASM.Register.*;

public class BrIR extends InstructionIR {
    public String reg;
    public BasicBlockIR trueBB;
    public BasicBlockIR falseBB;
    public Integer type;

    public BrIR(String reg, BasicBlockIR trueBB, BasicBlockIR falseBB) {
        this.reg = reg;
        this.trueBB = trueBB;
        this.falseBB = falseBB;
        this.type = 1;
    }

    public BrIR(BasicBlockIR BB) {
        this.trueBB = BB;
        this.type = 2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        if (type == 1) {
            funcIR.addInstructionAsm(new MemoryAsm(LW, T0, SP, offsetMap.get(reg)));
            funcIR.addInstructionAsm(new BrAsm(BEQ, T0, ZERO,
                    funcIR.ident + "_" + falseBB.name.substring(1)));
            funcIR.addInstructionAsm(new JumpAsm(J,
                    funcIR.ident + "_" + trueBB.name.substring(1)));
        } else {
            funcIR.addInstructionAsm(new JumpAsm(J,
                    funcIR.ident + "_" + trueBB.name.substring(1)));
        }
    }

    public String toString() {
        if (type == 1) {
            return "    br i1 " + cg(reg) +
                    ", label " + cg(trueBB.name) +
                    ", label " + cg(falseBB.name) + "\n";
        } else {
            return "    br label " + cg(trueBB.name) + "\n";
        }
    }
}
