package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.LiAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.ASM.FuncAsm.SyscallAsm;
import backend.LLVMIR.RegIR;

import static backend.ASM.AsmBuilder.push;
import static backend.ASM.AsmOP.SW;
import static backend.ASM.Register.SP;
import static backend.ASM.Register.V0;

public class GetCharIR extends InstructionIR{
    public RegIR regIR;

    public GetCharIR(RegIR regIR) {
        this.regIR = regIR;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        funcIR.addInstructionAsm(new LiAsm(V0 , 12));
        funcIR.addInstructionAsm(new SyscallAsm());
        funcIR.addInstructionAsm(new MemoryAsm(SW , V0, SP, push(regIR.reg, 4)));
    }

    public String toString() {
        return "    " + cg(regIR.reg) + " = call i32 @getchar()" + "\n";
    }
}
