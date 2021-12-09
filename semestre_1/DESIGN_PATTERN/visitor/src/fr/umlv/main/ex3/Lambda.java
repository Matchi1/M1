package fr.umlv.main.ex3;

public class Lambda {
    public static void main(String[] args) {
        ExprVisitor<Integer> evalVisitor = new ExprVisitor<>();
        evalVisitor
                .when(Value.class, value -> {
                })
                .when(BinOp.class, binOp -> {
                });
        evalVisitor.visit(expr);
    }
}
