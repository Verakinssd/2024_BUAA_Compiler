package backend.LLVMIR.FuncIR;

import backend.LLVMIR.RegIR;

public class PutChIR extends InstructionIR {
    public RegIR regIR;

    public PutChIR(RegIR regIR) {
        this.regIR = regIR;
    }

    public String toString() {
        if (regIR.type == 1) {
            return "    call void @putch(i32 " + regIR.value + ")\n";
        } else {
            return "    call void @putch(i32 " + cg(regIR.reg) + ")\n";
        }
    }
}
