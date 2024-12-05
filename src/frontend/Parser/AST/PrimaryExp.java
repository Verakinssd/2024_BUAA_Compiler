package frontend.Parser.AST;
import backend.LLVMIR.FuncIR.FuncIR;
import backend.LLVMIR.RegIR;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

public class PrimaryExp extends AstNode{
    public boolean isArray;
    public TokenType arrayType;
    public Integer type;
    public Exp exp = new Exp();
    public LVal lval = new LVal();
    public Number number = new Number();
    public Character character = new Character();

    public void parse(SymbolTable symbolTable) {
        if (getNowTokenType() == TokenType.LPARENT) {
            append(1);
            exp.parse(symbolTable);
            isArray = exp.isArray;
            arrayType = exp.arrayType;
            j_error();
            this.type = 1;
        } else if (getNowTokenType() == TokenType.INTCON){
            number.parse();
            isArray = false;
            arrayType = null;
            this.type = 3;
        } else if (getNowTokenType() == TokenType.CHRCON) {
            character.parse();
            isArray = false;
            arrayType = null;
            this.type = 4;
        } else {
            lval.parse(symbolTable);
            isArray = lval.isArray;
            arrayType = lval.arrayType;
            this.type = 2;
        }
        if (print == 1) {
            stringBuilder.append("<PrimaryExp>").append("\n");
        }
    }

    public Integer getValue() {
        if (type == 1) {
            return exp.getValue();
        } else if (type == 2) {
            return lval.getValue();
        } else if (type == 3) {
            return number.number;
        } else {
            return (int) character.character;
        }
    }

    public RegIR getVariableValue(FuncIR funcIR) {
        if (type == 4) {
            return new RegIR(RegType.I8, (int) character.character);
        } else if (type == 3) {
            return new RegIR(RegType.I32, number.number);
        } else if (type == 2) {
            return lval.getVariableValue(funcIR, true);
        } else {
            return exp.getVariableValue(funcIR);
        }
    }
}
