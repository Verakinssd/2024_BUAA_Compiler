package frontend.Symbol;

import frontend.Parser.AST.AstNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.nio.file.Path;

public class SymbolTable {
    public static Integer symbolTableId = 0;
    public Integer id;
    public SymbolTable father;
    public HashMap<String, Symbol> symbols = new HashMap<>();
    public static ArrayList<Symbol> allSymbols = new ArrayList<>();

    public SymbolTable() {
        symbolTableId ++;
        this.id = symbolTableId;
        this.father = null;
    }

    public SymbolTable(SymbolTable symbolTable) {
        symbolTableId ++;
        this.id = symbolTableId;
        this.father = symbolTable;
    }

    public void addSymbol(Symbol symbol) {
        //System.out.println(symbol.toString());
        symbols.put(symbol.getContent(), symbol);
        allSymbols.add(symbol);
    }

    public boolean contain(String content) {
        return symbols.containsKey(content);
    }

    public Symbol getSymbol(String content) {
        if (symbols.containsKey(content)) {
            return symbols.get(content);
        } else if (father != null){
            return father.getSymbol(content);
        } else {
            return null;
        }
    }

    public static void print() throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        Collections.sort(allSymbols);
        for (Symbol symbol : allSymbols) {
            stringBuilder.append(symbol.toString()).append("\n");
        }
        Path path = Paths.get("symbol.txt");
        Files.writeString(path, stringBuilder.toString());
    }
}
