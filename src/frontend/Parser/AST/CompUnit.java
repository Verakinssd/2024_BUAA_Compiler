package frontend.Parser.AST;

import frontend.Lexer.Token;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolTable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CompUnit extends AstNode {
    public ArrayList<Decl> decls = new ArrayList<>();
    public ArrayList<FuncDef> funcDefs = new ArrayList<>();
    public MainFuncDef mainFuncDef = new MainFuncDef();

    public void parse(SymbolTable symbolTable) throws IOException {
        while (getPreTokenType(2) != TokenType.LPARENT) {
            Decl decl = new Decl();
            decl.parse(symbolTable,true);
            decls.add(decl);
        }
        while (getPreTokenType(1) != TokenType.MAINTK) {
            FuncDef funcDef = new FuncDef();
            funcDef.parse(symbolTable);
            funcDefs.add(funcDef);
        }
        mainFuncDef.parse(symbolTable);
        stringBuilder.append("<CompUnit>").append("\n");
        //Path path2 = Paths.get("parser.txt");
        //Files.writeString(path2, stringBuilder.toString());
    }

    public void generateLLVMIR() {
        for (Decl decl : decls) {
            decl.generate_global_LLVMIR();
        }
        for (FuncDef funcDef : funcDefs) {
            funcDef.generate_LLVMIR();
        }
        mainFuncDef.generate_LLVMIR();
    }
}
