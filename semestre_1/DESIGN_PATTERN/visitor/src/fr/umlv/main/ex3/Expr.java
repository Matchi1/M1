package fr.umlv.main.ex3;

import java.util.Iterator;

public interface Expr {
    // public <T> T accept(ExprVisitor<T> visitor);
    public <T, C> T accept(ExprVisitor<T, C> visitor, C context);

    public static Expr parseExpr(Iterator<String> it) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException("no more tokens");
        }
        String token = it.next();
        return switch (token) {
            case "+" -> new BinOp(parseExpr(it), parseExpr(it), token, Integer::sum);
            case "-" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a - b);
            case "*" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a * b);
            case "/" -> new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a / b);
            default -> new Value(Integer.parseInt(token));
        };
    }

}
