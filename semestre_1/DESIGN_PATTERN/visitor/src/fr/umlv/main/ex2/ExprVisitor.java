package fr.umlv.main.ex2;

public interface ExprVisitor <T> {
    T visitValue(Value value);
    T visitBinOp(BinOp binOp);
}
