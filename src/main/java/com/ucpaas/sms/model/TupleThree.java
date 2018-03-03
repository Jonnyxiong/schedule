package com.ucpaas.sms.model;

public class TupleThree<A, B, C> extends TupleTwo{
    public final C third;
    public TupleThree(A a, B b, C c) {
        super(a, b);
        third = c;
    }

    @Override
    public String toString() {
        return "TupleThree{" +
                "third=" + third +
                ", first=" + first +
                ", second=" + second +
                '}';
    }
}
