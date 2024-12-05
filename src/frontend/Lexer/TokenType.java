package frontend.Lexer;

public enum TokenType {
    IDENFR , INTCON , STRCON , CHRCON , MAINTK , CONSTTK , INTTK ,
    CHARTK , BREAKTK , CONTINUETK , IFTK , ELSETK , NOT , AND , OR ,
    FORTK , GETINTTK , GETCHARTK , PRINTFTK , RETURNTK , PLUS , MINU ,
    VOIDTK , MULT , DIV , MOD , LSS , LEQ , GRE , GEQ , EQL , NEQ , ASSIGN ,
    SEMICN , COMMA , LPARENT , RPARENT , LBRACK , RBRACK , LBRACE , RBRACE , END , UNKNOWN;

    public static TokenType getWordType(String word) {
        return switch (word) {
            case "main" -> MAINTK;
            case "const" -> CONSTTK;
            case "int" -> INTTK;
            case "char" -> CHARTK;
            case "break" -> BREAKTK;
            case "continue" -> CONTINUETK;
            case "if" -> IFTK;
            case "else" -> ELSETK;
            case "for" -> FORTK;
            case "getint" -> GETINTTK;
            case "getchar" -> GETCHARTK;
            case "printf" -> PRINTFTK;
            case "return" -> RETURNTK;
            case "void" -> VOIDTK;
            default -> IDENFR;
        };
    }

    public static TokenType getArithmeticOperatorType(String c) {
        return switch (c) {
            case "+" -> PLUS;
            case "-" -> MINU;
            case "*" -> MULT;
            case "/" -> DIV;
            case "%" -> MOD;
            default -> NOT;
        };
    }

    public static TokenType getComparisonOperatorType(String operator) {
        return switch (operator) {
            case "<" -> LSS;
            case "<=" -> LEQ;
            case ">" -> GRE;
            case ">=" -> GEQ;
            case "==" -> EQL;
            case "!=" -> NEQ;
            default -> ASSIGN;
        };
    }

    public static TokenType getParenthesisType(String parenthesis) {
        return switch (parenthesis) {
            case "(" -> LPARENT;
            case ")" -> RPARENT;
            case "{" -> LBRACE;
            case "}" -> RBRACE;
            case "[" -> LBRACK;
            default -> RBRACK;
        };
    }

    public static TokenType getOtherCharacterType(String str) {
        return switch (str) {
            case ";" -> SEMICN;
            default -> COMMA;
        };
    }
}
