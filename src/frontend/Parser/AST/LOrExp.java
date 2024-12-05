package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.BasicBlockIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class LOrExp extends AstNode{
    public ArrayList<LAndExp> lAndExps = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        LAndExp lAndExp = new LAndExp();
        lAndExp.parse(symbolTable);
        lAndExps.add(lAndExp);
        stringBuilder.append("<LOrExp>").append("\n");
        while (getNowTokenType() == TokenType.OR) {
            append(1);
            lAndExp = new LAndExp();
            lAndExp.parse(symbolTable);
            lAndExps.add(lAndExp);
            stringBuilder.append("<LOrExp>").append("\n");
        }
    }

    public void generate_LLVMIR(FuncIR funcIR , BasicBlockIR trueBB, BasicBlockIR falseBB) {
        for (int i = 0;i < lAndExps.size() - 1;i ++) {
            BasicBlockIR nextBB = new BasicBlockIR();
            lAndExps.get(i).generate_LLVMIR(funcIR, trueBB, nextBB);
            nextBB.name = "%" + FuncIR.reg ++;
            funcIR.addInstructionIR(nextBB);
        }
        lAndExps.get(lAndExps.size() - 1).generate_LLVMIR(funcIR, trueBB, falseBB);
    }
}
