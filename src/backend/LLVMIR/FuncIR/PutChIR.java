package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.*;
import backend.LLVMIR.RegIR;

import static backend.ASM.AsmBuilder.getRegister;
import static backend.ASM.AsmOP.LW;
import static backend.ASM.AsmOP.SW;
import static backend.ASM.Register.A0;
import static backend.ASM.Register.V0;

public class PutChIR extends InstructionIR {
    public RegIR regIR;

    public PutChIR(RegIR regIR) {
        this.regIR = regIR;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        if (regIR.type == 2) {
            getRegister(funcIR, regIR.reg, regIR.isGlobal, A0);
        } else{
            getRegister(funcIR, regIR.value, A0);
        }
        funcIR.addInstructionAsm(new LiAsm(V0, 11));
        funcIR.addInstructionAsm(new SyscallAsm());
    }

    public String toString() {
        if (regIR.type == 1) {
            return "    call void @putch(i32 " + regIR.value + ")\n";
        } else {
            return "    call void @putch(i32 " + cg(regIR.reg) + ")\n";
        }
    }
}
