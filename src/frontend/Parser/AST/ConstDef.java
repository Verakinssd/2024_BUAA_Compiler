package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.AllocaIR;
import backend.LLVMIR.FuncIR.GetElementPtrIR;
import backend.LLVMIR.FuncIR.StoreIR;
import backend.LLVMIR.GlobalDeclIR.GlobalConstDeclIR;
import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolConst;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.getReference;

public class ConstDef extends AstNode {
    public String ident;
    public Boolean isArray;
    public Symbol symbol;
    public ArrayList<ConstExp> constExps = new ArrayList<>();
    public ConstInitVal constInitVal = new ConstInitVal();

    public void parse(SymbolTable symbolTable) {
        ident = getNowToken().getContent();
        append(1);
        isArray = false;
        if (getNowTokenType() == TokenType.LBRACK) {
            isArray = true;
            append(1);
            ConstExp constExp = new ConstExp();
            constExp.parse(symbolTable);
            this.constExps.add(constExp);
            k_error();
        }
        append(1);
        constInitVal.parse(symbolTable);
        stringBuilder.append("<ConstDef>").append("\n");
    }

    public void generate_global_LLVMIR(TokenType bType) {
        if (constExps.isEmpty()) {
            Integer value = constInitVal.getValue();
            GlobalConstDeclIR globalconstDeclIR;
            RegType regType;
            if (bType == TokenType.INTTK) {
                regType = RegType.I32_PTR;
            } else {
                regType = RegType.I8_PTR;
            }
            globalconstDeclIR = new GlobalConstDeclIR(regType, "@" + ident, value);
            globalDeclIRS.add(globalconstDeclIR);
            if (symbol instanceof SymbolConst symbolConst) {
                symbolConst.setValue(value, "@" + ident , regType);
            }
        } else {
            Integer size = constExps.get(0).getValue();
            ArrayList<Integer> values = new ArrayList<>();
            if (bType == TokenType.INTTK || constInitVal.type != 3) {
                values = constInitVal.getValues();
                int value_size = values.size();
                for (int i = value_size; i < size; i++) {
                    values.add(0);
                }
            } else {
                String string = constInitVal.getString();
                for (int i = 0;i < string.length();i++) {
                    values.add((int) string.charAt(i));
                }
                for (int i = string.length(); i < size; i++) {
                    values.add(0);
                }
            }
            GlobalConstDeclIR globalConstDeclIR;
            RegType regType;
            if (bType == TokenType.INTTK) {
                regType = RegType.I32_PTR_PTR;
                } else {
                regType = RegType.I8_PTR_PTR;
            }
            globalConstDeclIR = new GlobalConstDeclIR(regType, "@" +ident , size , values);
            globalDeclIRS.add(globalConstDeclIR);
            if (symbol instanceof SymbolConst symbolConst) {
                symbolConst.setValue(values,"@" +ident,regType, size);
            }
        }
    }

    public void generate_local_LLVMIR(FuncIR funcIR, TokenType bType) {
        RegType regType;
        if (bType == TokenType.INTTK && !isArray) {
            regType = RegType.I32_PTR;
        } else if (bType == TokenType.INTTK) {
            regType = RegType.I32_PTR_PTR;
        } else if (bType == TokenType.CHARTK && !isArray) {
            regType = RegType.I8_PTR;
        } else {
            regType = RegType.I8_PTR_PTR;
        }
        if (constExps.isEmpty()) {
            Integer value = constInitVal.getValue();
            funcIR.addInstructionIR(new AllocaIR("%" +FuncIR.reg ++, regType));
            if (symbol instanceof SymbolConst symbolConst) {
                symbolConst.setValue(value, "%" + (FuncIR.reg - 1), regType);
            }
            RegIR regIR = new RegIR(getReference(regType), value);
            funcIR.addInstructionIR(new StoreIR(regIR , symbol.reg, regType , symbol.isGlobal));
        } else  {
            Integer arraySize = constExps.get(0).getValue();
            funcIR.addInstructionIR(new AllocaIR("%" +FuncIR.reg ++, regType , arraySize));
            ArrayList<Integer> values = new ArrayList<>();
            if (bType == TokenType.INTTK || constInitVal.type != 3) {
                values = constInitVal.getValues();
            } else {
                String string = constInitVal.getString();
                for (int i = 0;i < string.length();i++) {
                    values.add((int) string.charAt(i));
                }
            }
            if (symbol instanceof SymbolConst symbolConst) {
                symbolConst.setValue(values, "%" + (FuncIR.reg - 1), regType, arraySize);
            }
            for (int i = 0; i < values.size(); i++) {
                funcIR.addInstructionIR(new GetElementPtrIR("%" + (FuncIR.reg ++),
                        getReference(regType) , arraySize, symbol.reg, i));
                RegIR regIR = new RegIR(getReference(getReference(regType)),values.get(i));
                funcIR.addInstructionIR(new StoreIR( regIR
                        , "%" + (FuncIR.reg - 1), getReference(regType) , false));
            }
        }
    }
}
