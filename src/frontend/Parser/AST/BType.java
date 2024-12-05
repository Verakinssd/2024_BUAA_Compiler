package frontend.Parser.AST;

import frontend.Lexer.TokenType;

public class BType extends AstNode {
    public TokenType type;

    public void parse() {
        type = getNowTokenType();
        append(1);
    }

    public TokenType getType() {
        return type;
    }
}
