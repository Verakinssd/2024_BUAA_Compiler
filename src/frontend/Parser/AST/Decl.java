package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

public class Decl extends AstNode {
    public ConstDecl constDecl = new ConstDecl();
    public VarDecl varDecl = new VarDecl();
    public Integer type;

    public void parse(SymbolTable symbolTable, Boolean isGlobal) {
        if (getNowTokenType() == TokenType.CONSTTK) {
            constDecl.parse(symbolTable, isGlobal);
            this.type = 1;
        } else {
            varDecl.parse(symbolTable, isGlobal);
            this.type = 2;
        }
    }

    public void generate_global_LLVMIR() {
        if (this.type == 1) {
            this.constDecl.generate_global_LLVMIR();
        } else {
            this.varDecl.generate_global_LLVMIR();
        }
    }

    public void generate_local_LLVMIR(FuncIR funcIR) {
        if (this.type == 1) {
            this.constDecl.generate_local_LLVMIR(funcIR);
        } else {
            this.varDecl.generate_local_LLVMIR(funcIR);
        }
    }
}
