package fr.umlv.main.ex2;

public class ToStringVisitor implements ExprVisitor <Void> {
    private final StringBuilder context = new StringBuilder();

    @Override
    public Void visitBinOp(BinOp binOp) {
        var left = binOp.getLeft();
        var right = binOp.getRight();

        context.append("(");
        left.accept(this);
        context.append(' ');
        context.append(binOp.getOpName());
        context.append(' ');
        right.accept(this);
        context.append(")");
        return null;
    }

    @Override
    public Void visitValue(Value value) {
        context.append(value.getValue());
        return null;
    }

    @Override
    public String toString() {
        return context.toString();
    }
}
