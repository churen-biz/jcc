package biz.churen.jcc.compiler;

import static biz.churen.jcc.compiler.NodeKind.ND_ADD;
import static biz.churen.jcc.compiler.NodeKind.ND_DIV;
import static biz.churen.jcc.compiler.NodeKind.ND_EQ;
import static biz.churen.jcc.compiler.NodeKind.ND_LE;
import static biz.churen.jcc.compiler.NodeKind.ND_LT;
import static biz.churen.jcc.compiler.NodeKind.ND_MUL;
import static biz.churen.jcc.compiler.NodeKind.ND_NE;
import static biz.churen.jcc.compiler.NodeKind.ND_NUM;
import static biz.churen.jcc.compiler.NodeKind.ND_RETURN;
import static biz.churen.jcc.compiler.NodeKind.ND_SUB;
import static biz.churen.jcc.compiler.TokenKind.TK_EOF;
import static biz.churen.jcc.compiler.TokenKind.TK_NUM;
import static biz.churen.jcc.compiler.TokenKind.TK_RESERVED;

/**
 *
 *
 * BNF:
 * program = stmt*
 * stmt = "return" expr ";"
 *        | expr ";"
 * expr = equality
 * equality = relational ("==" relational | "!=" relational)*
 * relational = add ("<" add | "<=" add | ">" add | ">=" add)*
 * add = mul ("+" mul | "-" mul)*
 * mul = unary ("*" unary | "/" unary)*
 * unary = ("+" | "-")? unary
 *        | primary
 * primary = "(" expr ")" | num
 *
 * @author lihai03
 * Created on 2023-11-24
 */
public class Parse {
    private Token token;

    public Parse(Token token) {
        this.token = token;
    }

    // program = stmt*
    public Node program() {
        Node head = new Node();
        Node cur = head;
        while (!atEOF()) {
            cur.next = stmt();
            cur = cur.next;
        }
        return head.next;
    }

    // stmt = "return" expr ";"
    //      | expr ";"
    private Node stmt() {
        if (consume("return")) {
            Node node = new Node(ND_RETURN, expr(), null);
            expect(";");
            return node;
        }
        Node node = expr();
        expect(";");
        return node;
    }

    // expr = equality
    private Node expr() {
        return equality();
    }

    // equality = relational ("==" relational | "!=" relational)*
    private Node equality() {
        Node node = relational();
        for (;;) {
            if (consume("==")) {
                node = new Node(ND_EQ, node, relational());
            } else if (consume("!=")) {
                node = new Node(ND_NE, node, relational());
            } else {
                return node;
            }
        }
    }

    // relational = add ("<" add | "<=" add | ">" add | ">=" add)*
    private Node relational() {
        Node node = add();
        for (;;) {
            if (consume("<")) {
                node = new Node(ND_LT, node, add());
            } else if (consume("<=")) {
                node = new Node(ND_LE, node, add());
            } else if (consume(">")) {
                node = new Node(ND_LT, add(), node);
            } else if (consume(">=")) {
                node = new Node(ND_LE, add(), node);
            } else {
                return node;
            }
        }
    }

    // add = mul ("+" mul | "-" mul)*
    private Node add() {
        Node node = mul();

        for (;;) {
            if (consume("+")) {
                node = new Node(ND_ADD, node, mul());
            } else if (consume("-")) {
                node = new Node(ND_SUB, node, mul());
            } else {
                return node;
            }
        }
    }

    // mul = unary ("*" unary | "/" unary)*
    private Node mul() {
        Node node = unary();

        for (;;) {
            if (consume("*"))
                node = new Node(ND_MUL, node, unary());
            else if (consume("/"))
                node = new Node(ND_DIV, node, unary());
            else
                return node;
        }
    }

    // unary = ("+" | "-")? unary
    //       | primary
    private Node unary() {
        if (consume("+")) {
            return unary();
        }
        if (consume("-")) {
            return new Node(ND_SUB, new Node(ND_NUM, 0L), unary());
        }
        return primary();
    }

    // primary = "(" expr ")" | num
    private Node primary() {
        if (consume("(")) {
            Node node = expr();
            expect(")");
            return node;
        }
        return new Node(ND_NUM, expectNumber());
    }


    // Consumes the current token if it matches `op`.
    private boolean consume(String op) {
        if (token.kind != TK_RESERVED || !op.equals(token.str)) {
            return false;
        }
        token = token.next;
        return true;
    }

    // Ensure that the current token is `op`.
    private void expect(String op) {
        if (token.kind != TK_RESERVED || !op.equals(token.str)) {
            errorAt(token,"expected '%s'", op);
        }
        token = token.next;
    }

    // Ensure that the current token is TK_NUM.
    private long expectNumber() {
        if (token.kind != TK_NUM) {
            errorAt(token,"expected a number");
        }
        long val = token.val;
        token = token.next;
        return val;
    }

    private boolean atEOF() {
        return TK_EOF == token.kind;
    }

    // Reports an error and exit.
    public static void errorAt(Token token, String format, Object... args) {
        String err = String.format(format, args);
        System.err.println(err);
        String locStr = String.format("^^^ near by %s, location: %s", token.str, token.loc);
        System.err.println(locStr);
        throw new RuntimeException(err);
    }
}
