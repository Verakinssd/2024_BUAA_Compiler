package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.JumpAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegIR;

import static backend.ASM.AsmBuilder.getRegister;
import static backend.ASM.AsmOP.JR;
import static backend.ASM.Register.RA;
import static backend.ASM.Register.V0;

public class ReturnIR extends InstructionIR {
    public RegIR regIR;
    public Integer type;

    public ReturnIR(RegIR regIR) {
        this.regIR = regIR;
        this.type = 1;
    }

    public ReturnIR() {
        this.type = 2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        if (type == 1) {
            if (regIR.type == 1) {
                getRegister(funcIR, regIR.value, V0);
            } else {
                getRegister(funcIR, regIR.reg, regIR.isGlobal, V0);
            }
        }
        funcIR.addInstructionAsm(new JumpAsm(JR,RA));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == 1) {
            sb.append("    ret ").append(regIR).append("\n");
        } else {
            sb.append("    ret void\n");
        }
        return sb.toString();
    }
}
