package fr.umlv.main.ex2;

import java.util.Iterator;

public interface Expr {
    public <T> T accept(ExprVisitor visitor);

    public static Expr parseExpr(Iterator<String> it) {
        if (!it.hasNext()) {
            throw new IllegalArgumentException("no more tokens");
        }
        String token = it.next();
        switch(token) {
            case "+":
                return new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a + b);
            case "-":
                return new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a - b);
            case "*":
                return new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a * b);
            case "/":
                return new BinOp(parseExpr(it), parseExpr(it), token, (a, b) -> a / b);
            default:
                return new Value(Integer.parseInt(token));
        }
    }

}
