package frontend.Parser.AST;

import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class ConstInitVal extends AstNode {
    public Integer type;
    public String stringConst = "";
    public ArrayList<ConstExp> constExps = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        if (getNowTokenType() == TokenType.STRCON) {
            this.stringConst = getNowToken().getContent();
            append(1);
            this.type = 3;
        } else if (getNowTokenType() == TokenType.LBRACE) {
            append(1);
            if (getNowTokenType() != TokenType.RBRACE) {
                ConstExp constExp = new ConstExp();
                constExp.parse(symbolTable);
                constExps.add(constExp);
                while (getNowTokenType() == TokenType.COMMA) {
                    append(1);
                    constExp = new ConstExp();
                    constExp.parse(symbolTable);
                    constExps.add(constExp);
                }
            }
            append(1);
            this.type = 2;
        } else {
            ConstExp constExp = new ConstExp();
            constExp.parse(symbolTable);
            constExps.add(constExp);
            this.type = 1;
        }
        stringBuilder.append("<ConstInitVal>").append("\n");
    }

    public Integer getValue() {
        return constExps.get(0).getValue();
    }

    public ArrayList<Integer> getValues() {
        ArrayList<Integer> values = new ArrayList<>();
        for (ConstExp constExp : constExps) {
            values.add(constExp.getValue());
        }
        return values;
    }



    public String getString() {
        return stringConst.substring(1, stringConst.length() - 1);
    }
}
