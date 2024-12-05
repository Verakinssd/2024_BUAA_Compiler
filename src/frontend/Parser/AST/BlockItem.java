package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.BasicBlockIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

public class BlockItem extends AstNode{
    public Integer returnRow = 0;
    public Decl decl = new Decl();
    public Stmt stmt = new Stmt();
    public Integer type;
    public BasicBlockIR afterBB = new BasicBlockIR();
    public BasicBlockIR forStmt2BB = new BasicBlockIR();

    public void parse(SymbolTable symbolTable, Integer isReturn , Boolean inFor) {
        if (getNowTokenType() == TokenType.CONSTTK ||
        getNowTokenType() == TokenType.INTTK ||
        getNowTokenType() == TokenType.CHARTK) {
            decl.parse(symbolTable,false);
            type = 1;
        } else {
            stmt.parse(symbolTable, isReturn, inFor);
            returnRow = stmt.returnRow;
            type = 2;
        }
    }

    public void generate_LLVMIR(FuncIR funcIR) {
        if (type == 1) {
            decl.generate_local_LLVMIR(funcIR);
        } else {
            stmt.afterBB = afterBB;
            stmt.forStmt2BB = forStmt2BB;
            stmt.generate_LLVMIR(funcIR);
        }
    }

}
