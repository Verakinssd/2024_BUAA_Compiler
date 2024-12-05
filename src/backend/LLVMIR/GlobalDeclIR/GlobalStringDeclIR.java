package backend.LLVMIR.GlobalDeclIR;

import backend.ASM.GlobalDeclAsm.GlobalStringDeclAsm;

import static backend.LLVMIR.FuncIR.InstructionIR.cg;

public class GlobalStringDeclIR extends GlobalDeclIR{
    public static Integer id = 0;
    public Integer idNum;
    public String str;
    public Integer arraySize;
    public String reg;

    public GlobalStringDeclIR(String str, Integer arraySize) {
        this.reg = "@.str" + (id > 0 ? "." + id : "");
        this.idNum = id ++;
        this.str = str;
        this.arraySize = arraySize;
    }

    public GlobalStringDeclAsm getMipsCode() {
        return new GlobalStringDeclAsm(reg, str, arraySize);
    }


    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(cg(reg)).append(" = private unnamed_addr constant [").append(arraySize)
                .append(" x i8] c\"").append(str).append("\", align 1\n");
        return sb.toString();
    }
}
