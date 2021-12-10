package fr.umlv.main.ex2;

public interface ExprVisitor <T, C> {
    T visitValue(Value value, C context);
    T visitBinOp(BinOp binOp, C context);
}
