package backend.ASM.FuncAsm;

import backend.ASM.Register;

public class LaAsm extends InstructionAsm {
    public Register register;
    public String name;

    public LaAsm(Register register,String name) {
        this.register = register;
        this.name = name;
    }

    public String toString() {
        return "    la " + register + " " + name +"\n";
    }
}
