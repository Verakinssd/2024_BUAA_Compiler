package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.*;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolFunc;
import frontend.Symbol.SymbolTable;
import frontend.Symbol.SymbolVar;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.getPtr;

public class FuncDef extends AstNode{
    public String ident;
    public FuncType funcType = new FuncType();
    public FuncFParams funcFParams = new FuncFParams();
    public Block block = new Block();
    public Symbol symbol;

    public void parse(SymbolTable symbolTable) {
        funcType.parse();

        int row = getNowToken().getRow();
        String ident = getNowToken().getContent();
        this.ident = ident;

        append(2);

        SymbolTable symbolTable1 = new SymbolTable(symbolTable);
        if (getNowTokenType() == TokenType.INTTK || getNowTokenType() == TokenType.CHARTK) {
            funcFParams.parse(symbolTable1);
        }

        if (symbolTable.contain(ident)) {
            b_error(row);
        } else {
            SymbolFunc symbol = new SymbolFunc(symbolTable.id, row, ident, funcType.funcType, funcFParams.num, funcFParams);
            symbolTable.addSymbol(symbol);
            this.symbol = symbol;
        }

        j_error();
        Integer isReturn = (funcType.funcType == TokenType.INTTK ||
                funcType.funcType == TokenType.CHARTK) ? 1 : 0;
        block.parse(symbolTable1, false , isReturn, false);
        stringBuilder.append("<FuncDef>").append("\n");
    }

    public void generate_LLVMIR() {
        FuncIR.reg = 0;
        FuncIR funcIR = new FuncIR(funcType.funcType, ident);
        funcIRs.add(funcIR);
        ArrayList<FuncFParamIR> params = funcFParams.generate_LLVMIR();
        funcIR.setParams(params);
        if (symbol instanceof SymbolFunc symbolFunc) {
            symbolFunc.setReg("@" + ident);
        }
        FuncIR.reg ++;
        int num = 0;
        for (FuncFParamIR param : params) {
            funcIR.addInstructionIR(new AllocaIR("%" + FuncIR.reg ++ , getPtr(param.regType)));
            RegIR regIR = new RegIR(param.regType , param.reg);
            funcIR.addInstructionIR(new StoreIR(regIR, "%" + (FuncIR.reg - 1) , getPtr(param.regType), false));
            if (funcFParams.funcFParams.get(num++).symbol instanceof SymbolVar symbolVar) {
                symbolVar.setValue("%" + (FuncIR.reg - 1), getPtr(param.regType));
            }
        }
        block.generate_LLVMIR(funcIR);
        if (!funcIR.isReturn) {
            funcIR.addInstructionIR(new ReturnIR());
        }
    }
}
