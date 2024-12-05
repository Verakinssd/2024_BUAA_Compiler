import backend.ASM.GlobalDeclAsm.GlobalDeclAsm;
import backend.LLVMIR.GlobalDeclIR.GlobalDeclIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Error.ErrorLog;
import frontend.Lexer.Lexer;
import frontend.Lexer.Token;
import frontend.Lexer.TokenType;
import frontend.Parser.AST.AstNode;
import frontend.Parser.Parser;
import frontend.Symbol.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Compiler {
    private static final StringBuilder llvm_ir = new StringBuilder();
    private static final StringBuilder mips = new StringBuilder();
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("testfile.txt");
        String str = Files.readString(path);
        Lexer lexer = new Lexer(str);
        ArrayList<Token> tokens = new ArrayList<>();
        Token token = lexer.getCurrentToken();
        do {
            tokens.add(token);
            lexer.next();
            token = lexer.getCurrentToken();
        } while(token.getType() != TokenType.END && token.getType() != TokenType.UNKNOWN);
        AstNode.print = 1;
        AstNode.tokens = tokens;

        SymbolTable symbolTable = new SymbolTable();
        Parser parser = new Parser();
        parser.parse(symbolTable);
        SymbolTable.print();
        ErrorLog.getInstance().printErrors();

        parser.generateLLVMIR();
        llvm_ir_pre();
        mips.append(".data\n");
        for (GlobalDeclIR globalDeclIR : AstNode.globalDeclIRS) {
            llvm_ir.append(globalDeclIR.toString());
            mips.append(globalDeclIR.getMipsCode());
        }
        mips.append("\n.text\n");
        mips.append(".globl main\n\n");
        llvm_ir.append("\n");
        for (FuncIR funcIR : AstNode.funcIRs) {
            llvm_ir.append(funcIR.toString()).append("\n");
            mips.append(funcIR.getMipsCode());
        }

        Path llvm_ir_path = Paths.get("llvm_ir.txt");
        Files.writeString(llvm_ir_path, llvm_ir.toString());

        Path outputPath = Paths.get("mips.txt");
        Files.writeString(outputPath, mips.toString());
    }

    private static void llvm_ir_pre() {
        llvm_ir.append("declare i32 @getint()\n");
        llvm_ir.append("declare i32 @getchar()\n");
        llvm_ir.append("declare void @putint(i32)\n");
        llvm_ir.append("declare void @putch(i32)\n");
        llvm_ir.append("declare void @putstr(i8*)\n\n");
    }
}