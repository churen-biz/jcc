package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-22
 */
public class JCC {
    public String compile(String input) {
        // Tokenize
        Token token = (new Tokenize(input)).tokenize();
        // Parse
        Node node = (new Parse(token)).expr();
        // To Assembly
        return (new CodeGenerator(node)).toAssembly();
    }
}
