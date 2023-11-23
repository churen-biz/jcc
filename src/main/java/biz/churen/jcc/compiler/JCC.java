package biz.churen.jcc.compiler;

import static biz.churen.jcc.compiler.NodeKind.ND_ADD;
import static biz.churen.jcc.compiler.NodeKind.ND_DIV;
import static biz.churen.jcc.compiler.NodeKind.ND_MUL;
import static biz.churen.jcc.compiler.NodeKind.ND_NUM;
import static biz.churen.jcc.compiler.NodeKind.ND_SUB;
import static biz.churen.jcc.compiler.TokenKind.TK_EOF;
import static biz.churen.jcc.compiler.TokenKind.TK_NUM;
import static biz.churen.jcc.compiler.TokenKind.TK_RESERVED;

import biz.churen.jcc.utils.StrUtil;

/**
 * @author lihai03
 * Created on 2023-11-22
 */
@SuppressWarnings({"StringBufferReplaceableByString", "FieldCanBeLocal"})
public class JCC {
    public Token token = null;
    public Node node = null;

    public String compile(String input) {
        // Tokenize
        this.token = tokenize(input);
        // Parse
        this.node = expr();

        StringBuilder sb = new StringBuilder();
        // Print out the first half of assembly.
        sb.append(".intel_syntax noprefix").append(System.lineSeparator());
        sb.append(".global main").append(System.lineSeparator());
        sb.append("main:").append(System.lineSeparator());

        // Traverse the AST to emit assembly.
        sb.append(gen(this.node));

        // A result must be at the top of the stack, so pop it
        // to RAX to make it a program exit code.
        sb.append("  pop rax").append(System.lineSeparator());
        sb.append("  ret").append(System.lineSeparator());
        return sb.toString();
    }

    // Parse `token` and returns new Node
    public String gen(Node node) {
        StringBuilder sb = new StringBuilder();
        if (null == node) { return sb.toString(); }

        if (node.kind == ND_NUM) {
            sb.append("  push ").append(node.val).append(System.lineSeparator());
            return sb.toString();
        }
        sb.append(gen(node.left));
        sb.append(gen(node.right));

        sb.append("  pop rdi").append(System.lineSeparator());
        sb.append("  pop rax").append(System.lineSeparator());

        switch (node.kind) {
            case ND_ADD:
                sb.append("  add rax, rdi").append(System.lineSeparator());
                break;
            case ND_SUB:
                sb.append("  sub rax, rdi").append(System.lineSeparator());
                break;
            case ND_MUL:
                sb.append("  imul rax, rdi").append(System.lineSeparator());
                break;
            case ND_DIV:
                sb.append("  cqo").append(System.lineSeparator());
                sb.append("  idiv rdi").append(System.lineSeparator());
                break;
        }
        sb.append("  push rax").append(System.lineSeparator());
        return sb.toString();
    }

    // Parse `token` and returns new Node
    // expr = mul ("+" mul | "-" mul)*
    public Node expr() {
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
    public Node mul() {
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

    // primary = "(" expr ")" | num
    public Node primary() {
        if (consume("(")) {
            Node node = expr();
            expect(")");
            return node;
        }
        return new Node(ND_NUM, expectNumber());
    }

    // unary = ("+" | "-")? unary
    //       | primary
    public Node unary() {
        if (consume("+")) {
            return unary();
        }
        if (consume("-")) {
            return new Node(ND_SUB, new Node(ND_NUM, 0L), unary());
        }
        return primary();
    }

    // Tokenize `input` and returns new tokens.
    public Token tokenize(String input) {
        Token head = new Token();
        Token cur = head;
        int i = 0;
        while (i < input.length()) {
            char ci = input.charAt(i);
            // Skip whitespace characters.
            if (StrUtil.isSpace(ci)) {
                i++;
                continue;
            }

            // Punctuator
            if (StrUtil.isPunctuator(ci)) {
                cur.next = new Token(TK_RESERVED, i);
                cur = cur.next;
                cur.str = String.valueOf(ci);
                i++;
                continue;
            }

            // Integer literal
            if (StrUtil.isDigit(ci)) {
                cur.next = new Token(TK_NUM, i);
                cur = cur.next;
                String numStr = StrUtil.firstNumber(input, i);
                i += numStr.length();
                cur.val = Long.parseLong(numStr);
                continue;
            }

            errorAt(String.valueOf(ci), String.valueOf(i), "invalid token", (Object) null);
        }

        cur.next = new Token(TK_EOF, i);
        return head.next;
    }

    //

    // Consumes the current token if it matches `op`.
    public boolean consume(String op) {
        if (token.kind != TK_RESERVED || !op.equals(token.str)) {
            return false;
        }
        token = token.next;
        return true;
    }

    // Ensure that the current token is `op`.
    public void expect(String op) {
        if (token.kind != TK_RESERVED || !op.equals(token.str)) {
            errorAt(token,"expected '%s'", op);
        }
        token = token.next;
    }

    // Ensure that the current token is TK_NUM.
    long expectNumber() {
        if (token.kind != TK_NUM) {
            errorAt(token,"expected a number");
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

    // Reports an error and exit.
    public static void errorAt(Token token, String format, Object... args) {
        errorAt(token.str, String.valueOf(token.loc), format, args);
    }

    // Reports an error and exit.
    public static void errorAt(String tokenStr, String loc, String format, Object... args) {
        String err = String.format(format, args);
        System.err.println(err);
        String locStr = String.format("^^^ near by %s, location: %s", tokenStr, loc);
        System.err.println(locStr);
        throw new RuntimeException(err);
    }
}
