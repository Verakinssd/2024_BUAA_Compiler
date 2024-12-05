package backend.LLVMIR.FuncIR;

import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.CmpAsm;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.MemoryAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;

import static backend.ASM.AsmBuilder.push;
import static backend.ASM.AsmOP.*;
import static backend.ASM.Register.*;

public class IcmpIR extends InstructionIR {
    public String reg;
    public TokenType op;
    public String reg1;
    public String reg2;
    public Integer value1;
    public Integer value2;
    public Integer type;
    public Boolean isGlobal1;
    public Boolean isGlobal2;

    public IcmpIR(String reg, TokenType op, String reg1, Boolean isGlobal1, String reg2, Boolean isGlobal2) {
        this.reg = reg;
        this.op = op;
        this.reg1 = reg1;
        this.reg2 = reg2;
        this.type = 1;
        this.isGlobal1 = isGlobal1;
        this.isGlobal2 = isGlobal2;
    }

    public IcmpIR(String reg, TokenType op, String reg1, Boolean isGlobal1, Integer value2) {
        this.reg = reg;
        this.op = op;
        this.reg1 = reg1;
        this.value2 = value2;
        this.type = 2;
        this.isGlobal1 = isGlobal1;
    }

    public IcmpIR(String reg, TokenType op, Integer value1, String reg2, Boolean isGlobal2) {
        this.reg = reg;
        this.op = op;
        this.value1 = value1;
        this.reg2 = reg2;
        this.type = 3;
        this.isGlobal2 = isGlobal2;
    }

    public IcmpIR(String reg, TokenType op, Integer value1, Integer value2) {
        this.reg = reg;
        this.op = op;
        this.value1 = value1;
        this.value2 = value2;
        this.type = 4;
    }

    public void generateMipsCode(FuncIR funcIR) {
        funcIR.addInstructionAsm(new CommentAsm(this.toString()));
        Register operand1 , operand2;
        if (type == 3 || type == 4) {
            operand1 = AsmBuilder.getRegister(funcIR, value1, T0);
        } else {
            operand1 = AsmBuilder.getRegister(funcIR, reg1, isGlobal1, T0);
        }
        if (type == 2 || type == 4) {
            operand2 = AsmBuilder.getRegister(funcIR, value2, T1);
        } else {
            operand2 = AsmBuilder.getRegister(funcIR, reg2, isGlobal2,T1);
        }
        Register result = T0;
        if (op == TokenType.LSS) {
            funcIR.addInstructionAsm(new CmpAsm(SLT, result, operand1, operand2));
        } else if (op == TokenType.GRE) {
            funcIR.addInstructionAsm(new CmpAsm(SGT, result, operand1, operand2));
        } else if (op == TokenType.LEQ) {
            funcIR.addInstructionAsm(new CmpAsm(SLE, result, operand1, operand2));
        } else if (op == TokenType.GEQ) {
            funcIR.addInstructionAsm(new CmpAsm(SGE, result, operand1, operand2));
        } else if (op == TokenType.EQL) {
            funcIR.addInstructionAsm(new CmpAsm(SEQ, result, operand1, operand2));
        } else {
            funcIR.addInstructionAsm(new CmpAsm(SNE, result, operand1, operand2));
        }
        funcIR.addInstructionAsm(new MemoryAsm(SW, result, SP, push(reg, 4)));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(cg(reg));
        if (op == TokenType.LSS) {
            sb.append(" = icmp slt i32 ");
        } else if (op == TokenType.GRE) {
            sb.append(" = icmp sgt i32 ");
        } else if (op == TokenType.LEQ) {
            sb.append(" = icmp sle i32 ");
        } else if (op == TokenType.GEQ) {
            sb.append(" = icmp sge i32 ");
        } else if (op == TokenType.EQL) {
            sb.append(" = icmp eq i32 ");
        } else {
            sb.append(" = icmp ne i32 ");
        }
        if (type == 1) {
            sb.append(cg(reg1)).append(", ").append(cg(reg2));
        } else if (type == 2) {
            sb.append(cg(reg1)).append(", ").append(value2);
        } else if (type == 3) {
            sb.append(value1).append(", ").append(cg(reg2));
        } else {
            sb.append(value1).append(", ").append(value2);
        }
        sb.append("\n");
        return sb.toString();
    }
}
