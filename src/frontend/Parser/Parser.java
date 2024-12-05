package frontend.Parser;

import frontend.Parser.AST.AstNode;
import frontend.Parser.AST.CompUnit;
import frontend.Symbol.SymbolTable;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Parser {
    private final CompUnit compUnit;

    public Parser() {
        compUnit = new CompUnit();
    }

    public void parse(SymbolTable symbolTable) throws IOException {
        compUnit.parse(symbolTable);
    }

    public void generateLLVMIR() {
        compUnit.generateLLVMIR();
    }
}
