package backend.LLVMIR.FuncIR;

import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.LabelAsm;

public class BasicBlockIR extends InstructionIR {
    public String name;

    public BasicBlockIR() {
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new LabelAsm(funcIR.ident + "_" + name.substring(1)));
    }

    public String toString() {
        return "\n" + cg(name).substring(1) + ":\n";
    }
}
