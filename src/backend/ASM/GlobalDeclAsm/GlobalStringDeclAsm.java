package backend.ASM.GlobalDeclAsm;

public class GlobalStringDeclAsm extends GlobalDeclAsm {
    public String str;
    public Integer arraySize;
    public String reg;

    public GlobalStringDeclAsm(String reg, String str, Integer arraySize) {
        this.reg = reg;
        this.str = str;
        this.arraySize = arraySize;
    }

    public String getMipsString(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i < str.length();i++) {
            if (str.charAt(i) == '\\' && i + 2 < str.length()) {
                sb.append("\\");
                if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == '0') {
                    sb.append("0");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == '7') {
                    sb.append("a");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == '8') {
                    sb.append("b");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == '9') {
                    sb.append("t");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == 'A') {
                    sb.append("n");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == 'B') {
                    sb.append("v");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == 'C') {
                    sb.append("f");
                    i += 2;
                } else if (str.charAt(i + 1) == '0' && str.charAt(i + 2) == 'D') {
                    sb.append("r");
                    i += 2;
                } else if (str.charAt(i + 1) == '2' && str.charAt(i + 2) == '2') {
                    sb.append("\"");
                    i += 2;
                } else if (str.charAt(i + 1) == '2' && str.charAt(i + 2) == '7') {
                    sb.append("'");
                    i += 2;
                } else if (str.charAt(i + 1) == '5' && str.charAt(i + 2) == 'C') {
                    sb.append("\\");
                    i += 2;
                }
            } else {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    ").append(reg);
        sb.append(":  .asciiz \"");
        sb.append(getMipsString(str));
        sb.append("\"\n");
        return sb.toString();
    }
}
