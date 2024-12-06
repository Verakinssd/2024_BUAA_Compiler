package backend.LLVMIR;

import static backend.LLVMIR.FuncIR.InstructionIR.cg;

public class RegIR {
    public RegType regType;
    public String reg;
    public Integer value;
    public Integer type;
    public String specialReg;
    public Boolean isGlobal = false;

    public RegIR(RegType regType, Integer value) {
        this.regType = regType;
        this.value = value;
        this.type = 1;
    }

    public RegIR(RegType regType, String reg) {
        this.regType = regType;
        this.reg = reg;
        this.type = 2;
    }

    public RegIR(RegType regType, String reg,Boolean isGlobal) {
        this.regType = regType;
        this.reg = reg;
        this.type = 2;
        this.isGlobal = isGlobal;
    }

    public RegIR(String specialReg) {
        this.specialReg = specialReg;
        this.type = 3;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (type == 1) {
            sb.append(regType).append(" ").append(value);
        } else if (type == 2) {
            sb.append(regType).append(" ").append(cg(reg));
        } else {
            sb.append(specialReg);
        }
        return sb.toString();
    }
}
