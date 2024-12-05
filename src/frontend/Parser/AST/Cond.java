package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.BasicBlockIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Symbol.SymbolTable;

public class Cond extends AstNode{
    public LOrExp lorExp = new LOrExp();

    public void parse(SymbolTable symbolTable) {
        lorExp.parse(symbolTable);
        stringBuilder.append("<Cond>").append("\n");
    }

    public void generate_LLVMIR(FuncIR funcIR, BasicBlockIR trueBB, BasicBlockIR falseBB) {
        lorExp.generate_LLVMIR(funcIR, trueBB, falseBB);
    }
}
