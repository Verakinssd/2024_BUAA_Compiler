package backend.LLVMIR.FuncIR;

public class PutStrIR extends InstructionIR {
    public Integer arraySize;
    public String reg;

    public PutStrIR(Integer arraySize, String reg) {
        this.arraySize = arraySize;
        this.reg = reg;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("    call void @putstr(i8* getelementptr inbounds ([").append(arraySize)
                .append(" x i8], [").append(arraySize).append(" x i8]* ").append(cg(reg)).append(", i64 0, i64 0))\n");
        return sb.toString();
    }
}
