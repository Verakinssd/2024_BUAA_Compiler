package backend.LLVMIR.GlobalDeclIR;

import backend.ASM.GlobalDeclAsm.GlobalDeclAsm;
import backend.LLVMIR.RegType;

import java.util.ArrayList;

public class GlobalDeclIR {
    public RegType regType;
    public String ident;
    public Integer value;
    public Integer size;
    public ArrayList<Integer> values;

    public GlobalDeclIR(RegType regType, String ident, Integer value) {
        this.regType = regType;
        this.ident = ident;
        this.value = value;
    }

    public GlobalDeclIR(RegType regType, String ident, Integer size , ArrayList<Integer> values) {
        this.regType = regType;
        this.ident = ident;
        this.size = size;
        this.values = values;
    }

    public GlobalDeclIR() {
    }

    public GlobalDeclAsm getMipsCode() {
        return null;
    }
}
