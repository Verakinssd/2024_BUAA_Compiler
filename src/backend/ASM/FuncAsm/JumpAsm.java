package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;
import backend.ASM.Register;

public class JumpAsm extends InstructionAsm {
    public AsmOP op;
    public String name;
    public Register reg;
    public Integer type;

    public JumpAsm(AsmOP op, String name) {
        this.op = op;
        this.name = name;
        this.type = 1;
    }

    public JumpAsm(AsmOP op, Register reg) {
        this.op = op;
        this.reg = reg;
        this.type = 2;
    }

    public String toString() {
        if (type == 1) {
            return "    " + op + " " + name + "\n";
        } else {
            return "    " + op + " " + reg + "\n";
        }
    }
}
