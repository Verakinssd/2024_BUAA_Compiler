package backend.LLVMIR.FuncIR;

import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import java.util.Objects;

import static backend.ASM.AsmOP.SW;
import static backend.ASM.Register.T0;
import static backend.ASM.Register.T1;
import static backend.LLVMIR.RegType.getReference;

public class StoreIR extends InstructionIR {
    public RegIR regIR1;
    public String reg2;
    public RegType regType2;
    public Boolean isGlobal2;

    public StoreIR(RegIR regIR1, String reg2, RegType regType2, Boolean isGlobal2) {
        this.regIR1 = regIR1;
        this.reg2 = reg2;
        this.regType2 = regType2;
        this.isGlobal2 = isGlobal2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        Register operand1;
        if (regIR1.type == 1) {
            operand1 = AsmBuilder.getRegister(funcIR, regIR1.value, T0);
        } else {
            operand1 = AsmBuilder.getRegister(funcIR, regIR1.reg, regIR1.isGlobal, T0);
        }
        Register operand2 = AsmBuilder.getRegister(funcIR,reg2, isGlobal2, T1);
        funcIR.addInstructionAsm(new MemoryAsm(SW, operand1, operand2, 0));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.regIR1.type == 1) {
            sb.append("    store ").append(regIR1.regType).append(" ").
                    append(regIR1.value).append(", ").append(regType2).append(" ").
                    append(cg(reg2)).append(" ").append("\n");
        } else {
            sb.append("    store ").append(regIR1.regType).append(" ").
                    append(cg(regIR1.reg)).append(", ").append(regType2).append(" ").
                    append(cg(reg2)).append(" ").append("\n");
        }
        return sb.toString();
    }
}
