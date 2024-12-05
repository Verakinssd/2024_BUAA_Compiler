package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.*;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.*;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.I32;
import static backend.LLVMIR.RegType.getReference;

public class LVal extends AstNode{
    public String ident;
    public ArrayList<Exp> exps = new ArrayList<>();
    public boolean isArray;
    public TokenType arrayType;
    public Symbol symbol;

    public void parse(SymbolTable symbolTable) {
        ident = getNowToken().getContent();
        symbol = symbolTable.getSymbol(ident);
        if (symbol == null) {
            c_error(getNowToken().getRow());
        }
        append(1);
        if (symbol instanceof SymbolVar var) {
            isArray = var.isArray;
            if (isArray) {
                arrayType = var.bType;
            }
        } else if (symbol instanceof SymbolConst const_) {
            isArray = const_.isArray;
            if (isArray) {
                arrayType = const_.bType;
            }
        }

        if (getNowTokenType() == TokenType.LBRACK) {
            append(1);
            Exp exp = new Exp();
            exp.parse(symbolTable);
            exps.add(exp);
            k_error();
            isArray = false;
            arrayType = null;
        }
        if (print == 1) {
            stringBuilder.append("<LVal>").append("\n");
        }
    }

    public Integer getValue() {
        if (exps.isEmpty()) {
            if (symbol instanceof SymbolConst) {
                return ((SymbolConst) symbol).getValue();
            } else if (symbol instanceof SymbolVar) {
                return ((SymbolVar) symbol).getValue();
            } else {
                System.err.println("Error: symbol is not a constant or variable.");
                return AstNode.ERROR_VALUE;
            }
        } else {
            if (symbol instanceof SymbolConst) {
                return ((SymbolConst) symbol).getValue(exps.get(0).getValue());
            } else if (symbol instanceof SymbolVar) {
                return ((SymbolVar) symbol).getValue(exps.get(0).getValue());
            } else {
                System.err.println("Error: symbol is not a constant or variable.");
                return AstNode.ERROR_VALUE;
            }
        }
    }

    public RegIR getVariableValue(FuncIR funcIR, Boolean reload) {
        RegType regType = symbol.regType;
        String reg = symbol.reg;
//        System.out.println("LVal: " + symbol + " " +  regType + " " + reg);
        if (exps.isEmpty() && !symbol.isArray) {
            if (reload) {
                funcIR.addInstructionIR(new LoadIR(getReference(regType), "%" + FuncIR.reg++, regType, reg, symbol.isGlobal));
                reg = "%" + (FuncIR.reg - 1);
                return new RegIR(getReference(regType), reg);
            } else {
                return new RegIR(regType, reg);
            }
        } else {
            RegIR regIR;
            //get the value of the index
            if (!exps.isEmpty()) {
                regIR = exps.get(0).getVariableValue(funcIR);
            } else {
                regIR = new RegIR(I32, 0);
            }
            if (regIR.regType != I32) {
                if (regIR.type == 2) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg ++ , I32, regIR.reg , regIR.regType));
                    regIR.reg = "%" + (FuncIR.reg - 1);
                }
                regIR.regType = I32;
            }
            if (symbol.arraySize == null) {
                funcIR.addInstructionIR(new LoadIR(getReference(regType), "%" + FuncIR.reg++, regType, reg, symbol.isGlobal));
                reg = "%" + (FuncIR.reg - 1);
            }
            if (regIR.type == 1) {
                funcIR.addInstructionIR(new GetElementPtrIR("%" + (FuncIR.reg++),
                        getReference(regType), symbol.arraySize, reg, regIR.value));
            } else {
                funcIR.addInstructionIR(new GetElementPtrIR("%" + (FuncIR.reg++),
                        getReference(regType), symbol.arraySize, reg, regIR.reg));
            }
            if (!exps.isEmpty() && reload) {
                funcIR.addInstructionIR(new LoadIR(getReference(getReference(regType)),
                        "%" + (FuncIR.reg ++), getReference(regType), "%"  + (FuncIR.reg - 2) , false));
                return new RegIR(getReference(getReference(regType)) , "%" + (FuncIR.reg - 1));
            } else {
                return new RegIR(getReference(regType), "%" + (FuncIR.reg - 1));
            }
        }
    }
}
