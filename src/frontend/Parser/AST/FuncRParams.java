package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class FuncRParams extends AstNode{
    public Integer num = 0;
    public ArrayList<Exp> exps = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        Exp exp = new Exp();
        exp.parse(symbolTable);
        exps.add(exp);
        num ++;
        while (getNowTokenType() == TokenType.COMMA) {
            append(1);
            exp = new Exp();
            exp.parse(symbolTable);
            exps.add(exp);
            num ++;
        }
        if (print == 1) {
            stringBuilder.append("<FuncRParams>").append("\n");
        }
    }

    public ArrayList<RegIR> getVariableValue(FuncIR funcIR) {
        ArrayList<RegIR> regIRs = new ArrayList<>();
        for (Exp exp : exps) {
            regIRs.add(exp.getVariableValue(funcIR));
        }
        return regIRs;
    }
}