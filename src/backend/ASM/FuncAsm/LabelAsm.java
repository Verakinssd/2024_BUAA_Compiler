package backend.ASM.FuncAsm;

public class LabelAsm extends InstructionAsm {
    public String labelName;

    public LabelAsm(String labelName) {
        this.labelName = labelName;
    }

    public String toString() {
        return labelName + ":\n";
    }
}
