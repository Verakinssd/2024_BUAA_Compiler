package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncFParamIR;
import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolTable;
import frontend.Symbol.SymbolVar;

public class FuncFParam extends AstNode{
    public BType bType = new BType();
    public boolean isArray;
    public String ident;
    public Symbol symbol;

    public void parse(SymbolTable symbolTable) {
        bType.parse();
        String ident = getNowToken().getContent();
        this.ident = ident;
        int row = getNowToken().getRow();
        if (symbolTable.contain(ident)) {
            b_error(row);
        }
        append(1);
        SymbolVar symbolVar;
        if (getNowTokenType() == TokenType.LBRACK) {
            isArray = true;
            append(1);
            k_error();
            symbolVar = new SymbolVar(symbolTable.id, row, ident , false, true, bType.getType());
        } else {
            isArray = false;
            symbolVar = new SymbolVar(symbolTable.id, row, ident , false, false, bType.getType());
        }
        symbolTable.addSymbol(symbolVar);
        symbol = symbolVar;
        stringBuilder.append("<FuncFParam>").append("\n");
    }

    public FuncFParamIR generate_LLVMIR() {
        if (bType.getType() == TokenType.INTTK && !isArray) {
            return new FuncFParamIR(RegType.I32, ident ,"%" + FuncIR.reg ++);
        } else if (bType.getType() == TokenType.INTTK && isArray) {
            return new FuncFParamIR(RegType.I32_PTR, ident , "%" + FuncIR.reg ++);
        } else if (bType.getType() == TokenType.CHARTK && !isArray) {
            return new FuncFParamIR(RegType.I8, ident , "%" + FuncIR.reg ++);
        } else {
            return new FuncFParamIR(RegType.I8_PTR, ident , "%" + FuncIR.reg ++);
        }
    }
}
