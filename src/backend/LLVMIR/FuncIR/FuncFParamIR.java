package backend.LLVMIR.FuncIR;

import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import static backend.LLVMIR.FuncIR.InstructionIR.cg;

public class FuncFParamIR {
    public RegType regType;
    public String ident;
    public String reg;

    public FuncFParamIR(RegType regType, String ident, String reg) {
        this.regType = regType;
        this.ident = ident;
        this.reg = reg;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(regType).append(" ").append(cg(reg));
        return sb.toString();
    }
}
