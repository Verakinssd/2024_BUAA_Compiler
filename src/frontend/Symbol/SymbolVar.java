package frontend.Symbol;

import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import java.util.ArrayList;

public class SymbolVar extends Symbol{
    public TokenType bType;

    public SymbolVar(Integer id, Integer row, String content, Boolean isGlobal,Boolean isArray, TokenType bType) {
        super(id, row, content, isGlobal);
        this.isArray = isArray;
        this.bType = bType;
    }

    public String toString() {
        if (isArray) {
            if (bType == TokenType.INTTK) {
                return id + " " + content + " IntArray";
            } else {
                return id + " " + content + " CharArray";
            }
        } else {
            if (bType == TokenType.INTTK) {
                return id + " " + content + " Int";
            } else {
                return id + " " + content + " Char";
            }
        }
    }

    public Integer getValue() {
        return value;
    }

    public Integer getValue(int index) {
        return values.get(index);
    }

    public void setValue(Integer value, String reg, RegType regType) {
        this.value = value;
        this.reg = reg;
        this.regType = regType;
    }

    public void setValue(String reg, RegType regType) {
        this.reg = reg;
        this.regType = regType;
    }

    public void setValue(String reg, RegType regType, Integer arraySize) {
        this.reg = reg;
        this.regType = regType;
        this.arraySize = arraySize;
    }

    public void setValue(ArrayList<Integer> values, String reg, RegType regType,Integer arraySize) {
        this.values = values;
        this.reg = reg;
        this.regType = regType;
        this.arraySize = arraySize;
    }
}
