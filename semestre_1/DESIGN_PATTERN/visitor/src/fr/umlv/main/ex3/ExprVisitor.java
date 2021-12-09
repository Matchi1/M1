package fr.umlv.main.ex3;

public class ExprVisitor <T> {
    T visitValue(Value value);
    T visitBinOp(BinOp binOp);
}
