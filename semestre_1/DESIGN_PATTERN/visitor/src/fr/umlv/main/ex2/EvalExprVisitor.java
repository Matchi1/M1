package fr.umlv.main.ex2;

public class EvalExprVisitor implements ExprVisitor <Integer> {
    @Override
    public Integer visitValue(Value value) {
        return value.getValue();
    }

    @Override
    public Integer visitBinOp(BinOp binOp) {
        var left = binOp.getLeft();
        var right = binOp.getRight();
        return binOp.getOperator().applyAsInt((Integer) left.accept(this),(Integer) right.accept(this));
    }
}
