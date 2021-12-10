package fr.umlv.main.ex3;

import java.util.HashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ExprVisitor <T, C> {
    // private final HashMap<Class<? extends Expr>, Function<Expr, T>> actions = new HashMap<>();
    private final HashMap<Class<? extends Expr>, BiFunction<Expr, C, T>> actions = new HashMap<>();

    //public ExprVisitor<T, C> when(Class<? extends Expr> exprClass,  Function<Expr, T> function) {
    public ExprVisitor<T, C> when(Class<? extends Expr> exprClass,  BiFunction<Expr, C, T> function) {
        actions.putIfAbsent(exprClass, function);
        return this;
    }

    public T visit(Expr expression, C context) {
        // return actions.get(expression.getClass()).apply(expression);
        return actions.get(expression.getClass()).apply(expression, context);
    }
}
