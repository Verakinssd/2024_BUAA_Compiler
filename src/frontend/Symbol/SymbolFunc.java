package frontend.Symbol;

import frontend.Lexer.TokenType;
import frontend.Parser.AST.FuncFParam;
import frontend.Parser.AST.FuncFParams;

import java.util.ArrayList;

public class SymbolFunc extends Symbol{
    public TokenType tokenType;
    public Integer num;
    public FuncFParams funcFParams;

    public SymbolFunc(Integer id, Integer row, String content, TokenType funcType,Integer num, FuncFParams funcFParams) {
        super(id, row, content, false);
        this.tokenType = funcType;
        this.num = num;
        this.funcFParams = funcFParams;
    }

    public void setReg(String reg) {
        this.reg = reg;
    }

    public String toString() {
        if (tokenType == TokenType.VOIDTK) {
            return id + " " + content + " VoidFunc";
        } else if (tokenType == TokenType.CHARTK) {
            return id + " " + content + " CharFunc";
        } else {
            return id + " " + content + " IntFunc";
        }
    }
}
