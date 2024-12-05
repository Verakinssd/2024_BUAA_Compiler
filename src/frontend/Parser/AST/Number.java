package frontend.Parser.AST;

public class Number extends AstNode {
    public Integer number;

    public void parse() {
        number = Integer.valueOf(getNowToken().getContent());
        append(1);
        if (print == 1) {
            stringBuilder.append("<Number>").append("\n");
        }
    }
}
