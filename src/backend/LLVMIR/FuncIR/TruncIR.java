package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.CommentAsm;
import backend.LLVMIR.RegType;

import static backend.ASM.AsmBuilder.*;

public class TruncIR extends InstructionIR {
    String reg1;
    RegType regType1;
    String reg2;
    RegType regType2;

    public TruncIR(String reg1, RegType regType1, String reg2, RegType regType2) {
        this.reg1 = reg1;
        this.regType1 = regType1;
        this.reg2 = reg2;
        this.regType2 = regType2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        offsetMap.put(reg1,offsetMap.get(reg2));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(cg(reg1)).append(" = trunc ").append(regType2).append(" ").append(cg(reg2)).
                append(" to ").append(regType1).append("\n");
        return sb.toString();
    }
}
