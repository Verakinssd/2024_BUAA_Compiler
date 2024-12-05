package backend.LLVMIR.FuncIR;
import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.ComputeAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import static backend.ASM.AsmBuilder.push;
import static backend.ASM.AsmOP.ADDI;
import static backend.ASM.AsmOP.SW;
import static backend.ASM.Register.SP;
import static backend.ASM.Register.T0;
import static backend.LLVMIR.RegType.getReference;

public class AllocaIR extends InstructionIR {
    public String reg;
    public RegType regType;
    public Integer type;
    public Integer arraySize;

    public AllocaIR(String reg, RegType regType) {
        this.reg = reg;
        this.regType = regType;
        this.type = 1;
        this.arraySize = 1;
    }

    public AllocaIR(String reg, RegType regType, Integer arraySize) {
        this.reg = reg;
        this.regType = regType;
        this.type = 2;
        this.arraySize = arraySize;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        int offset = AsmBuilder.allocate(arraySize * 4);
        funcIR.addInstructionAsm(new ComputeAsm(ADDI, T0 , SP, offset));
        funcIR.addInstructionAsm(new MemoryAsm(SW, T0, SP, push(reg,4)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == 1) {
            sb.append("    ").append(cg(reg)).append(" = alloca ").append(getReference(regType)).append("\n");
        } else {
            sb.append("    ").append(cg(reg)).append(" = alloca ").append("[").append(arraySize).append(" x ").append(getReference(getReference(regType))).append("]\n");
        }
        return sb.toString();
    }
}
