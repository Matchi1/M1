package fr.umlv.main.ex3;

public class Value implements Expr {
    private final int value;

    public Value(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    // public <T> T accept(ExprVisitor<T> visitor){
    public <T, C> T accept(ExprVisitor<T, C> visitor, C context){
        // return visitor.visit(this);
        return visitor.visit(this, context);
    }
}
