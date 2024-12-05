package backend.LLVMIR.FuncIR;

import backend.LLVMIR.RegIR;

public class GetCharIR extends InstructionIR{
    public RegIR regIR;

    public GetCharIR(RegIR regIR) {
        this.regIR = regIR;
    }

    public String toString() {
        return "    " + cg(regIR.reg) + " = call i32 @getchar()" + "\n";
    }
}
