package frontend.Lexer;

import frontend.Error.Error;
import frontend.Error.ErrorLog;

public class Lexer {
    private final String input;
    private Integer pos = 0;
    private Integer row = 1;
    private Token currentToken = null;

    public Lexer(String input) {
        this.input = input;
        next();
    }

    public void move() {
        pos++;
        if (!end() && input.charAt(pos) == '\n') {
            row++;
        }
    }

    public boolean end() {
        return pos >= input.length();
    }

    public void skipComments() {
        char c = input.charAt(pos);
        if (c == '/' && pos + 1 < input.length()) {
            char c1 = input.charAt(pos + 1);
            if (c1 == '/') {
                move();
                do {
                    move();
                } while (!end() && input.charAt(pos) != '\n');
            } else if (c1 == '*') {
                move();
                do {
                    move();
                } while (!end() && (input.charAt(pos) != '*' || input.charAt(pos + 1) != '/'));
                move();
                move();
            }
        }
    }

    public Token getDigit() {
        StringBuilder stringBuilder = new StringBuilder();
        int row1 = row;
        do {
            stringBuilder.append(input.charAt(pos));
            move();
        } while (!end() && Character.isDigit(input.charAt(pos)));
        return new Token(TokenType.INTCON, stringBuilder.toString(), row1);
    }

    public Token getLetter() {
        StringBuilder stringBuilder = new StringBuilder();
        int row1 = row;
        do {
            stringBuilder.append(input.charAt(pos));
            move();
        } while (!end() && (Character.isLetter(input.charAt(pos))
                            || Character.isDigit(input.charAt(pos))
                            || input.charAt(pos) == '_'));
        String word = stringBuilder.toString();
        return new Token(TokenType.getWordType(word), word, row1);
    }

    public Token getStringConst() {
        StringBuilder stringBuilder = new StringBuilder();
        int row1 = row;
        do {
            stringBuilder.append(input.charAt(pos));
            move();
        } while (!end() && input.charAt(pos) != '\"');
        if (!end() && input.charAt(pos) == '\"') {
            stringBuilder.append('\"');
            move();
        }
        String stringConst = stringBuilder.toString();
        return new Token(TokenType.STRCON, stringConst, row1);
    }

    public Token getCharConst() {
        StringBuilder stringBuilder = new StringBuilder();
        int row1 = row;
        do {
            stringBuilder.append(input.charAt(pos));
            move();
            if (pos + 1 < input.length() && input.charAt(pos) == '\\') {
                stringBuilder.append('\\');
                move();
                stringBuilder.append(input.charAt(pos));
                move();
            }
        } while (!end() && input.charAt(pos) != '\'');
        if (!end() && input.charAt(pos) == '\'') {
            stringBuilder.append('\'');
            move();
        }
        String charConst = stringBuilder.toString();
        return new Token(TokenType.CHRCON, charConst , row1);
    }

    public Token getLogicalOperator() {
        StringBuilder stringBuilder = new StringBuilder();
        char c = input.charAt(pos);
        int row1 = row;
        stringBuilder.append(c);
        move();
        if (end() || input.charAt(pos) != c) {
            ErrorLog.getInstance().addError(new Error(row,"a"));
        } else {
            stringBuilder.append(c);
        }
        if (!end()) {
            move();
        }
        if (c == '&') {
            return new Token(TokenType.AND, stringBuilder.toString(), row1);
        } else {
            return new Token(TokenType.OR, stringBuilder.toString(), row1);
        }
    }

    public Token getArithmeticOperator(char c){
        String arithmeticOperator = String.valueOf(c);
        int row1 = row;
        move();
        return new Token(TokenType.getArithmeticOperatorType(arithmeticOperator), arithmeticOperator , row1);
    }

    public Token getComparisonOperator() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(input.charAt(pos));
        int row1 = row;
        move();
        if (!end() && input.charAt(pos) == '=') {
            stringBuilder.append('=');
            move();
        }
        String operator = stringBuilder.toString();
        return new Token(TokenType.getComparisonOperatorType(operator), operator , row1);
    }

    public Token getParenthesis() {
        String parenthesis = String.valueOf(input.charAt(pos));
        int row1 = row;
        move();
        return new Token(TokenType.getParenthesisType(parenthesis),parenthesis , row1);
    }

    public Token getOtherCharacter() {
        String str = String.valueOf(input.charAt(pos));
        int row1 = row;
        move();
        return new Token(TokenType.getOtherCharacterType(str), str , row1);
    }

    public boolean isArithmeticOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/'
                || c == '%' || (c == '!' && (pos + 1 >= input.length() || input.charAt(pos + 1) != '='));
    }

    public boolean isComparisonOperator(char c) {
        return c == '>' || c == '<' || c == '=' || c == '!';
    }

    public boolean isParenthesis(char c) {
        return c == '(' || c == ')' || c == '{' || c == '}' || c == '[' || c == ']';
    }

    public boolean isOtherCharacter(char c) {
        return c == ';' || c == ',';
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public void next() {
        if (end()) {
            currentToken = new Token(TokenType.END,"END", row);
            return;
        }
        skipComments();
        if (end()) {
            currentToken = new Token(TokenType.END,"END" , row);
            return;
        }
        char c = input.charAt(pos);
        //System.err.println("c = " + c + " row = " + row);
        if (Character.isDigit(c)) {
            currentToken = getDigit();
        } else if (Character.isLetter(c) || c == '_') {
            currentToken = getLetter();
        } else if (c == '\"') {
            currentToken = getStringConst();
        } else if (c == '\'') {
            currentToken = getCharConst();
        } else if (c == '&' || c == '|') {
            currentToken = getLogicalOperator();
        } else if (isArithmeticOperator(c)){
            currentToken = getArithmeticOperator(c);
        } else if (isComparisonOperator(c)) {
            currentToken = getComparisonOperator();
        } else if (isParenthesis(c)) {
            currentToken = getParenthesis();
        } else if (isOtherCharacter(c)) {
            currentToken = getOtherCharacter();
        } else if (Character.isWhitespace(c)){
            move();
            next();
        } else {
            currentToken = new Token(TokenType.UNKNOWN, String.valueOf(c) , row);
        }
    }
}
