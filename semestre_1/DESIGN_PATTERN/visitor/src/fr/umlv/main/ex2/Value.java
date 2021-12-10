package fr.umlv.main.ex2;

public class Value implements Expr {
    private final int value;

    public Value(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public <T, C> T accept(ExprVisitor<T, C> visitor, C context){
        return visitor.visitValue(this, context);
    }
}
