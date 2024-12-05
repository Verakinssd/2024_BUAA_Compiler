package backend.ASM.GlobalDeclAsm;

import backend.LLVMIR.RegType;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.*;

public class GlobalVarDeclAsm extends GlobalDeclAsm {
    public boolean isInit;

    public GlobalVarDeclAsm(RegType regType, String ident, Integer value) {
        super(regType, ident, value);
    }

    public GlobalVarDeclAsm(RegType regType, String ident, Integer size, ArrayList<Integer> values, Boolean isInit) {
        super(regType, ident, size, values);
        this.isInit = isInit;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        if (regType == I32_PTR) {
            sb.append(ident).append(":  .word ").append(value);
        } else if (regType == I8_PTR) {
            sb.append(ident).append(":  .byte ").append(value);
        } else if (regType == I32_PTR_PTR) {
            sb.append(ident).append(":  .word ");
            if (isInit) {
                for (int i = 0;i < size;i ++) {
                    sb.append(values.get(i));
                    if (i < size - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("\n");
            } else {
                sb.append("0:").append(size);
            }
        } else if (regType == I8_PTR_PTR) {
            sb.append(ident).append(":  .byte ");
            if (isInit) {
                for (int i = 0;i < size;i ++) {
                    sb.append(values.get(i));
                    if (i < size - 1) {
                        sb.append(", ");
                    }
                }
                sb.append("\n");
            } else {
                sb.append("0:").append(size);
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
