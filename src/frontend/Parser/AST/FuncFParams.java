package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.FuncFParamIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

public class FuncFParams extends AstNode{
    public Integer num = 0;
    public ArrayList<FuncFParam> funcFParams = new ArrayList<>();

    public void parse(SymbolTable symbolTable) {
        FuncFParam funcFParam = new FuncFParam();
        funcFParam.parse(symbolTable);
        funcFParams.add(funcFParam);
        num ++;
        while (getNowTokenType() == TokenType.COMMA) {
            append(1);
            funcFParam = new FuncFParam();
            funcFParam.parse(symbolTable);
            funcFParams.add(funcFParam);
            num ++;
        }
        stringBuilder.append("<FuncFParams>").append("\n");
    }

    public ArrayList<FuncFParamIR> generate_LLVMIR() {
        ArrayList<FuncFParamIR> funcFParamsIR = new ArrayList<>();
        for (FuncFParam funcFParam : funcFParams) {
            funcFParamsIR.add(funcFParam.generate_LLVMIR());
        }
        return funcFParamsIR;
    }
}
