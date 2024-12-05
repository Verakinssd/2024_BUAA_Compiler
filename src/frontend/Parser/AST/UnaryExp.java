package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.*;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolFunc;
import frontend.Symbol.SymbolTable;
import backend.LLVMIR.RegIR;

import java.util.ArrayList;
import java.util.Objects;

import static backend.LLVMIR.RegType.*;
import static frontend.Lexer.TokenType.EQL;

public class UnaryExp extends AstNode{
    public boolean isArray;
    public TokenType arrayType;
    public Integer type;
    public UnaryOp unaryOp = new UnaryOp();
    public ArrayList<UnaryExp> unaryExps = new ArrayList<>();
    public SymbolFunc symbolFunc;
    public FuncRParams funcRParams = new FuncRParams();
    public PrimaryExp primaryExp = new PrimaryExp();

    public void parse(SymbolTable symbolTable) {
        if (getNowTokenType() == TokenType.PLUS ||
        getNowTokenType() == TokenType.MINU ||
        getNowTokenType() == TokenType.NOT) {
            unaryOp.parse();
            UnaryExp unaryExp = new UnaryExp();
            unaryExp.parse(symbolTable);
            unaryExps.add(unaryExp);
            isArray = unaryExp.isArray;
            arrayType = unaryExp.arrayType;
            this.type = 3;
        } else if (getNowTokenType() == TokenType.IDENFR &&
                getPreTokenType(1) == TokenType.LPARENT) {
            isArray = false;
            arrayType = null;
            int row = getNowToken().getRow();
            Symbol symbol = symbolTable.getSymbol(getNowToken().getContent());
            if (symbol == null) {
                c_error(row);
            }
            append(2);
            if (isExp()) {
                funcRParams.parse(symbolTable);
            }
            //System.err.println(symbol.toString());
            //System.err.println("funcRParams.num = " + funcRParams.num);
            if (symbol instanceof SymbolFunc func && !Objects.equals(func.num, funcRParams.num)) {
                d_error(row);
            } else if (symbol instanceof SymbolFunc func){
                this.symbolFunc = func;
                FuncFParams funcFParams = func.funcFParams;
                for (int i = 0;i < funcFParams.num;i ++) {
                    if (funcFParams.funcFParams.get(i).isArray != funcRParams.exps.get(i).isArray) {
                        e_error(row);
                        break;
                    } else if (funcFParams.funcFParams.get(i).isArray
                            && funcFParams.funcFParams.get(i).bType.type != funcRParams.exps.get(i).arrayType) {
                        e_error(row);
                        break;
                    }
                }
            }
            j_error();
            this.type = 2;
        } else {
            primaryExp.parse(symbolTable);
            isArray = primaryExp.isArray;
            arrayType = primaryExp.arrayType;
            this.type = 1;
        }
        if (print == 1) {
            stringBuilder.append("<UnaryExp>").append("\n");
        }
    }

    public Integer getValue() {
        if (this.type == 1) {
            return primaryExp.getValue();
        } else if (this.type == 2) {
            System.err.println("getValue of func");
            return ERROR_VALUE;
        } else {
            if (Objects.equals(unaryOp.op, "+")) {
                return unaryExps.get(0).getValue();
            } else if (Objects.equals(unaryOp.op, "-")) {
                return -unaryExps.get(0).getValue();
            } else {
                System.err.println("getValue of not");
                return ERROR_VALUE;
            }
        }
    }

    public RegIR getVariableValue(FuncIR funcIR) {
        if (this.type == 1) {
            return primaryExp.getVariableValue(funcIR);
        } else if (this.type == 2) {
            ArrayList<RegIR> params = funcRParams.getVariableValue(funcIR);
            if (symbolFunc.tokenType == TokenType.VOIDTK) {
                funcIR.addInstructionIR(new CallIR(symbolFunc.reg ,params));
                return new RegIR("Void_Function");
            } else {
                funcIR.addInstructionIR(new CallIR("%" + FuncIR.reg ++ , symbolFunc.reg ,params , symbolFunc.tokenType));
                if (symbolFunc.tokenType == TokenType.INTTK) {
                    return new RegIR(I32, "%" + (FuncIR.reg - 1));
                } else {
                    return new RegIR(I8 , "%" + (FuncIR.reg - 1));
                }
            }
        } else {
            RegIR regIR = unaryExps.get(0).getVariableValue(funcIR);
            if (Objects.equals(unaryOp.op, "+")) {
                return regIR;
            } else if (Objects.equals(unaryOp.op, "-")) {
                if (regIR.regType == I8) {
                    if (regIR.type == 2) {
                        funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg, I8));
                        regIR.reg = "%" + (FuncIR.reg - 1);
                    }
                    regIR.regType = I32;
                }
                RegIR regIR1 = new RegIR(I32 , 0);
                funcIR.addInstructionIR(new ComputeIR("%" + FuncIR.reg ++ , "sub", regIR1, regIR));
                return new RegIR(I32, "%" + (FuncIR.reg - 1));
            } else {
                if (regIR.regType != I32) {
                    if (regIR.type == 2) {
                        funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg , regIR.regType));
                        regIR.reg = "%" + (FuncIR.reg - 1);
                    }
                    regIR.regType = I32;
                }
                if (regIR.type == 1) {
                    funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, EQL, regIR.value, 0));
                } else {
                    funcIR.addInstructionIR(new IcmpIR("%" + FuncIR.reg++, EQL, regIR.reg, regIR.isGlobal, 0));
                }
                regIR.reg = "%" + (FuncIR.reg - 1);
                funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg, I1));
                return new RegIR(I32, "%" + (FuncIR.reg - 1));
            }
        }
    }
}
