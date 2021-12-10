package fr.umlv.main.ex2;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Calculette {
    public static void main(String[] args) {
        var it = Pattern.compile(" ").splitAsStream("+ * 4 + 1 1 + 2 3").iterator();
        Expr expr = Expr.parseExpr(it);
        var visitor = new EvalExprVisitor();
        var visitorString = new ToStringVisitor();
        var context = new StringBuilder();
        System.out.println(expr);
        System.out.println(expr.accept(visitor, null));
        expr.accept(visitorString, context);
        System.out.println(context);
    }
}
