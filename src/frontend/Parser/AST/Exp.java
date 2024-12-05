package frontend.Parser.AST;


import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

public class Exp extends AstNode{
    public boolean isArray;
    public TokenType arrayType;
    public AddExp addExp = new AddExp();

    public void parse(SymbolTable symbolTable) {
        addExp.parse(symbolTable);
        isArray = addExp.isArray;
        arrayType = addExp.arrayType;
        if (print == 1) {
            stringBuilder.append("<Exp>").append("\n");
        }
    }

    public Integer getValue() {
        return addExp.getValue();
    }

    public RegIR getVariableValue(FuncIR funcIR) {
        return addExp.getVariableValue(funcIR);
    }
}
