package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.FuncIR.IcmpIR;
import backend.LLVMIR.FuncIR.ZextIR;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.I1;
import static backend.LLVMIR.RegType.I32;

public class RelExp extends AstNode{
    public ArrayList<AddExp> addExps = new ArrayList<>();
    public ArrayList<TokenType> operators = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        AddExp addExp = new AddExp();
        addExp.parse(symbolTable);
        addExps.add(addExp);
        stringBuilder.append("<RelExp>").append("\n");
        while (getNowTokenType() == TokenType.LSS ||
        getNowTokenType() == TokenType.LEQ ||
        getNowTokenType() == TokenType.GEQ ||
        getNowTokenType() == TokenType.GRE) {
            operators.add(getNowTokenType());
            append(1);
            addExp = new AddExp();
            addExp.parse(symbolTable);
            addExps.add(addExp);
            stringBuilder.append("<RelExp>").append("\n");
        }
    }

    public RegIR generate_LLVMIR(FuncIR funcIR) {
        RegIR regIR1 = addExps.get(0).getVariableValue(funcIR);
        for (int i = 1;i < addExps.size(); i ++) {
            RegIR regIR2 = addExps.get(i).getVariableValue(funcIR);
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
            if (regIR1.type == 1 && regIR2.type == 1) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.value , regIR2.value));
            } else if (regIR1.type == 1 && regIR2.type == 2) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.value , regIR2.reg, regIR2.isGlobal));
            } else if (regIR1.type == 2 && regIR2.type == 1) {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.reg ,regIR1.isGlobal, regIR2.value));
            } else {
                funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, operators.get(i - 1),
                        regIR1.reg ,regIR1.isGlobal, regIR2.reg, regIR2.isGlobal));
            }
            regIR1.type = 2;
            regIR1.regType = I1;
            regIR1.reg = "%" + (FuncIR.reg - 1);
        }
        return regIR1;
    }
}
