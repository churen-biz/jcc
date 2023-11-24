package biz.churen.jcc.compiler;

/**
 * @author lihai03
 * Created on 2023-11-23
 */
public enum NodeKind {
    ND_ADD, // +
    ND_SUB, // -
    ND_MUL, // *
    ND_DIV, // /
    ND_NEG, // unary -
    ND_EQ,  // ==
    ND_NE,  // !=
    ND_LT,  // <
    ND_LE,  // <=
    // ND_GT,  // >
    // ND_GE,  // >=
    ND_EXPR_STMT, // Expression statement
    ND_RETURN, // "return"
    ND_NUM, // Integer
}
