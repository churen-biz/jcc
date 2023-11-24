package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-24
 */
public class CodeGenerator {
    private final Node node;
    public CodeGenerator(Node node) {
        this.node = node;
    }

    // Traverse the AST to emit assembly.
    public String toAssembly() {
        StringBuilder sb = new StringBuilder();

        // Print out the first half of assembly.
        sb.append(".intel_syntax noprefix").append(System.lineSeparator());
        sb.append(".global main").append(System.lineSeparator());
        sb.append("main:").append(System.lineSeparator());

        // Traverse the AST to emit assembly.
        Node n = this.node;
        while (null != n) {
            genAssembly(n, sb);
            n = n.next;
        }

        sb.append("  ret").append(System.lineSeparator());
        return sb.toString();
    }

    // Parse `token` and returns new Node
    private void genAssembly(Node node, StringBuilder sb) {
        if (null == node) { return; }

        switch (node.kind) {
            case ND_NUM:
                sb.append("  push ").append(node.val).append(System.lineSeparator());
                return;
            case ND_EXPR_STMT:
                genAssembly(node.left, sb);
                sb.append("  add rsp, 8").append(System.lineSeparator());
                // sb.append("  pop rax").append(System.lineSeparator());
                return;
            case ND_RETURN:
                genAssembly(node.left, sb);
                sb.append("  pop rax").append(System.lineSeparator());
                sb.append("  ret").append(System.lineSeparator());
                return;
        }

        genAssembly(node.left, sb);
        genAssembly(node.right, sb);

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
            case ND_EQ:
                sb.append("  cmp rax, rdi").append(System.lineSeparator());
                sb.append("  sete al").append(System.lineSeparator());
                sb.append("  movzb rax, al").append(System.lineSeparator());
                break;
            case ND_NE:
                sb.append("  cmp rax, rdi").append(System.lineSeparator());
                sb.append("  setne al").append(System.lineSeparator());
                sb.append("  movzb rax, al").append(System.lineSeparator());
                break;
            case ND_LT:
                sb.append("  cmp rax, rdi").append(System.lineSeparator());
                sb.append("  setl al").append(System.lineSeparator());
                sb.append("  movzb rax, al").append(System.lineSeparator());
                break;
            case ND_LE:
                sb.append("  cmp rax, rdi").append(System.lineSeparator());
                sb.append("  setle al").append(System.lineSeparator());
                sb.append("  movzb rax, al").append(System.lineSeparator());
                break;
        }
        sb.append("  push rax").append(System.lineSeparator());
    }
}
