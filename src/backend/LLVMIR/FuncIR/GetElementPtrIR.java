package backend.LLVMIR.FuncIR;

import backend.LLVMIR.RegType;

import static backend.LLVMIR.RegType.I32;
import static backend.LLVMIR.RegType.getReference;

public class GetElementPtrIR extends InstructionIR {
    public String reg1;
    public RegType regType1;
    public String reg2;
    public Integer offset;
    public String offsetReg;
    Integer arraySize;
    public Integer type;

    public GetElementPtrIR(String reg1,RegType regType1, Integer arraySize ,String reg2 , Integer offset) {
        this.reg1 = reg1;
        this.regType1 = regType1;
        this.reg2 = reg2;
        this.offset = offset;
        this.arraySize = arraySize;
        this.type = 1;
    }

    public GetElementPtrIR(String reg1,RegType regType1, Integer arraySize ,String reg2 , String offset) {
        this.reg1 = reg1;
        this.regType1 = regType1;
        this.reg2 = reg2;
        this.offsetReg = offset;
        this.arraySize = arraySize;
        this.type = 2;
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
