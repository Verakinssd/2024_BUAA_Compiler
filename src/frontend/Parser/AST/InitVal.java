package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;
import backend.LLVMIR.RegIR;

import java.util.ArrayList;

public class InitVal extends AstNode{
    public Integer type;
    public ArrayList<Exp> exps = new ArrayList<>();
    public String string = "";

    public void parse(SymbolTable symbolTable) {
        if (getNowTokenType() == TokenType.LBRACE) {
            append(1);
            if (isExp()) {
                Exp exp = new Exp();
                exp.parse(symbolTable);
                exps.add(exp);
                while (getNowTokenType() == TokenType.COMMA) {
                    append(1);
                    exp = new Exp();
                    exp.parse(symbolTable);
                    exps.add(exp);
                }
            }
            append(1);
            this.type = 2;
        } else if (getNowTokenType() == TokenType.STRCON) {
            string = getNowToken().getContent();
            append(1);
            this.type = 3;
        } else {
            Exp exp = new Exp();
            exp.parse(symbolTable);
            this.exps.add(exp);
            this.type = 1;
        }
        stringBuilder.append("<InitVal>").append("\n");
    }

    public Integer getValue() {
        return exps.get(0).getValue();
    }

    public ArrayList<Integer> getValues() {
        ArrayList<Integer> values = new ArrayList<>();
        for (Exp exp : exps) {
            values.add(exp.getValue());
        }
        return values;
    }

    public ArrayList<RegIR> getVariableValue(FuncIR funcIR) {
        ArrayList<RegIR> regIRs = new ArrayList<>();
        for (Exp exp : exps) {
            regIRs.add(exp.getVariableValue(funcIR));
        }
        return regIRs;
    }

    public String getString() {
        return string.substring(1, string.length() - 1);
    }
}
