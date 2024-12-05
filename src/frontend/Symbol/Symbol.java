package frontend.Symbol;

import backend.LLVMIR.RegType;

import java.util.ArrayList;

public class Symbol implements Comparable<Symbol> {
    public Integer id;
    public Integer row;
    public String content;
    public String reg;
    public RegType regType;
    public Integer value;
    public ArrayList<Integer> values;
    public Integer arraySize;
    public Boolean isArray;
    public Boolean isGlobal;

    public Symbol(Integer id, Integer row, String content, Boolean isGlobal) {
        this.id = id;
        this.row = row;
        this.content = content;
        this.isGlobal = isGlobal;
    }

    public int compareTo(Symbol o) {
        return this.id.compareTo(o.id);
    }

    public String getContent() {
        return content;
    }
}