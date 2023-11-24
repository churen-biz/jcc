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
        String asm = jcc.compile("1+2");
        System.out.println(asm);
    }
}