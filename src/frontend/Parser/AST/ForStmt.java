package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.FuncIR.StoreIR;
import backend.LLVMIR.FuncIR.TruncIR;
import backend.LLVMIR.FuncIR.ZextIR;
import backend.LLVMIR.RegIR;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolTable;

import static backend.LLVMIR.RegType.*;

public class ForStmt extends AstNode {
    public LVal lVal = new LVal();
    public Exp exp = new Exp();

    public void parse(SymbolTable symbolTable) {
        int row1 = getNowToken().getRow();
        lVal.parse(symbolTable);
        Symbol symbol = symbolTable.getSymbol(lVal.ident);
        h_error(symbol , row1);
        append(1);
        exp.parse(symbolTable);
        stringBuilder.append("<ForStmt>").append("\n");
    }

    public void generate_LLVMIR(FuncIR funcIR) {
        RegIR regIR1 = lVal.getVariableValue(funcIR, false);
        RegIR regIR2 = exp.getVariableValue(funcIR);
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
        funcIR.addInstructionIR(new StoreIR(regIR2, regIR1.reg, regIR1.regType , regIR1.isGlobal));
    }
}
