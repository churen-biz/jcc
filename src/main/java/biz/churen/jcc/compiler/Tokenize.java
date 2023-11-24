package biz.churen.jcc.compiler;

import static biz.churen.jcc.compiler.TokenKind.TK_EOF;
import static biz.churen.jcc.compiler.TokenKind.TK_NUM;
import static biz.churen.jcc.compiler.TokenKind.TK_RESERVED;

/**
 * @author lihai03
 * Created on 2023-11-24
 */
public class Tokenize {
    private final String input;
    public Tokenize(String input) {
        this.input = input;
    }

    // Tokenize `input` and returns new tokens.
    public Token tokenize() {
        Token head = new Token();
        Token cur = head;
        int i = 0;
        while (i < input.length()) {
            char ci = input.charAt(i);
            // Skip whitespace characters.
            if (isSpace(ci)) {
                i++;
                continue;
            }

            //  Multi-letter Punctuator
            int len = isIdentifier(input, i);
            if (len > 0) {
                cur.next = new Token(TK_RESERVED, i);
                cur = cur.next;
                cur.str = input.substring(i, i + len);
                i += len;
                continue;
            }

            // Integer literal
            if (isDigit(ci)) {
                cur.next = new Token(TK_NUM, i);
                cur = cur.next;
                String numStr = firstNumber(input, i);
                i += numStr.length();
                cur.val = Long.parseLong(numStr);
                continue;
            }

            errorAt(
                input.substring(i, Math.min(input.length(), i + 5)),
                String.valueOf(i),
                "invalid token",
                (Object) null
            );
        }

        cur.next = new Token(TK_EOF, i);
        return head.next;
    }

    private static int isIdentifier(String s, int start) {
        if ((s.startsWith("return", start))
        ) {
            return 6;
        } else if ((s.startsWith("==", start))
                || (s.startsWith("!=", start))
                || (s.startsWith(">=", start))
                || (s.startsWith("<=", start))
        ) {
            return 2;
        } else if ((s.startsWith("+", start))
                || (s.startsWith("-", start))
                || (s.startsWith("*", start))
                || (s.startsWith("/", start))
                || (s.startsWith("<", start))
                || (s.startsWith(">", start))
                || (s.startsWith("(", start))
                || (s.startsWith(")", start))
                || (s.startsWith(";", start))
        ) {
            return 1;
        } else {
            return 0;
        }
    }

    private static String firstNumber(String s, int start) {
        int end = start;
        for (int i = start; i < s.length(); i++) {
            char ci = s.charAt(i);
            if (ci >= '0' && ci <= '9') {
                end++;
            } else {
                break;
            }
        }
        return s.substring(start, end);
    }

    private static boolean isSpace(char ci) {
        return (' ' == ci);
    }

    private static boolean isDigit(char ci) {
        return (ci >= '0' && ci <= '9');
    }


    // Reports an error and exit.
    private void errorAt(String tokenStr, String loc, String format, Object... args) {
        String err = String.format(format, args);
        System.err.println(err);
        String locStr = String.format("^^^ near by %s, location: %s", tokenStr, loc);
        System.err.println(locStr);
        throw new RuntimeException(err);
    }
}
