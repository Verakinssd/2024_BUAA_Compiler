package backend.ASM.FuncAsm;

import backend.ASM.AsmOP;

public class SyscallAsm extends InstructionAsm {

    public SyscallAsm() {
    }

    public String toString() {
        return "    syscall\n";
    }
}
