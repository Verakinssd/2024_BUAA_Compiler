package backend.LLVMIR.FuncIR;

import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegType;

import static backend.ASM.AsmBuilder.push;
import static backend.ASM.AsmOP.*;
import static backend.ASM.Register.SP;
import static backend.ASM.Register.T0;
import static backend.LLVMIR.RegType.I8_PTR;

public class LoadIR extends InstructionIR {
    public RegType regType1;
    public String reg1;
    public RegType regType2;
    public String reg2;
    public Boolean isGlobal2;

    public LoadIR(RegType regType1, String reg1, RegType regType2, String reg2, Boolean isGlobal2) {
        this.regType1 = regType1;
        this.reg1 = reg1;
        this.regType2 = regType2;
        this.reg2 = reg2;
        this.isGlobal2 = isGlobal2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        Register reg = AsmBuilder.getRegister(funcIR ,reg2, isGlobal2, T0);
        if (regType2 == I8_PTR) {
            funcIR.addInstructionAsm(new MemoryAsm(LB, T0, reg, 0));
            funcIR.addInstructionAsm(new MemoryAsm(SB, T0, SP, push(reg1, 4)));
        } else {
            funcIR.addInstructionAsm(new MemoryAsm(LW, T0, reg, 0));
            funcIR.addInstructionAsm(new MemoryAsm(SW, T0, SP, push(reg1, 4)));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(cg(reg1)).append(" = load ").append(regType1).
                append(", ").append(regType2).append(" ").append(cg(reg2)).append("\n");
        return sb.toString();
    }
}
