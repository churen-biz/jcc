package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-22
 */
@SuppressWarnings("StringBufferReplaceableByString")
public class JCC {

    public String compile(String input) {
        StringBuilder sb = new StringBuilder();
        sb.append(".intel_syntax noprefix").append(System.lineSeparator());
        sb.append(".global main").append(System.lineSeparator());
        sb.append("main:").append(System.lineSeparator());
        sb.append("  mov rax, ").append(Integer.parseInt(input)).append(System.lineSeparator());
        sb.append("  ret").append(System.lineSeparator());
        return sb.toString();
    }
}
