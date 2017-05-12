package com.busao.gyn.events;

/**
 * Created by cezar.carneiro on 12/05/2017.
 */

public class DataChangeEvent<T> {

    private T item;

    public DataChangeEvent(T item) {
        this.item = item;
    }

    public T getItem() {
        return item;
    }
}
