package biz.churen.jcc.compiler;

import org.junit.jupiter.api.Test;

/**
 * @author lihai03 <lihai03@kuaishou.com>
 * Created on 2023-11-23
 */
class JCCTest {

    @Test
    void compile() {
    }

    @Test
    void tokenize() {
        JCC jcc = new JCC();
        // tokenize
        String input = "1>0";
        Token token = jcc.tokenize(input);
        Token token0 = token;

        StringBuilder sb = new StringBuilder();
        while (null != token) {
            String t0 = String.format("(kind: %s, str: %s, val: %s) "
                    , token.kind, token.str, token.val);
            sb.append(t0);
            sb.append(" -> ");
            token = token.next;
        }
        jcc.token = token0;
        System.out.println(sb);

        // parse
        Node node = jcc.expr();

        // generate assembly
        String sm = jcc.gen(node);
        System.out.println("sm ==> ");
        System.out.println(sm);
    }
}