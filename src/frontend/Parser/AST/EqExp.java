package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.*;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.I1;
import static backend.LLVMIR.RegType.I32;

public class EqExp extends AstNode{
    public ArrayList<RelExp> relExps = new ArrayList<>();
    public ArrayList<TokenType> operators = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        RelExp relExp = new RelExp();
        relExp.parse(symbolTable);
        relExps.add(relExp);
        stringBuilder.append("<EqExp>").append("\n");
        while (getNowTokenType() == TokenType.EQL ||
        getNowTokenType() == TokenType.NEQ) {
            operators.add(getNowTokenType());
            append(1);
            relExp = new RelExp();
            relExp.parse(symbolTable);
            relExps.add(relExp);
            stringBuilder.append("<EqExp>").append("\n");
        }
    }

    public void generate_LLVMIR(FuncIR funcIR, BasicBlockIR trueBB, BasicBlockIR falseBB) {
        RegIR regIR1 = relExps.get(0).generate_LLVMIR(funcIR);
        for (int i = 1; i < relExps.size(); i++) {
            RegIR regIR2 = relExps.get(i).generate_LLVMIR(funcIR);
            if (regIR1.regType != I32) {
                if (regIR1.type == 2) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR1.reg , regIR1.regType));
                    regIR1.reg = "%" + (FuncIR.reg - 1);
                }
                regIR1.regType = I32;
            }
            if (regIR2.regType != I32) {
                if (regIR2.type == 2) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR2.reg , regIR2.regType));
                    regIR2.reg = "%" + (FuncIR.reg - 1);
                }
                regIR2.regType = I32;
            }
//            System.out.println(regIR1 + " " + regIR2 + " " + operators.get(i - 1));
            if (regIR1.type == 1 && regIR2.type == 1) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.value , regIR2.value));
            } else if (regIR1.type == 1 && regIR2.type == 2) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.value , regIR2.reg, regIR2.isGlobal));
            } else if (regIR1.type == 2 && regIR2.type == 1) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.reg ,regIR1.isGlobal , regIR2.value));
            } else {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.reg ,regIR1.isGlobal, regIR2.reg, regIR2.isGlobal));
            }
            regIR1.reg = "%" + (FuncIR.reg - 1);
            regIR1.regType = I1;
            regIR1.type = 2;
        }
        if (regIR1.regType != I1) {
            if (regIR1.regType != I32) {
                if (regIR1.type == 2) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR1.reg , regIR1.regType));
                    regIR1.reg = "%" + (FuncIR.reg - 1);
                }
                regIR1.regType = I32;
            }
            if (regIR1.type == 1) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg ++ , TokenType.NEQ, regIR1.value, 0));
                regIR1.type = 2;
            } else {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg ++ , TokenType.NEQ, regIR1.reg, regIR1.isGlobal, 0));
            }
            regIR1.reg = "%" + (FuncIR.reg - 1);
            regIR1.regType = I1;
        }
        funcIR.addInstructionIR(new BrIR(regIR1.reg, trueBB, falseBB));
    }
}
