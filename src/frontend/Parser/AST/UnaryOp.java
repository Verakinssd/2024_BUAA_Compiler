package frontend.Parser.AST;

public class UnaryOp extends AstNode {
    String op = "";

    public void parse() {
        op = this.getNowToken().getContent();
        append(1);
        if (print == 1) {
            stringBuilder.append("<UnaryOp>").append("\n");
        }
    }
}
