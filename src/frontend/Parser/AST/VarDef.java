package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.*;
import backend.LLVMIR.GlobalDeclIR.GlobalVarDeclIR;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolTable;
import frontend.Symbol.SymbolVar;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.*;

public class VarDef extends AstNode {
    public String ident;
    public Boolean isArray;
    public Integer type;
    public Symbol symbol;
    public ArrayList<ConstExp> constExps = new ArrayList<>();
    public ArrayList<InitVal> initVals = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        ident = getNowToken().getContent();
        append(1);
        isArray = false;
        if (getNowTokenType() == TokenType.LBRACK) {
            isArray = true;
            append(1);
            ConstExp constExp = new ConstExp();
            constExp.parse(symbolTable);
            constExps.add(constExp);
            k_error();
        }
        this.type = 1;
        if (getNowTokenType() == TokenType.ASSIGN) {
            append(1);
            InitVal initVal = new InitVal();
            initVal.parse(symbolTable);
            initVals.add(initVal);
            this.type = 2;
        }
        stringBuilder.append("<VarDef>").append("\n");
    }

    private RegType getRegType(TokenType bType) {
        RegType regType;
        if (!isArray && bType == TokenType.INTTK) {
            regType = RegType.I32_PTR;
        } else if (!isArray && bType == TokenType.CHARTK) {
            regType = I8_PTR;
        } else if (isArray && bType == TokenType.INTTK) {
            regType = RegType.I32_PTR_PTR;
        } else {
            regType = RegType.I8_PTR_PTR;
        }
        return regType;
    }

    public void generate_global_LLVMIR(TokenType bType) {
        RegType regType = getRegType(bType);
        if (constExps.isEmpty()) {
            Integer value = 0;
            if (!initVals.isEmpty()) {
                value = initVals.get(0).getValue();
            }
            GlobalVarDeclIR globalVarDeclIR;
            globalVarDeclIR = new GlobalVarDeclIR(regType, "@" + ident , value);
            globalDeclIRS.add(globalVarDeclIR);
            if (symbol instanceof SymbolVar symbolVar) {
                symbolVar.setValue(value, "@" + ident, regType);
            }
        } else {
            Integer size = constExps.get(0).getValue();
            boolean isInit = false;
            ArrayList<Integer> values = new ArrayList<>();
            int value_size = 0;
            if (!initVals.isEmpty()) {
                isInit = true;
                if (bType == TokenType.INTTK || initVals.get(0).type != 3) {
                    values = initVals.get(0).getValues();
                } else {
                    String string = initVals.get(0).getString();
                    for (int i = 0;i < string.length();i++) {
                        values.add((int) string.charAt(i));
                    }
                }
                value_size = values.size();
            }
            for (int i = value_size; i < size;i ++) {
                values.add(0);
            }
            GlobalVarDeclIR globalVarDeclIR;
            globalVarDeclIR = new GlobalVarDeclIR(regType , "@" +ident , size , values, isInit);
            globalDeclIRS.add(globalVarDeclIR);
            if (symbol instanceof SymbolVar symbolVar) {
                symbolVar.setValue(values, "@" + ident, regType, size);
            }
        }
    }

    public void generate_local_LLVMIR(FuncIR funcIR,TokenType bType) {
        RegType regType = getRegType(bType);
        if (constExps.isEmpty()) {
            funcIR.addInstructionIR(new AllocaIR("%" + FuncIR.reg ++ , regType));
            if (symbol instanceof SymbolVar symbolVar) {
                symbolVar.setValue("%" + (FuncIR.reg - 1), regType);
            }
            if (!initVals.isEmpty()) {
                RegIR regIR = initVals.get(0).getVariableValue(funcIR).get(0);
               // System.out.println("regIR.type = " + regIR.type);
                if (regIR.type == 2) {
                   // System.out.println("regIR.regType = " + regIR.regType + "regType = " + regType + "\n");
                    if (regIR.regType == I32 && regType == I8_PTR) {
                        funcIR.addInstructionIR(new TruncIR("%" + FuncIR.reg++, I8, regIR.reg, regIR.regType));
                        regIR.reg = "%" + (FuncIR.reg - 1);
                        regIR.regType = I8;
                    } else if (regIR.regType == I8 && regType == I32_PTR) {
                        funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg, regIR.regType));
                        regIR.reg = "%" + (FuncIR.reg - 1);
                        regIR.regType = I32;
                    }
                } else {
                    regIR.regType = getReference(regType);
                }
                funcIR.addInstructionIR(new StoreIR(regIR, symbol.reg , regType , symbol.isGlobal));
            }
        } else {
            Integer arraySize = constExps.get(0).getValue();
            funcIR.addInstructionIR(new AllocaIR("%" +FuncIR.reg ++, regType , arraySize));
            if (symbol instanceof SymbolVar symbolVar) {
                symbolVar.setValue("%" + (FuncIR.reg - 1) , regType , arraySize);
            }
            if (!initVals.isEmpty()) {
                if (bType == TokenType.INTTK || initVals.get(0).type != 3) {
                    ArrayList<RegIR> regIRs = initVals.get(0).getVariableValue(funcIR);
                    for (int i = 0;i < regIRs.size();i++) {
                        funcIR.addInstructionIR(new GetElementPtrIR("%" + (FuncIR.reg ++),
                                getReference(regType) , arraySize, symbol.reg, symbol.isGlobal, i));
                        RegIR regIR1 = new RegIR(getReference(regType) ,"%" + (FuncIR.reg - 1));
                        RegIR regIR2 = regIRs.get(i);
                        if (regIR2.type == 2) {
                            if (regIR2.regType == I32 && regIR1.regType == I8_PTR) {
                                funcIR.addInstructionIR(new TruncIR("%" + FuncIR.reg++, I8, regIR2.reg, regIR2.regType));
                                regIR2.reg = "%" + (FuncIR.reg - 1);
                                regIR2.regType = I8;
                            } else if (regIR2.regType == I8 && regIR1.regType == I32_PTR) {
                                funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR2.reg, regIR2.regType));
                                regIR2.reg = "%" + (FuncIR.reg - 1);
                                regIR2.regType = I32;
                            }
                        } else {
                            regIR2.regType = getReference(regIR1.regType);
                        }
                        funcIR.addInstructionIR(new StoreIR(regIR2, regIR1.reg, regIR1.regType, regIR1.isGlobal));
                    }
                } else {
                    String string = initVals.get(0).getString();
                    for (int i = 0;i < string.length();i++) {
                        funcIR.addInstructionIR(new GetElementPtrIR("%" + (FuncIR.reg ++),
                                getReference(regType) , arraySize, symbol.reg, symbol.isGlobal, i));
                        RegIR regIR = new RegIR(getReference(getReference(regType)), (int) string.charAt(i));
                        funcIR.addInstructionIR(new StoreIR(regIR ,
                                 "%" + (FuncIR.reg - 1), getReference(regType) , false));
                    }
                }
            }
        }
    }
}
