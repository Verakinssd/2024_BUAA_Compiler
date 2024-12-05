package backend.LLVMIR.FuncIR;

import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.*;
import backend.ASM.Register;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import backend.LLVMIR.RegIR;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static backend.ASM.AsmBuilder.*;
import static backend.ASM.AsmOP.*;
import static backend.ASM.Register.*;
import static backend.ASM.Register.SP;

public class CallIR extends InstructionIR{
    public TokenType funcType;
    public ArrayList<RegIR> regIRs;
    public String funcReg;
    public String reg;
    public Integer type;

    public CallIR(String funcReg, ArrayList<RegIR> regIRs) {
        this.funcType = TokenType.VOIDTK;
        this.regIRs = regIRs;
        this.funcReg = funcReg;
        this.type = 1;
    }

    public CallIR(String reg, String funcReg, ArrayList<RegIR> regIRs, TokenType funcType){
        this.funcType = funcType;
        this.regIRs = regIRs;
        this.funcReg = funcReg;
        this.reg = reg;
        this.type = 2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        HashSet<Register> registerSet = new HashSet<>(registerMap.values());
        HashMap<Register, Integer> registerOffsetMap = new HashMap<>();
        for (Register reg : registerSet) {
            Integer offset = allocate(4);
            funcIR.addInstructionAsm(new MemoryAsm(SW, reg, SP, offset));
            registerOffsetMap.put(reg, offset);
        }
        funcIR.addInstructionAsm(new MemoryAsm(SW, SP, SP, allocate(4)));
        funcIR.addInstructionAsm(new MemoryAsm(SW, RA, SP, allocate(4)));
        Integer offset = AsmBuilder.offset;
        for (RegIR regIR : regIRs) {
            if (regIR.type == 1) {
                funcIR.addInstructionAsm(new LiAsm(T0, regIR.value));
                funcIR.addInstructionAsm(new MemoryAsm(SW ,T0, SP, allocate(4)));
            } else {
                Integer offset1 = offsetMap.get(reg);
                if (offset1 == null) {
                    push(reg, 4);
                } else {
                    if (type == 1) {
                        getRegister(funcIR, regIR.reg, false, T0);
                    } else {
                        getRegister(funcIR, regIR.value, T0);
                    }
                    funcIR.addInstructionAsm(new MemoryAsm(SW, T0, SP, allocate(4)));
                }
            }
        }
        funcIR.addInstructionAsm(new ComputeAsm(ADDI, SP , SP ,offset));
        funcIR.addInstructionAsm(new JumpAsm(JAL, funcReg.substring(1)));
        funcIR.addInstructionAsm(new MemoryAsm(LW, RA, SP, 0));
        funcIR.addInstructionAsm(new MemoryAsm(LW, SP, SP, 4));
        for (Register reg : registerSet) {
            funcIR.addInstructionAsm(new MemoryAsm(LW, reg, SP, registerOffsetMap.get(reg)));
        }
        if (funcType != TokenType.VOIDTK) {
            funcIR.addInstructionAsm(new MemoryAsm(SW, V0, SP, push(reg, 4)));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        if (funcType == TokenType.VOIDTK) {
            sb.append("call void ").append(funcReg).append("(");
        } else if (funcType == TokenType.INTTK) {
            sb.append(cg(reg)).append(" = call i32 ").append(funcReg).append("(");;
        } else {
            sb.append(cg(reg)).append(" = call i8 ").append(funcReg).append("(");
        }
        for (int i = 0; i < regIRs.size(); i++) {
            sb.append(regIRs.get(i).toString());
            if (i!= regIRs.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")\n");
        return sb.toString();
    }
}
