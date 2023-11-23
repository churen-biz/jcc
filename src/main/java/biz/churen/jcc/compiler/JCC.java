package biz.churen.jcc.compiler;

import biz.churen.jcc.utils.StrUtil;

/**
 * @author lihai03
 * Created on 2023-11-22
 */
public class JCC {

    public String compile(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append(".intel_syntax noprefix").append(System.lineSeparator());
        sb.append(".global main").append(System.lineSeparator());
        sb.append("main:").append(System.lineSeparator());
        // first number
        int i = 0;
        String firstNumber = StrUtil.firstNumber(input, i);
        i += firstNumber.length();
        sb.append("  mov rax, ").append(firstNumber).append(System.lineSeparator());
        while (i < input.length()) {
            char c = input.charAt(i);
            if (c == '+') {
                i++;
                String numStr = StrUtil.firstNumber(input, i);
                i += numStr.length();
                sb.append("  add rax, ").append(numStr).append(System.lineSeparator());
                continue;
            }
            if (c == '-') {
                i++;
                String numStr = StrUtil.firstNumber(input, i);
                i += numStr.length();
                sb.append("  sub rax, ").append(numStr).append(System.lineSeparator());
                continue;
            }

            String error = "unexpected character: '"+ c +"'";
            System.err.println(error);
            throw new RuntimeException(error);
        }

        sb.append("  ret").append(System.lineSeparator());
        return sb.toString();
    }
}
