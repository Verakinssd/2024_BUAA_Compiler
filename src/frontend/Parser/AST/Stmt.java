package frontend.Parser.AST;

import backend.LLVMIR.FuncIR.*;
import backend.LLVMIR.GlobalDeclIR.GlobalStringDeclIR;
import backend.LLVMIR.RegIR;
import frontend.Lexer.TokenType;
import frontend.Symbol.Symbol;
import frontend.Symbol.SymbolTable;

import java.util.ArrayList;

import static backend.LLVMIR.RegType.*;

public class Stmt extends AstNode{
    public Integer returnRow = 0;
    public Integer type;
    public LVal lVal = new LVal();
    public ArrayList<Exp> exps = new ArrayList<>();
    public Block block = new Block();
    public Cond cond = null;
    public ArrayList<Stmt> stmts = new ArrayList<>();
    public String stringConst = "";
    public Boolean hasElse = false;
    public ForStmt forStmt1 = null;
    public ForStmt forStmt2 = null;
    public BasicBlockIR afterBB = new BasicBlockIR();
    public BasicBlockIR forStmt2BB = new BasicBlockIR();
    public TokenType tokenType = null;

    public void parse(SymbolTable symbolTable, Integer isReturn , Boolean inFor) {
        //System.out.println(getNowToken() + " " + getNowToken().getRow());
        if (getNowTokenType() == TokenType.IDENFR) {
            int curPos = AstNode.pos;
            AstNode.print = 0;
            Exp exp = new Exp();
            exp.parse(symbolTable);
            AstNode.print = 1;
            if (getNowTokenType() == TokenType.ASSIGN) {
                AstNode.pos = curPos;
                int row1 = getNowToken().getRow();
                lVal.parse(symbolTable);
                Symbol symbol = symbolTable.getSymbol(lVal.ident);
                h_error(symbol , row1);
                append(1);
                if (getNowTokenType() == TokenType.GETINTTK
                        || getNowTokenType() == TokenType.GETCHARTK) {
                    this.type = (getNowTokenType() == TokenType.GETINTTK ? 8 : 9);
                    append(2);
                    j_error();
                    i_error();
                }  else {
                    exp = new Exp();
                    exp.parse(symbolTable);
                    exps.add(exp);
                    i_error();
                    this.type = 1;
                }
            } else {
                AstNode.pos = curPos;
                if (isExp()) {
                    exp = new Exp();
                    exp.parse(symbolTable);
                    exps.add(exp);
                }
                i_error();
                this.type = 2;
            }
        } else if (getNowTokenType() == TokenType.SEMICN) {
            append(1);
            this.type = 2;
        } else if (getNowTokenType() == TokenType.LBRACE) {
            block.parse(symbolTable, true , isReturn == 0 ? 0 : 2 , inFor);
            this.type = 3;
        } else if (getNowTokenType() == TokenType.IFTK) {
            append(2);
            Cond cond = new Cond();
            cond.parse(symbolTable);
            this.cond = cond;
            j_error();
            Stmt stmt = new Stmt();
            stmt.parse(symbolTable , isReturn == 0 ? 0 : 2 , inFor);
            this.stmts.add(stmt);
            if (getNowTokenType() == TokenType.ELSETK) {
                hasElse = true;
                append(1);
                Stmt stmt1 = new Stmt();
                stmt1.parse(symbolTable , isReturn == 0 ? 0 : 2 , inFor);
                this.stmts.add(stmt1);
            }
            this.type = 4;
        } else if (getNowTokenType() == TokenType.FORTK) {
            append(2);
            if (getNowTokenType() != TokenType.SEMICN) {
                ForStmt forStmt = new ForStmt();
                forStmt.parse(symbolTable);
                forStmt1 = forStmt;
            }
            append(1);
            if (getNowTokenType() != TokenType.SEMICN) {
                Cond cond = new Cond();
                cond.parse(symbolTable);
                this.cond = cond;
            }
            append(1);
            if (getNowTokenType() != TokenType.RPARENT) {
                ForStmt forStmt = new ForStmt();
                forStmt.parse(symbolTable);
                forStmt2 = forStmt;
            }
            append(1);
            Stmt stmt = new Stmt();
            stmt.parse(symbolTable, isReturn == 0 ? 0 : 2 ,true);
            stmts.add(stmt);
            this.type = 5;
        } else if (getNowTokenType() == TokenType.BREAKTK ||
        getNowTokenType() == TokenType.CONTINUETK) {
            tokenType = getNowTokenType();
            if (!inFor) {
                m_error();
            }
            append(1);
            i_error();
            this.type = 6;
        } else if (getNowTokenType() == TokenType.RETURNTK) {
            returnRow = getNowToken().getRow();
            append(1);
            if (isExp()) {
                if (isReturn == 0) {
                    f_error(returnRow);
                }
                Exp exp = new Exp();
                exp.parse(symbolTable);
                this.exps.add(exp);
            }
            i_error();
            this.type = 7;
        } else if (getNowTokenType() == TokenType.PRINTFTK) {
            int row1 = getNowToken().getRow();
            append(2);
            String stringConst = getNowToken().getContent();
            this.stringConst = stringConst;
            int cnt = 0;
            for (int i = 0;i < stringConst.length();i++) {
                if (stringConst.charAt(i) == '%'
                        && i + 1 < stringConst.length()
                        && (stringConst.charAt(i + 1) == 'd' || stringConst.charAt(i + 1) == 'c')) {
                    cnt ++;
                }
            }
            append(1);
            while (getNowTokenType() == TokenType.COMMA) {
                cnt --;
                append(1);
                Exp exp = new Exp();
                exp.parse(symbolTable);
                this.exps.add(exp);
            }
            if (cnt != 0) {
                l_error(row1);
            }
            j_error();
            i_error();
            this.type = 10;
        } else if (isExp()) {
            if (isExp()) {
                Exp exp = new Exp();
                exp.parse(symbolTable);
                this.exps.add(exp);
            }
            i_error();
            this.type = 2;
        } else {
            i_error();
            this.type = 2;
        }
        stringBuilder.append("<Stmt>").append("\n");
    }

