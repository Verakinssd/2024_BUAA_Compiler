package backend.ASM.FuncAsm;

public class CommentAsm extends InstructionAsm {
    public String comment;

    public CommentAsm(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return "#" + comment.substring(3);
    }
}
