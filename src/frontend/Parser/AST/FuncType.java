package frontend.Parser.AST;

import frontend.Lexer.TokenType;

public class FuncType extends AstNode{
    public TokenType funcType;

    public void parse() {
        funcType = getNowTokenType();
        append(1);
        stringBuilder.append("<FuncType>").append("\n");
    }
}
