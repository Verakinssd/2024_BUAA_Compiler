package backend.LLVMIR.FuncIR;

import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.ComputeAsm;
import backend.ASM.FuncAsm.HiLoAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;

import java.util.Objects;

import static backend.ASM.AsmBuilder.push;
import static backend.ASM.AsmOP.*;
import static backend.ASM.Register.*;

public class ComputeIR extends InstructionIR {
    public String op;
    public String reg;
    public RegIR regIR1;
    public RegIR regIR2;

    public ComputeIR(String reg, String op, RegIR regIR1, RegIR regIR2) {
        this.reg = reg;
        this.op = op;
        this.regIR1 = regIR1;
        this.regIR2 = regIR2;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        Register operand1 , operand2;
        if (regIR1.type == 1) {
            operand1 = AsmBuilder.getRegister(funcIR, regIR1.value, T0);
        } else {
            operand1 = AsmBuilder.getRegister(funcIR, regIR1.reg , regIR1.isGlobal, T0);
        }
        if (regIR2.type == 1) {
            operand2 = AsmBuilder.getRegister(funcIR, regIR2.value, T1);
        } else {
            operand2 = AsmBuilder.getRegister(funcIR, regIR2.reg , regIR2.isGlobal, T1);
        }
        Register result = T0;
        if (op.equals("add")) {
            funcIR.addInstructionAsm(new ComputeAsm(ADDU, result, operand1, operand2));
        } else if (op.equals("sub")) {
            funcIR.addInstructionAsm(new ComputeAsm(SUBU, result, operand1, operand2));
        } else if (op.equals("mul")) {
            funcIR.addInstructionAsm(new ComputeAsm(MULT , operand1, operand2));
            funcIR.addInstructionAsm(new HiLoAsm(MFLO, result));
        } else if (op.equals("sdiv")) {
            funcIR.addInstructionAsm(new ComputeAsm(DIV, operand1, operand2));
            funcIR.addInstructionAsm(new HiLoAsm(MFLO, result));
        } else if (op.equals("srem")) {
            funcIR.addInstructionAsm(new ComputeAsm(DIV, operand1, operand2));
            funcIR.addInstructionAsm(new HiLoAsm(MFHI, result));
        } else {
            System.err.println("Error: unknown op: " + op);
        }
        Integer offset = push(reg, 4);
        funcIR.addInstructionAsm(new MemoryAsm(SW, result, SP, offset));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(cg(reg)).append(" = ").append(op).append(" ");
        if (!Objects.equals(op, "sdiv") && !Objects.equals(op, "srem")) {
            sb.append("nsw ");
        }
        if (regIR1.type == 1) {
            sb.append(regIR1.regType).append(" ").append(regIR1.value).append(", ");
        } else {
            sb.append(regIR1.regType).append(" ").append(cg(regIR1.reg)).append(", ");
        }
        if (regIR2.type == 1) {
            sb.append(regIR2.value).append("\n");
        } else {
            sb.append(cg(regIR2.reg)).append("\n");
        }
        return sb.toString();
    }
}
