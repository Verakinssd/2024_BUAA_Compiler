package backend.LLVMIR.GlobalDeclIR;

import backend.ASM.GlobalDeclAsm.GlobalVarDeclAsm;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import java.util.ArrayList;

import static backend.LLVMIR.FuncIR.InstructionIR.cg;
import static backend.LLVMIR.RegType.*;

public class GlobalVarDeclIR extends GlobalDeclIR {

    public boolean isInit;

    public GlobalVarDeclIR(RegType regType, String ident, Integer value) {
        super(regType, ident, value);
    }

    public GlobalVarDeclIR(RegType regType, String ident, Integer size, ArrayList<Integer> values, Boolean isInit) {
        super(regType, ident, size, values);
        this.isInit = isInit;
    }

    public GlobalVarDeclAsm getMipsCode() {
        if (regType == I32_PTR || regType == I8_PTR) {
            return new GlobalVarDeclAsm(regType, ident, value);
        } else {
            return new GlobalVarDeclAsm(regType, ident, size, values, isInit);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (regType == RegType.I32_PTR || regType == RegType.I8_PTR) {
            sb.append(cg(ident)).append(" = dso_local global").append(" ").append(getReference(regType))
                    .append(" ").append(value).append("\n");
        } else if (regType == RegType.I32_PTR_PTR || regType == RegType.I8_PTR_PTR) {
            sb.append(cg(ident)).append(" = dso_local global [").append(size).append(" x ").
            append(getReference(getReference(regType))).append("] ");
            if (isInit) {
                sb.append("[");
                for (int i = 0; i < size; i++) {
                    sb.append(getReference(getReference(regType))).append(" ").append(values.get(i));
                    if (i < size - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("]\n");
            }else {
                sb.append("zeroinitializer\n");
            }
        }
        return sb.toString();
    }
}