package backend.ASM.GlobalDeclAsm;

import backend.LLVMIR.RegType;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.*;

public class GlobalConstDeclAsm extends GlobalDeclAsm{
    public GlobalConstDeclAsm(RegType regType, String ident, Integer value) {
        super(regType, ident, value);
    }

    public GlobalConstDeclAsm(RegType regType, String ident, Integer size , ArrayList<Integer> values) {
        super(regType, ident, size, values);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ");
        if (regType == I32_PTR) {
            sb.append(ident.substring(1)).append(":  .word ").append(value);
        } else if (regType == I8_PTR) {
            sb.append(ident.substring(1)).append(":  .byte ").append(value);
        } else if (regType == I32_PTR_PTR) {
            sb.append(ident.substring(1)).append(":  .word ");
            for (int i = 0;i < size;i ++) {
                sb.append(values.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
        } else if (regType == I8_PTR_PTR) {
            sb.append(ident.substring(1)).append(":  .byte ");
            for (int i = 0;i < size;i ++) {
                sb.append(values.get(i));
                if (i < size - 1) {
                    sb.append(", ");
                }
            }
        }
        sb.append("\n");
        return sb.toString();
    }
}
