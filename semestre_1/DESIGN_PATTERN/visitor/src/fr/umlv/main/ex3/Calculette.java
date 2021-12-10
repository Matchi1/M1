package fr.umlv.main.ex3;

import java.util.regex.Pattern;

public class Calculette {
    private static ExprVisitor<Integer, Void> createEvalVisitor() {
        var evalVisitor = new ExprVisitor<Integer, Void>();
        evalVisitor
                .when(Value.class, (value, context) -> {
                    var element = (Value) value;
                    return element.getValue();
                })
                .when(BinOp.class, (binOp, context) -> {
                    var element = (BinOp) binOp;
                    var left = element.getLeft().accept(evalVisitor, context);
                    var right = element.getRight().accept(evalVisitor, context);
                    return element.getOperator().applyAsInt(left, right);
                });
        return evalVisitor;
    }

    private static ExprVisitor<Void, StringBuilder> createToStringVisitor() {
        var toStringVisitor = new ExprVisitor<Void, StringBuilder>();
        toStringVisitor
                .when(Value.class, (value, context) -> {
                    var element = (Value) value;
                    context.append(element.getValue());
                    return null;
                })
                .when(BinOp.class, (binOp, context) -> {
                    var element = (BinOp) binOp;
                    var left = element.getLeft();
                    var right = element.getRight();

                    context.append("(");
                    left.accept(toStringVisitor, context);
                    context.append(' ');
                    context.append(element.getOpName());
                    context.append(' ');
                    right.accept(toStringVisitor, context);
                    context.append(")");
                    return null;
                });
        return toStringVisitor;
    }


    public static void main(String[] args) {
        var it = Pattern.compile(" ").splitAsStream("+ * 4 + 1 1 + 2 3").iterator();
        var expr = Expr.parseExpr(it);
        var evalVisitor = createEvalVisitor();
        var toStringVisitor = createToStringVisitor();

        System.out.println(evalVisitor.visit(expr, null));
        var context = new StringBuilder();
        toStringVisitor.visit(expr, context);
        System.out.println(context);
    }
}
