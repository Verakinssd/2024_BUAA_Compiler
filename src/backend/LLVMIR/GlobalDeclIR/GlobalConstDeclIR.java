package backend.LLVMIR.GlobalDeclIR;

import backend.ASM.GlobalDeclAsm.GlobalConstDeclAsm;
import backend.LLVMIR.RegType;
import frontend.Lexer.TokenType;

import java.util.ArrayList;

import static backend.LLVMIR.FuncIR.InstructionIR.cg;
import static backend.LLVMIR.RegType.*;

public class GlobalConstDeclIR extends GlobalDeclIR {
    public GlobalConstDeclIR(RegType regType, String ident, Integer value) {
        super(regType, ident, value);
    }

    public GlobalConstDeclIR(RegType regType, String ident, Integer size , ArrayList<Integer> values) {
        super(regType, ident, size, values);
    }

    public GlobalConstDeclAsm getMipsCode() {
        if (regType == I32_PTR || regType == I8_PTR) {
            return new GlobalConstDeclAsm(regType, ident, value);
        } else {
            return new GlobalConstDeclAsm(regType, ident, size, values);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (regType == RegType.I32_PTR || regType == RegType.I8_PTR) {
            sb.append(cg(ident)).append(" = dso_local constant").append(" ").
                    append(getReference(regType)).append(" ").append(value).append("\n");
        } else if (regType == RegType.I32_PTR_PTR || regType == RegType.I8_PTR_PTR) {
            sb.append(cg(ident)).append(" = dso_local constant [").append(size).append(" x ").
            append(getReference(getReference(regType))).append("] [");
            for (int i = 0; i < size; i++) {
                sb.append(getReference(getReference(regType))).append(" ").append(values.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]\n");
        }
        return sb.toString();
    }
}
