package backend.LLVMIR.FuncIR;

import backend.ASM.AsmOP;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.ComputeAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegType;

import static backend.ASM.AsmBuilder.*;
import static backend.ASM.Register.*;
import static backend.LLVMIR.RegType.I32;
import static backend.LLVMIR.RegType.getReference;
import static backend.ASM.AsmOP.*;

public class GetElementPtrIR extends InstructionIR {
    public String reg1;
    public RegType regType1;
    public String reg2;
    public Boolean isGlobal2;
    public Integer offset;
    public String offsetReg;
    public Boolean isGlobalForOffset;
    Integer arraySize;
    public Integer type;

    public GetElementPtrIR(String reg1,RegType regType1, Integer arraySize ,
                           String reg2 ,Boolean isGlobal2, Integer offset) {
        this.reg1 = reg1;
        this.regType1 = regType1;
        this.reg2 = reg2;
        this.isGlobal2 = isGlobal2;
        this.offset = offset;
        this.arraySize = arraySize;
        this.type = 1;
    }

    public GetElementPtrIR(String reg1,RegType regType1, Integer arraySize ,
                           String reg2 ,Boolean isGlobal2, String offset, Boolean isGlobalForOffset) {
        this.reg1 = reg1;
        this.regType1 = regType1;
        this.reg2 = reg2;
        this.isGlobal2 = isGlobal2;
        this.offsetReg = offset;
        this.isGlobalForOffset = isGlobalForOffset;
        this.arraySize = arraySize;
        this.type = 2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        getRegister(funcIR, reg2, isGlobal2, T0);
        if (type == 1) {
            getRegister(funcIR, offset, T1);
        } else {
            getRegister(funcIR, offsetReg , isGlobalForOffset, T1);
        }
        funcIR.addInstructionAsm(new ComputeAsm(MUL, T1, T1, 4));
        funcIR.addInstructionAsm(new ComputeAsm(ADD, T1, T1 , T0));
        funcIR.addInstructionAsm(new MemoryAsm(SW, T1, SP, push(reg1, 4)));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(cg(reg1)).append(" = getelementptr inbounds ");
        if (arraySize != null) {
            sb.append("[").
                    append(arraySize).
                    append(" x ").
                    append(getReference(regType1)).append("], ").
                    append("[").
                    append(arraySize).
                    append(" x ").
                    append(getReference(regType1)).append("]* ").
                    append(cg(reg2)).
                    append(", ");
                    if (type == 2) {
                        sb.append(I32).append(" 0, ");
                    } else {
                        sb.append(getReference(regType1)).append(" 0, ");
                    }
        } else {
            sb.append(getReference(regType1)).
                    append(", ").
                    append(regType1).
                    append(" ").
                    append(cg(reg2)).
                    append(", ");
        }
        if (type == 1) {
            sb.append(getReference(regType1)).append(" ");
            sb.append(offset);
        } else {
            sb.append(I32).append(" ");
            sb.append(cg(offsetReg));
        }
        sb.append("\n");
        return sb.toString();
    }
}
