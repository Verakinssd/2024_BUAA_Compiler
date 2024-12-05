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

public class MulExp extends AstNode{
    public boolean isArray;
    public TokenType arrayType;
    public ArrayList<UnaryExp> unaryExps = new ArrayList<>();
    public ArrayList<TokenType> opts = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        UnaryExp unaryExp = new UnaryExp();
        unaryExp.parse(symbolTable);
        unaryExps.add(unaryExp);
        isArray = unaryExp.isArray;
        arrayType = unaryExp.arrayType;
        if (print == 1) {
            stringBuilder.append("<MulExp>").append("\n");
        }
        while (getNowTokenType() == TokenType.MULT ||
        getNowTokenType() == TokenType.DIV ||
        getNowTokenType() == TokenType.MOD) {
            isArray = false;
            arrayType = null;
            opts.add(getNowTokenType());
            append(1);
            unaryExp = new UnaryExp();
            unaryExp.parse(symbolTable);
            unaryExps.add(unaryExp);
            if (print == 1) {
                stringBuilder.append("<MulExp>").append("\n");
            }
        }
    }

    public Integer getValue() {
        Integer value = unaryExps.get(0).getValue();
        for (int i = 0;i < opts.size();i++) {
            if (opts.get(i) == TokenType.MULT) {
                value *= unaryExps.get(i+1).getValue();
            } else if (opts.get(i) == TokenType.DIV) {
                value /= unaryExps.get(i+1).getValue();
            } else {
                value %= unaryExps.get(i+1).getValue();
            }
        }
        return value;
    }

    public RegIR getVariableValue(FuncIR funcIR) {
        RegIR regIR = unaryExps.get(0).getVariableValue(funcIR);
        for (int i = 0;i < opts.size();i ++) {
            RegIR regIR1 = unaryExps.get(i + 1).getVariableValue(funcIR);
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
            if (opts.get(i) == TokenType.MULT) {
                funcIR.addInstructionIR(new ComputeIR("%" + FuncIR.reg ++ , "mul", regIR, regIR1));
            } else if (opts.get(i) == TokenType.DIV) {
                funcIR.addInstructionIR(new ComputeIR("%" + FuncIR.reg ++ , "sdiv", regIR, regIR1));
            } else if (opts.get(i) == TokenType.MOD) {
                funcIR.addInstructionIR(new ComputeIR("%" + FuncIR.reg ++ , "srem", regIR, regIR1));
            }
            regIR = new RegIR(I32, "%" + (FuncIR.reg - 1));
        }
        return regIR;
    }
}
