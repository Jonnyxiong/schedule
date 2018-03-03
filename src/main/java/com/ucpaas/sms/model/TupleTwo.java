package com.ucpaas.sms.model;

public class TupleTwo<A, B> {
    public final A first;
    public final B second;
    public TupleTwo(A a, B b) {
        first = a;
        second = b;
    }

    @Override
    public String toString() {
        return "TupleTwo{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
