package biz.churen.jcc.compiler;

import static biz.churen.jcc.compiler.TokenKind.TK_EOF;
import static biz.churen.jcc.compiler.TokenKind.TK_NUM;
import static biz.churen.jcc.compiler.TokenKind.TK_RESERVED;

import biz.churen.jcc.utils.StrUtil;

/**
 * @author lihai03
 * Created on 2023-11-22
 */
public class JCC {
    private Token token = null;

    public String compile(String input) {
        this.token = tokenize(input);

        StringBuilder sb = new StringBuilder();
        sb.append(".intel_syntax noprefix").append(System.lineSeparator());
        sb.append(".global main").append(System.lineSeparator());
        sb.append("main:").append(System.lineSeparator());

        // The first token must be a number
        sb.append("  mov rax, ").append(expectNumber()).append(System.lineSeparator());

        // ... followed by either `+ <number>` or `- <number>`.
        while (!atEOF()) {
            if (consume('+')) {
                sb.append("  add rax, ").append(expectNumber()).append(System.lineSeparator());
            } else {
                expect('-');
                sb.append("  sub rax, ").append(expectNumber()).append(System.lineSeparator());
            }
        }

        sb.append("  ret").append(System.lineSeparator());
        return sb.toString();
    }



    // Tokenize `p` and returns new tokens.
    public Token tokenize(String p) {
        Token head = new Token();
        Token cur = head;
        int i = 0;
        while (i < p.length()) {
            char ci = p.charAt(i);
            // Skip whitespace characters.
            if (StrUtil.isSpace(ci)) {
                i++;
                continue;
            }

            // Punctuator
            if (ci == '+' || ci == '-') {
                cur.next = new Token(TK_RESERVED);
                cur = cur.next;
                cur.str = String.valueOf(ci);
                i++;
                continue;
            }

            // Integer literal
            if (StrUtil.isDigit(ci)) {
                cur.next = new Token(TK_NUM);
                cur = cur.next;
                String numStr = StrUtil.firstNumber(p, i);
                i += numStr.length();
                cur.val = Long.parseLong(numStr);
                continue;
            }

            error("invalid token");
        }

        cur.next = new Token(TK_EOF);
        return head.next;
    }

    // Consumes the current token if it matches `op`.
    public boolean consume(char op) {
        if (token.kind != TK_RESERVED || (token.str).charAt(0) != op) {
            return false;
        }
        token = token.next;
        return true;
    }

    // Ensure that the current token is `op`.
    public void expect(char op) {
        if (token.kind != TK_RESERVED || (token.str).charAt(0) != op) {
            error("expected '%s'", op);
        }
        token = token.next;
    }

    // Ensure that the current token is TK_NUM.
    long expectNumber() {
        if (token.kind != TK_NUM) {
            error("expected a number");
        }
        long val = token.val;
        token = token.next;
        return val;
    }

    public boolean atEOF() {
        return TK_EOF == token.kind;
    }

    // Reports an error and exit.
    public static void error(String format, Object... args) {
        String err = String.format(format, args);
        System.err.println(err);
        throw new RuntimeException(err);
    }
}
