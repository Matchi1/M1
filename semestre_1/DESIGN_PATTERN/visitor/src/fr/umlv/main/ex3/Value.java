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
    public Object accept(ExprVisitor visitor){
        return visitor.visitValue(this);
    }
}
