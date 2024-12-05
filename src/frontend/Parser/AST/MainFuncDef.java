package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.AllocaIR;
import backend.LLVMIR.FuncIR.FuncFParamIR;
import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.FuncIR.StoreIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolFunc;
import frontend.Symbol.SymbolTable;
import frontend.Symbol.SymbolVar;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.getPtr;

public class MainFuncDef extends AstNode{
    public Block block = new Block();

    public void parse(SymbolTable symbolTable) {
        append(3);
        j_error();
        block.parse(symbolTable, true , 1 , false);
        stringBuilder.append("<MainFuncDef>").append("\n");
    }

    public void generate_LLVMIR() {
        FuncIR.reg = 0;
        FuncIR funcIR = new FuncIR(TokenType.INTTK, "main");
        funcIRs.add(funcIR);
        FuncIR.reg ++;
        block.generate_LLVMIR(funcIR);
    }
}
