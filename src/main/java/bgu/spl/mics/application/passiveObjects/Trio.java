package bgu.spl.mics.application.passiveObjects;

public class Trio<A, B, C> extends Pair<A, B> {

    private A first;
    private B second;
    private C third;

    public Trio(A first, B second, C third) {
        super(first, second);
        this.third = third;
    }

    public C getThird() {
        return third;
    }
}