package frontend.Lexer;

public class Token {

    private final String content;
    private final TokenType type;
    private final Integer row;

    public Token(TokenType type, String content, Integer row) {
        this.type = type;
        this.content = content;
        this.row = row;
    }

    public String getContent() {
        return content;
    }

    public String toString() {
        return type + " " + content;
    }

    public TokenType getType() {
        return type;
    }

    public Integer getRow() {
        return row;
    }
}
