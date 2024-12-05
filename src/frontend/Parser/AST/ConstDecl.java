package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolConst;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class ConstDecl extends AstNode {
    public BType bType = new BType();
    public ArrayList<ConstDef> constDefs = new ArrayList<>();

    public void parse(SymbolTable symbolTable,Boolean isGlobal) {
        append(1);
        bType = new BType();
        bType.parse();

        solve(symbolTable, bType, isGlobal);
        while (getNowTokenType() == TokenType.COMMA) {
            append(1);
            solve(symbolTable, bType, isGlobal);
        }
        i_error();
        stringBuilder.append("<ConstDecl>").append("\n");
    }

    private void solve(SymbolTable symbolTable, BType bType, Boolean isGlobal) {
        int row1 = getNowToken().getRow();
        String ident = getNowToken().getContent();
        ConstDef constDef = new ConstDef();
        SymbolConst symbolConst;
        constDef.parse(symbolTable);
        this.constDefs.add(constDef);

        if (symbolTable.contain(ident)) {
            b_error(row1);
        } else {
            symbolConst = new SymbolConst(symbolTable.id , row1 , constDef.ident, isGlobal, constDef.isArray, bType.getType());
            symbolTable.addSymbol(symbolConst);
            constDef.symbol = symbolConst;
        }
    }

    public void generate_global_LLVMIR() {
        for (ConstDef constDef : constDefs) {
            constDef.generate_global_LLVMIR(bType.getType());
        }
    }

    public void generate_local_LLVMIR(FuncIR funcIR) {
        for (ConstDef constDef : constDefs) {
            constDef.generate_local_LLVMIR(funcIR, bType.getType());
        }
    }
}