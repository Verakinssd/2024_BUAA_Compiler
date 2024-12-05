package backend.LLVMIR.FuncIR;

import backend.LLVMIR.RegType;

public class ZextIR extends InstructionIR {
    String reg1;
    RegType regType1;
    String reg2;
    RegType regType2;

    public ZextIR(String reg1, RegType regType1, String reg2, RegType regType2) {
        this.reg1 = reg1;
        this.regType1 = regType1;
        this.reg2 = reg2;
        this.regType2 = regType2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(cg(reg1)).append(" = zext ").append(regType2).append(" ").append(cg(reg2)).
                append(" to ").append(regType1).append("\n");
        return sb.toString();
    }
}
