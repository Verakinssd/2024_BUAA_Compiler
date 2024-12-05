package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.BasicBlockIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class Block extends AstNode{
    public ArrayList<BlockItem> blockItems = new ArrayList<>();
    public BasicBlockIR afterBB = new BasicBlockIR();
    public BasicBlockIR forStmt2BB = new BasicBlockIR();

    // isReturn :  0 -> void  1 -> ReturnFunc  2 - > ReturnBlock
    public void parse(SymbolTable symbolTable,Boolean flag, Integer isReturn, Boolean inFor) {
        //System.err.println(isReturn);
        append(1);
        SymbolTable symbolTable1 = null;
        if (flag) {
            symbolTable1 = new SymbolTable(symbolTable);
        }
        int returnRow = 0;
        while (getNowTokenType() != TokenType.RBRACE) {
            BlockItem blockItem = new BlockItem();
            if (flag) {
                blockItem.parse(symbolTable1, isReturn , inFor);
            } else {
                blockItem.parse(symbolTable , isReturn , inFor);
            }
            blockItems.add(blockItem);
            if (blockItem.returnRow != 0) {
                returnRow = blockItem.returnRow;
            }
        }
        if (isReturn == 1 && returnRow == 0) {
            g_error();
        }
        append(1);
        stringBuilder.append("<Block>").append("\n");
    }

    public void generate_LLVMIR(FuncIR funcIR) {
        for (BlockItem blockItem : blockItems) {
            blockItem.afterBB = afterBB;
            blockItem.forStmt2BB = forStmt2BB;
            blockItem.generate_LLVMIR(funcIR);
        }
    }
}
