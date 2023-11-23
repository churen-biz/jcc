package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-23
 */
public class Node {
    public NodeKind kind; // Node kind
    public Node left;     // Left-hand side
    public Node right;     // Right-hand side
    public long val;      // Used if kind == ND_NUM

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
