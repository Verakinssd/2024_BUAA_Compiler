package frontend.Parser.AST;

import frontend.Symbol.SymbolTable;

public class ConstExp extends AstNode {
    public AddExp addExp = new AddExp();

    public void parse(SymbolTable symbolTable) {
        addExp.parse(symbolTable);
        stringBuilder.append("<ConstExp>").append("\n");
    }

    public Integer getValue() {
        return addExp.getValue();
    }
}
