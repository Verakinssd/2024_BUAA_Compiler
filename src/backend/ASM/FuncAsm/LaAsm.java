package backend.ASM.FuncAsm;

import backend.ASM.Register;

import static backend.ASM.AsmOP.LA;

public class LaAsm extends InstructionAsm {
    public Register register;
    public String name;

    public LaAsm(Register register,String name) {
        this.register = register;
        this.name = name;
    }

    public String toString() {
        return "    " + LA + " " + register + " " + name.substring(1) +"\n";
    }
}
