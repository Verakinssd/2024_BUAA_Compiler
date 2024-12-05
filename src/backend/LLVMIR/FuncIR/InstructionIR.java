package backend.LLVMIR.FuncIR;

public class InstructionIR {
    public static String cg(String reg) {
        if (reg != null)
            return reg.charAt(0) + "v" + reg.substring(1);
        else return null;
    }

    public void generateMipsCode(FuncIR funcIR) {
    }
}
