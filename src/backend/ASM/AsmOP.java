package backend.ASM;

public enum AsmOP {
    MULT, DIV, ADD, SUB, ADDU, SUBU, ADDI, AND, OR, SRL , SLL, MFHI ,MFLO,
    SLT, SLE, SGT, SGE, SEQ, SNE, LW, SW, SYSCALL, LI , LA, LABEL, J, JAL, JR, BEQ;

    public String toString() {
        return String.format("%-4s", this.name().toLowerCase());
    }
}
