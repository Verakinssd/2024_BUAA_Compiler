package frontend.Symbol;

import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import java.util.ArrayList;

public class SymbolConst extends Symbol{
    public TokenType bType;

    public SymbolConst(Integer id, Integer row, String content,Boolean isGlobal
            , Boolean isArray,TokenType bType) {
        super(id, row, content,isGlobal);
        this.isArray = isArray;
        this.bType = bType;
    }

    public String toString() {
        if (isArray) {
            if (bType == TokenType.INTTK) {
                return id + " " + content + " ConstIntArray";
            } else {
                return id + " " + content + " ConstCharArray";
            }
        } else {
            if (bType == TokenType.INTTK) {
                return id + " " + content + " ConstInt";
            } else {
                return id + " " + content + " ConstChar";
            }
        }
    }

    public void setValue(Integer value, String reg, RegType regType) {
        this.value = value;
        this.reg = reg;
        this.regType = regType;
    }

    public void setValue(ArrayList<Integer> values,String reg,RegType regType,Integer arraySize) {
        this.values = values;
        this.reg = reg;
        this.regType = regType;
        this.arraySize = arraySize;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getValue(int index) {
        return values.get(index);
    }
}