    public void generate_LLVMIR(FuncIR funcIR) {
        if (type == 1) {
            RegIR regIR1 = lVal.getVariableValue(funcIR, false);
            RegIR regIR2 = exps.get(0).getVariableValue(funcIR);
            if (regIR2.type == 2) {
                if (regIR2.regType == I32 && regIR1.regType == I8_PTR) {
                    funcIR.addInstructionIR(new TruncIR("%" + FuncIR.reg++, I8, regIR2.reg, regIR2.regType));
                    regIR2.reg = "%" + (FuncIR.reg - 1);
                    regIR2.regType = I8;
                } else if (regIR2.regType == I8 && regIR1.regType == I32_PTR) {
                    funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR2.reg, regIR2.regType));
                    regIR2.reg = "%" + (FuncIR.reg - 1);
                    regIR2.regType = I32;
                }
            } else {
                regIR2.regType = getReference(regIR1.regType);
            }
            funcIR.addInstructionIR(new StoreIR(regIR2, regIR1.reg, regIR1.regType, regIR1.isGlobal));
        } else if (type == 2) {
            if (!exps.isEmpty()) {
                exps.get(0).getVariableValue(funcIR);
            }
        } else if (type == 3) {
            block.forStmt2BB = forStmt2BB;
            block.afterBB = afterBB;
            block.generate_LLVMIR(funcIR);
        } else if (type == 4) {
            BasicBlockIR ifBB = new BasicBlockIR();
            BasicBlockIR elseBB = new BasicBlockIR();
            BasicBlockIR afterBB = new BasicBlockIR();
            if (!hasElse) {
                if (cond != null) {
                    cond.generate_LLVMIR(funcIR, ifBB, afterBB);
                } else {
                    funcIR.addInstructionIR(new BrIR(ifBB));
                }
                ifBB.name = "%" + FuncIR.reg ++;
                funcIR.addInstructionIR(ifBB);
                stmts.get(0).afterBB = this.afterBB;
                stmts.get(0).forStmt2BB = forStmt2BB;
                stmts.get(0).generate_LLVMIR(funcIR);
                afterBB.name = "%" + FuncIR.reg ++;
                funcIR.addInstructionIR(new BrIR(afterBB));
                funcIR.addInstructionIR(afterBB);
            } else {
                if (cond != null) {
                    cond.generate_LLVMIR(funcIR, ifBB, elseBB);
                } else {
                    funcIR.addInstructionIR(new BrIR(ifBB));
                }
                ifBB.name = "%" + FuncIR.reg ++;
                funcIR.addInstructionIR(ifBB);
                stmts.get(0).afterBB = this.afterBB;
                stmts.get(0).forStmt2BB = forStmt2BB;
                stmts.get(0).generate_LLVMIR(funcIR);
                funcIR.addInstructionIR(new BrIR(afterBB));

                elseBB.name = "%" + FuncIR.reg ++;
                funcIR.addInstructionIR(elseBB);
                stmts.get(1).afterBB = this.afterBB;
                stmts.get(1).forStmt2BB = forStmt2BB;
                stmts.get(1).generate_LLVMIR(funcIR);
                afterBB.name = "%" + FuncIR.reg ++;
                funcIR.addInstructionIR(new BrIR(afterBB));
                funcIR.addInstructionIR(afterBB);
            }
        } else if (type == 5) {
            if (forStmt1 != null) {
                forStmt1.generate_LLVMIR(funcIR);
            }
            BasicBlockIR condBB = new BasicBlockIR();
            BasicBlockIR stmtBB = new BasicBlockIR();
            BasicBlockIR forStmt2BB = new BasicBlockIR();
            BasicBlockIR afterBB = new BasicBlockIR();
            condBB.name = "%" + FuncIR.reg ++;
            funcIR.addInstructionIR(new BrIR(condBB));
            funcIR.addInstructionIR(condBB);
            if (cond != null) {
                cond.generate_LLVMIR(funcIR, stmtBB, afterBB);
            } else {
                funcIR.addInstructionIR(new BrIR(stmtBB));
            }
            stmtBB.name = "%" + FuncIR.reg++;
            funcIR.addInstructionIR(stmtBB);
            stmts.get(0).afterBB = afterBB;
            stmts.get(0).forStmt2BB = forStmt2BB;
            stmts.get(0).generate_LLVMIR(funcIR);
            funcIR.addInstructionIR(new BrIR(forStmt2BB));
            forStmt2BB.name = "%" + FuncIR.reg ++;
            funcIR.addInstructionIR(forStmt2BB);
            if (forStmt2 != null) {
                forStmt2.generate_LLVMIR(funcIR);
            }
            funcIR.addInstructionIR(new BrIR(condBB));
            afterBB.name = "%" + FuncIR.reg ++;
            funcIR.addInstructionIR(afterBB);
        } else if (type == 6) {
            if (tokenType == TokenType.BREAKTK) {
                funcIR.addInstructionIR(new BrIR(afterBB));
            } else {
                funcIR.addInstructionIR(new BrIR(forStmt2BB));
            }
        } else if (type == 7) {
            funcIR.isReturn = true;
            if (!exps.isEmpty()) {
                RegIR regIR = exps.get(0).getVariableValue(funcIR);
                if (regIR.regType == I32 && funcIR.funcType == TokenType.CHARTK) {
                    if (regIR.type == 2) {
                        funcIR.addInstructionIR(new TruncIR("%" + FuncIR.reg++, I8, regIR.reg, regIR.regType));
                        regIR.reg = "%" + (FuncIR.reg - 1);
                    }
                    regIR.regType = I8;
                } else if (regIR.regType == I8 && funcIR.funcType == TokenType.INTTK) {
                    if (regIR.type == 2) {
                        funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg, regIR.regType));
                        regIR.reg = "%" + (FuncIR.reg - 1);
                    }
                    regIR.regType = I32;
                }
                funcIR.addInstructionIR(new ReturnIR(regIR));
            } else {
                funcIR.addInstructionIR(new ReturnIR());
            }
        } else if (type == 8) {
            RegIR regIR1 = new RegIR(I32,"%" + FuncIR.reg ++);
            funcIR.addInstructionIR(new GetIntIR(regIR1));
            RegIR regIR2 = lVal.getVariableValue(funcIR, false);
            if (regIR2.regType == I8_PTR) {
                funcIR.addInstructionIR(new TruncIR("%" + FuncIR.reg++, I8, regIR1.reg, regIR1.regType));
            }
            RegIR regIR3 = new RegIR(I8, "%" + (FuncIR.reg - 1));
            funcIR.addInstructionIR(new StoreIR(regIR2.regType == I8_PTR ? regIR3 : regIR1, regIR2.reg, regIR2.regType , regIR2.isGlobal));
        } else if (type == 9) {
            RegIR regIR1 = new RegIR(I32,"%" + FuncIR.reg ++);
            funcIR.addInstructionIR(new GetCharIR(regIR1));
            funcIR.addInstructionIR(new TruncIR("%" + FuncIR.reg ++,I8, regIR1.reg , regIR1.regType ));
            RegIR regIR3 = new RegIR(I8, "%" + (FuncIR.reg - 1));
            RegIR regIR2 = lVal.getVariableValue(funcIR, false);
            funcIR.addInstructionIR(new StoreIR(regIR3, regIR2.reg, regIR2.regType, regIR2.isGlobal));
        } else if (type == 10) {
            StringBuilder sb = new StringBuilder();
            GlobalStringDeclIR globalStringDeclIR;
            ArrayList<RegIR> regIRs = new ArrayList<>();
            for (Exp exp : exps) {
                regIRs.add(exp.getVariableValue(funcIR));
            }
            int tmp = 0;
            int arraySize = 0;
            String str = stringConst.substring(1, stringConst.length() - 1);
            for (int i = 0;i < str.length();i++) {
                if (str.charAt(i) == '%' && i + 1 < str.length() &&
                        (str.charAt(i + 1) == 'd' || str.charAt(i + 1) == 'c') ) {
                    if (!sb.isEmpty()) {
                        sb.append("\\00");
                        arraySize ++;
                        globalStringDeclIR = new GlobalStringDeclIR(sb.toString(), arraySize);
                        AstNode.globalDeclIRS.add(globalStringDeclIR);
                        funcIR.addInstructionIR(new PutStrIR(arraySize, globalStringDeclIR.reg));
                    }
                    RegIR regIR = regIRs.get(tmp);
                    if (regIR.regType == I8) {
                        if (regIR.type == 2) {
                            funcIR.addInstructionIR(new ZextIR("%" + FuncIR.reg++, I32, regIR.reg, I8));
                            regIR.reg = "%" + (FuncIR.reg - 1);
                        }
                        regIR.regType = I32;
                    }
                    if (str.charAt(i + 1) == 'd') {
                        funcIR.addInstructionIR(new PutIntIR(regIR));
                    } else {
                        funcIR.addInstructionIR(new PutChIR(regIR));
                    }
                    i ++;
                    tmp ++;
                    arraySize = 0;
                    sb = new StringBuilder();
                } else {
                    if (isEscapeCharacter(str, i)) {
                        i++;
                        arraySize ++;
                        sb.append(getEscapeCharacter(str.charAt(i)));
                    } else {
                        arraySize ++;
                        sb.append(str.charAt(i));
                    }
                }
            }
            if (!sb.isEmpty()) {
                sb.append("\\00");
                arraySize ++;
                globalStringDeclIR = new GlobalStringDeclIR(sb.toString(), arraySize);
                AstNode.globalDeclIRS.add(globalStringDeclIR);
                funcIR.addInstructionIR(new PutStrIR(arraySize, globalStringDeclIR.reg));
            }
        }
    }

    private Boolean isEscapeCharacter(String str, int i) {
        return (str.charAt(i) == '\\' && i + 1 < str.length() &&
                (str.charAt(i + 1) == 'a' || str.charAt(i + 1) == 'b' || str.charAt(i + 1) == 't' || str.charAt(i + 1) == 'n'
                || str.charAt(i + 1) == 'v' || str.charAt(i + 1) == 'f' || str.charAt(i + 1) == '"' || str.charAt(i + 1) == '\''
                || str.charAt(i + 1) == '\\' || str.charAt(i + 1) == '0'));
    }

    public String getEscapeCharacter(char ch) {
        switch (ch) {
            case 'a' : return "\\07";
            case 'b' : return "\\08";
            case 't' : return "\\09";
            case 'n' : return "\\0A";
            case 'v' : return "\\0B";
            case 'f' : return "\\0C";
            case 'r' : return "\\0D";
            case '"' : return "\\22";
            case '\'' : return "\\27";
            case '\\' : return "\\5C";
            case '0' : return "\\00";
            default : return "";
        }
    }
}
