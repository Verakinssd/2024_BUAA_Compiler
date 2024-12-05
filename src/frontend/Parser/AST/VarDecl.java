package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;
import frontend.Symbol.SymbolVar;

import java.util.ArrayList;

public class VarDecl extends AstNode {
    public BType bType = new BType();
    public ArrayList<VarDef> varDefs = new ArrayList<>();

    public void parse(SymbolTable symbolTable,Boolean isGlobal) {
        bType.parse();
        solve(symbolTable, bType, isGlobal);
        while (getNowTokenType() == TokenType.COMMA) {
            append(1);
            solve(symbolTable, bType, isGlobal);
        }
        i_error();
        stringBuilder.append("<VarDecl>").append("\n");
    }

    private void solve(SymbolTable symbolTable, BType bType,Boolean isGlobal) {
        int row1 = getNowToken().getRow();
        String ident = getNowToken().getContent();
        VarDef varDef = new VarDef();
        SymbolVar symbolVar;
        varDef.parse(symbolTable);
        varDefs.add(varDef);

        if (symbolTable.contain(ident)) {
            b_error(row1);
        } else {
            symbolVar = new SymbolVar(symbolTable.id, row1, varDef.ident, isGlobal, varDef.isArray, bType.getType());
            symbolTable.addSymbol(symbolVar);
            varDef.symbol = symbolVar;
        }
    }

    public void generate_global_LLVMIR() {
        for (VarDef varDef : varDefs) {
            varDef.generate_global_LLVMIR(bType.getType());
        }
    }

    public void generate_local_LLVMIR(FuncIR funcIR) {
        for (VarDef varDef : varDefs) {
            varDef.generate_local_LLVMIR(funcIR, bType.getType());
        }
    }
}
