package com.busao.gyn.events;

import com.busao.gyn.data.line.BusLine;

/**
 * Created by cezar.carneiro on 17/08/2017.
 */

public class BusLineChanged {

    private BusLine line;

    public BusLineChanged(BusLine line) {
        this.line = line;
    }

    public BusLine getLine() {
        return line;
    }
}
