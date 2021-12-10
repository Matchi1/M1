package fr.umlv.main.ex2;

public class ToStringVisitor implements ExprVisitor <Void, StringBuilder> {
    @Override
    public Void visitBinOp(BinOp binOp, StringBuilder context) {
        var left = binOp.getLeft();
        var right = binOp.getRight();

        context.append("(");
        left.accept(this, context);
        context.append(' ');
        context.append(binOp.getOpName());
        context.append(' ');
        right.accept(this, context);
        context.append(")");
        return null;
    }

    @Override
    public Void visitValue(Value value, StringBuilder context) {
        context.append(value.getValue());
        return null;
    }
}
