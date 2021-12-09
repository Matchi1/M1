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
    public <T> T accept(ExprVisitor visitor){
        return visitor.visitValue(this);
    }
}
