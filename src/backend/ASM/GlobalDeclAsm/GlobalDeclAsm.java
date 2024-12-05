package backend.ASM.GlobalDeclAsm;

import backend.LLVMIR.RegType;

import java.util.ArrayList;

public class GlobalDeclAsm {
    public RegType regType;
    public String ident;
    public Integer value;
    public Integer size;
    public ArrayList<Integer> values;

    public GlobalDeclAsm(RegType regType, String ident, Integer value) {
        this.regType = regType;
        this.ident = ident;
        this.value = value;
    }

    public GlobalDeclAsm(RegType regType, String ident, Integer size , ArrayList<Integer> values) {
        this.regType = regType;
        this.ident = ident;
        this.size = size;
        this.values = values;
    }

    public GlobalDeclAsm() {
    }
}
