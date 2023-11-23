package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-23
 */
public class Token {
    public TokenKind kind;
    public Token next;
    public Long val; // If kind is TK_NUM, its value
    String str;      // Token string
    public Long loc; // Location in the input

    public Token() {
    }

    public Token(TokenKind kind, long loc) {
        this.kind = kind;
        this.loc = loc;
    }

    public Token(TokenKind kind, Token next, Long val, String str) {
        this.kind = kind;
        this.next = next;
        this.val = val;
        this.str = str;
    }

}
