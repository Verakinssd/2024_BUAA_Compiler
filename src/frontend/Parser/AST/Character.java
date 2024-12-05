package frontend.Parser.AST;

public class Character extends AstNode {
    public char character;

    public void parse() {
        if (this.getNowToken().getContent().charAt(1) == '\\') {
            if (this.getNowToken().getContent().charAt(2) == '\\') {
                character = '\\';
            } else if (this.getNowToken().getContent().charAt(2) == '0') {
                character = '\0';
            } else if (this.getNowToken().getContent().charAt(2) == '\'') {
                character = '\'';
            } else if (this.getNowToken().getContent().charAt(2) == '\"') {
                character = '\"';
            } else if (this.getNowToken().getContent().charAt(2) == 'f') {
                character = '\f';
            } else if (this.getNowToken().getContent().charAt(2) == 'v') {
                character = 11;
            } else if (this.getNowToken().getContent().charAt(2) == 'n') {
                character = '\n';
            } else if (this.getNowToken().getContent().charAt(2) == 't') {
                character = '\t';
            } else if (this.getNowToken().getContent().charAt(2) == 'b') {
                character = '\b';
            } else if (this.getNowToken().getContent().charAt(2) == 'a') {
                character = 7;
            }
        } else {
            character = this.getNowToken().getContent().charAt(1);
        }
//        System.out.println("Character: " + character);
        append(1);
        if (print == 1) {
            stringBuilder.append("<Character>").append("\n");
        }
    }
}
