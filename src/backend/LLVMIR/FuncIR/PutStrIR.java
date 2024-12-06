package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.LiAsm;
import backend.ASM.FuncAsm.SyscallAsm;

import static backend.ASM.AsmBuilder.getRegister;
import static backend.ASM.Register.A0;
import static backend.ASM.Register.V0;

public class PutStrIR extends InstructionIR {
    public Integer arraySize;
    public String reg;

    public PutStrIR(Integer arraySize, String reg) {
        this.arraySize = arraySize;
        this.reg = reg;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        getRegister(funcIR, reg, true, A0);
        funcIR.addInstructionAsm(new LiAsm(V0 , 4));
        funcIR.addInstructionAsm(new SyscallAsm());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    call void @putstr(i8* getelementptr inbounds ([").append(arraySize)
                .append(" x i8], [").append(arraySize).append(" x i8]* ").append(cg(reg)).append(", i64 0, i64 0))\n");
        return sb.toString();
    }
}
