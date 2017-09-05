package com.busao.gyn.events;

import com.busao.gyn.data.line.BusLinesWithStops;

/**
 * Created by cezar.carneiro on 05/09/2017.
 */

public class LineMapIconClickEvent {

    private BusLinesWithStops line;

    public LineMapIconClickEvent(BusLinesWithStops line) {
        this.line = line;
    }

    public BusLinesWithStops getLine() {
        return line;
    }
}
