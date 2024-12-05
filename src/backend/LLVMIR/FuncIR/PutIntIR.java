package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.LiAsm;
import backend.ASM.FuncAsm.SyscallAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegIR;

import static backend.ASM.AsmBuilder.getRegister;
import static backend.ASM.Register.*;

public class PutIntIR extends InstructionIR {
    public RegIR regIR;

    public PutIntIR(RegIR regIR) {
        this.regIR = regIR;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        getRegister(funcIR, regIR.reg, regIR.isGlobal , A0);
        funcIR.addInstructionAsm(new LiAsm(V0, 1));
        funcIR.addInstructionAsm(new SyscallAsm());
    }

    public String toString() {
        if (regIR.type == 1) {
            return "    call void @putint(i32 " + regIR.value + ")\n";
        } else {
            return "    call void @putint(i32 " + cg(regIR.reg) + ")\n";
        }
    }
}
