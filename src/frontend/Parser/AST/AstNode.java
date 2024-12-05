package frontend.Parser.AST;

import backend.LLVMIR.GlobalDeclIR.GlobalDeclIR;
import backend.LLVMIR.FuncIR.FuncIR;
import frontend.Error.Error;
import frontend.Error.ErrorLog;
import frontend.Lexer.Token;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolConst;

import java.util.ArrayList;

public class AstNode {
    public static ArrayList<Token> tokens;
    public static StringBuilder stringBuilder = new StringBuilder();
    public static Integer pos = 0;
    public static Integer print;
    public static final int ERROR_VALUE = 22373468;
    public static ArrayList<GlobalDeclIR> globalDeclIRS = new ArrayList<>();
    public static ArrayList<FuncIR> funcIRs = new ArrayList<>();

    public TokenType getNowTokenType() {
        return tokens.get(pos).getType();
    }

    public Token getToken(Integer num) {
        return tokens.get(pos + num);
    }

    public Token getNowToken() {
        return tokens.get(pos);
    }

    public void next() {
        pos++;
    }

    public void append(int num) {
        for (int i = 1;i <= num;i++) {
            if (print == 1) {
                stringBuilder.append(getNowToken()).append("\n");
            }
            next();
        }
    }

    public TokenType getPreTokenType(int num) {
        return tokens.get(pos + num).getType();
    }

    public boolean isExp() {
        return getNowTokenType() == TokenType.LPARENT ||
                getNowTokenType() == TokenType.IDENFR ||
                getNowTokenType() == TokenType.INTCON ||
                getNowTokenType() == TokenType.CHRCON ||
                getNowTokenType() == TokenType.NOT ||
                getNowTokenType() == TokenType.PLUS ||
                getNowTokenType() == TokenType.MINU;
    }

    public void i_error() {
        if (getNowTokenType() != TokenType.SEMICN) {
            if (print == 1) {
                stringBuilder.append(new Token(TokenType.SEMICN, ";" , getToken(-1).getRow())).append("\n");
                ErrorLog.getInstance().addError(new Error(getToken(-1).getRow(), "i"));
            }
        } else {
            if (print == 1) {
                stringBuilder.append(getNowToken()).append("\n");
            }
            next();
        }
    }

    public void k_error() {
        if (getNowTokenType() != TokenType.RBRACK) {
            if (print == 1) {
                stringBuilder.append(new Token(TokenType.RBRACK, "]" , getToken(-1).getRow())).append("\n");
                ErrorLog.getInstance().addError(new Error(getToken(-1).getRow(), "k"));
            }
        } else {
            if (print == 1) {
                stringBuilder.append(getNowToken()).append("\n");
            }
            next();
        }
    }

    public void j_error() {
        if (getNowTokenType() != TokenType.RPARENT) {
            if (print == 1) {
                stringBuilder.append(new Token(TokenType.RPARENT, ")" , getToken(-1).getRow())).append("\n");
                ErrorLog.getInstance().addError(new Error(getToken(-1).getRow(), "j"));
            }
        } else {
            if (print == 1) {
                stringBuilder.append(getNowToken()).append("\n");
            }
            next();
        }
    }

    public void b_error(int row) {
        if (print == 1) {
            ErrorLog.getInstance().addError(
                    new Error(row, "b"));
        }
    }

    public void g_error() {
        if (print == 1) {
            ErrorLog.getInstance().addError(
                    new Error(getNowToken().getRow(), "g"));
        }
    }

    public void h_error(Symbol symbol , Integer row1) {
        if (symbol instanceof SymbolConst && print == 1) {
            ErrorLog.getInstance().addError(
                    new Error(row1, "h"));
        }
    }

    public void m_error() {
        if (print == 1) {
            ErrorLog.getInstance().addError(new Error(getNowToken().getRow(), "m"));
        }
    }

    public void f_error(Integer row) {
        if (print == 1) {
            ErrorLog.getInstance().addError(new Error(row, "f"));
        }
    }

    public void l_error(Integer row) {
        if (print == 1) {
            ErrorLog.getInstance().addError(new Error(row, "l"));
        }
    }

    public void c_error(Integer row) {
        if (print == 1) {
            ErrorLog.getInstance().addError(new Error(row, "c"));
        }
    }

    public void d_error(Integer row) {
        if (print == 1) {
            ErrorLog.getInstance().addError(new Error(row, "d"));
        }
    }

    public void e_error(Integer row) {
        if (print == 1) {
            ErrorLog.getInstance().addError(new Error(row, "e"));
        }
    }
}