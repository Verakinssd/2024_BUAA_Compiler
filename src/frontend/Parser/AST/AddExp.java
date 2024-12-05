package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.ComputeIR;
import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.FuncIR.TruncIR;
import backend.LLVMIR.FuncIR.ZextIR;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.I32;
import static backend.LLVMIR.RegType.I8;

public class AddExp extends AstNode{
    public boolean isArray;
    public TokenType arrayType;
    public ArrayList<MulExp> mulExps = new ArrayList<>();
    public ArrayList<TokenType> opts = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        MulExp mulExp = new MulExp();
        mulExp.parse(symbolTable);
        mulExps.add(mulExp);
        isArray = mulExp.isArray;
        arrayType = mulExp.arrayType;
        if (print == 1) {
            stringBuilder.append("<AddExp>").append("\n");
        }
        while (getNowTokenType() == TokenType.PLUS || getNowTokenType() == TokenType.MINU) {
            opts.add(getNowTokenType());
            append(1);
            mulExp = new MulExp();
            mulExp.parse(symbolTable);
            mulExps.add(mulExp);
            isArray = false;
            arrayType = null;
            if (print == 1) {
                stringBuilder.append("<AddExp>").append("\n");
            }
        }
    }

    public Integer getValue() {
        Integer value = mulExps.get(0).getValue();
        for (int i = 0; i < opts.size(); i++) {
            if (opts.get(i) == TokenType.PLUS) {
                value += mulExps.get(i + 1).getValue();
            } else {
                value -= mulExps.get(i + 1).getValue();
            }
        }
        return value;
    }

    public RegIR getVariableValue(FuncIR funcIR) {
        RegIR regIR = mulExps.get(0).getVariableValue(funcIR);
        for (int i = 0;i < opts.size();i ++) {
            RegIR regIR1 = mulExps.get(i + 1).getVariableValue(funcIR);
            //System.out.println("regIR.regType: " + regIR.regType + " regIR1.regType: " + regIR1.regType + "\n");
            if (regIR.regType == I8) {
                if (regIR.type == 2) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg, regIR.regType));
                    regIR.reg = "%" + (FuncIR.reg - 1);
                }
                regIR.regType = I32;
            }
            if (regIR1.regType == I8) {
                if (regIR1.type == 2) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR1.reg, regIR1.regType));
                    regIR1.reg = "%" + (FuncIR.reg - 1);
                }
                regIR1.regType = I32;
            }
            if (opts.get(i) == TokenType.PLUS) {
                funcIR.addInstructionIR(new ComputeIR("%" + FuncIR.reg ++ , "add", regIR, regIR1));
            } else if (opts.get(i) == TokenType.MINU) {
                funcIR.addInstructionIR(new ComputeIR("%" + FuncIR.reg ++ , "sub", regIR, regIR1));
            }
            regIR = new RegIR(I32, "%" + (FuncIR.reg - 1));
            //System.out.println("regIR.type: \n" + regIR.type);
        }
        return regIR;
    }

}
