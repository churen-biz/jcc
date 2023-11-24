package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-23
 */
public class Node {
    public NodeKind kind; // Node kind
    public Node next;     // Next stmt
    public Node left;     // Left-hand side
    public Node right;     // Right-hand side
    public Long val;      // Used if kind == ND_NUM

    public Node() {
    }

    public Node(NodeKind kind, long val) {
        this.kind = kind;
        this.val = val;
    }

    public Node(NodeKind kind, Node left, Node right) {
        this.kind = kind;
        this.left = left;
        this.right = right;
    }
}
