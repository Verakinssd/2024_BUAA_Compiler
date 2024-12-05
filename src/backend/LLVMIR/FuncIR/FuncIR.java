package backend.LLVMIR.FuncIR;

import backend.ASM.AsmBuilder;
import backend.ASM.FuncAsm.CommentAsm;
import backend.ASM.FuncAsm.InstructionAsm;
import backend.ASM.FuncAsm.LabelAsm;
import backend.ASM.Register;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;

import static backend.ASM.AsmBuilder.push;

public class FuncIR {
    public static Integer reg;

    public ArrayList<InstructionAsm> instructionAsms = new ArrayList<>();
    public TokenType funcType;
    public String ident;
    public HashMap<String, Register> registerMap = new HashMap<>();
    public ArrayList<FuncFParamIR> params = new ArrayList<>();
    public ArrayList<InstructionIR> instructionIRS = new ArrayList<>();
    public Boolean isReturn = false;

    public FuncIR(TokenType funcType,String ident) {
        this.funcType = funcType;
        this.ident = ident;
    }

    public void setParams(ArrayList<FuncFParamIR> params) {
        this.params.addAll(params);
    }

    public void addInstructionAsm(InstructionAsm instructionAsm) {
        instructionAsms.add(instructionAsm);
    }

    public void addInstructionIR(InstructionIR instructionIR) {
        instructionIRS.add(instructionIR);
//        System.out.print(ident + " " + instructionIR);
    }

    public String getMipsCode() {
        instructionAsms.add(new CommentAsm("    " + ident + ":\n"));
        instructionAsms.add(new LabelAsm(ident));
        AsmBuilder.pre(registerMap);
        for (FuncFParamIR p : params) {
            push(p.reg , 4);
        }
        for (InstructionIR i : instructionIRS) {
            i.generateMipsCode(this);
        }
        StringBuilder sb = new StringBuilder();
        for (InstructionAsm i : instructionAsms) {
            sb.append(i);
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("define dso_local ");
        if (funcType == TokenType.INTTK) {
            sb.append("i32 ");
        } else if (funcType == TokenType.CHARTK) {
            sb.append("i8 ");
        } else {
            sb.append("void ");
        }
        sb.append("@").append(ident).append("(");
        if (params != null) {
            for (FuncFParamIR p : params) {
                sb.append(p.toString());
                if (p!= params.get(params.size() - 1)) {
                    sb.append(", ");
                }
            }
        }
        sb.append(") {\n");
        for (InstructionIR i : instructionIRS) {
            sb.append(i);
        }
        sb.append("}\n");
        return sb.toString();
    }
}
