package fr.umlv.main.ex3;

import java.util.Iterator;
import java.util.regex.Pattern;

public class Calculette {
    public static void main(String[] args) {
        Iterator<String> it = Pattern.compile(" ").splitAsStream("+ * 4 + 1 1 + 2 3").iterator();
        Expr expr = Expr.parseExpr(it);
        var visitor = new EvalExprVisitor();
        var visitorString = new ToStringVisitor();
        System.out.println(expr);
        System.out.println(expr.accept(visitor));
        expr.accept(visitorString);
        System.out.println(visitorString);
    }
}
