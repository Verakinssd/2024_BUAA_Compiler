package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.BasicBlockIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class LAndExp extends AstNode{
    public ArrayList<EqExp> eqExps = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        EqExp eqExp = new EqExp();
        eqExp.parse(symbolTable);
        eqExps.add(eqExp);
        stringBuilder.append("<LAndExp>").append("\n");
        while (getNowTokenType() == TokenType.AND) {
            append(1);
            eqExp = new EqExp();
            eqExp.parse(symbolTable);
            eqExps.add(eqExp);
            stringBuilder.append("<LAndExp>").append("\n");
        }
    }

    public void generate_LLVMIR(FuncIR funcIR, BasicBlockIR trueBB, BasicBlockIR falseBB) {
        for (int i = 0;i < eqExps.size() - 1;i ++) {
            BasicBlockIR nextBB = new BasicBlockIR();
            eqExps.get(i).generate_LLVMIR(funcIR, nextBB, falseBB);
            nextBB.name = "%" + FuncIR.reg ++;
            funcIR.addInstructionIR(nextBB);
        }
        eqExps.get(eqExps.size() - 1).generate_LLVMIR(funcIR, trueBB, falseBB);
    }
}
