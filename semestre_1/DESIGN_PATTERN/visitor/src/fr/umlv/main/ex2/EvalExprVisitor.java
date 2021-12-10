package fr.umlv.main.ex2;

public class EvalExprVisitor implements ExprVisitor <Integer, Void> {
    @Override
    public Integer visitValue(Value value, Void context) {
        return value.getValue();
    }

    @Override
    public Integer visitBinOp(BinOp binOp, Void context) {
        var left = binOp.getLeft();
        var right = binOp.getRight();
        return binOp.getOperator().applyAsInt((Integer) left.accept(this, context),(Integer) right.accept(this, context));
    }
}
