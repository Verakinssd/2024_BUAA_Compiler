package backend.ASM.FuncAsm;

import backend.ASM.Register;

import static backend.ASM.AsmOP.LI;

public class LiAsm extends InstructionAsm {
    public Register rs;
    public Integer num;

    public LiAsm(Register rs, Integer num) {
        this.rs = rs;
        this.num = num;
    }

    @Override
    public String toString() {
        return "    " + LI + " " + rs + " " + num + "\n";
    }
}
