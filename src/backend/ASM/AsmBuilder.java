package backend.ASM;

import backend.ASM.FuncAsm.LaAsm;
import backend.ASM.FuncAsm.LiAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.RegIR;

import java.util.HashMap;

import static backend.ASM.AsmOP.LW;
import static backend.ASM.Register.SP;

public class AsmBuilder {
    public static HashMap<String, Integer> offsetMap = new HashMap<>();
    public static HashMap<String, Register> registerMap = new HashMap<>();
    public static Integer offset = 0;

    public static Integer push(String reg, Integer off) {
        Integer curOffset = offsetMap.get(reg);
        if (curOffset == null) {
            offset -= off;
            offsetMap.put(reg, offset);
            return offset;
        } else {
            return curOffset;
        }
    }

    public static void pre(HashMap<String, Register> registerMap) {
        offset = 0;
        offsetMap = new HashMap<>();
        AsmBuilder.registerMap = registerMap;
    }

    public static Integer allocate(Integer off) {
        offset -= off;
        return offset;
    }

    public static Register getRegister(FuncIR funcIR,Integer value, Register register) {
        funcIR.addInstructionAsm(new LiAsm(register, value));
        return register;
    }

    public static Register getRegister(FuncIR funcIR, String reg,Boolean isGlobal, Register register) {
        if (isGlobal) {
            funcIR.addInstructionAsm(new LaAsm(register, reg));
        } else {
            Integer offset = offsetMap.get(reg);
            if (offset == null) {
                offset = push(reg, 4);
            }
            funcIR.addInstructionAsm(new MemoryAsm(LW, register, SP, offset));
        }
        return register;
    }
}
