package biz.churen.jcc;

import biz.churen.jcc.compiler.JCC;

/**
 * @author churen
 * Created on 2023-11-22
 */
public class JCCApplication {
    public static void main(String[] args) {
        try {
            int argsSize = args.length;
            if (argsSize != 1) {
                System.err.println("JCCApplication: invalid number of arguments");
                return;
            }
            String input = args[0];
            JCC jcc = new JCC();
            String output = jcc.compile(input);
            System.out.println(output);
        } catch (Throwable th) {
            System.err.println("JCCApplication exit 1 !!!");
        }
    }
}
