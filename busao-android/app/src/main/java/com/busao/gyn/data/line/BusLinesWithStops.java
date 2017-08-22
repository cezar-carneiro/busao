package com.busao.gyn.data.line;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import com.busao.gyn.data.LineStop;

import java.util.List;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

public class BusLinesWithStops {

    @Embedded
    private BusLine line;

    @Relation(
            parentColumn = "codigo",
            entityColumn = "codigoPonto",
            entity = LineStop.class)
    private List<LineStop> stops;

    public BusLinesWithStops() {

    }

    public BusLine getLine() {
        return line;
    }

    public void setLine(BusLine line) {
        this.line = line;
    }

    public List<LineStop> getStops() {
        return stops;
    }

    public void setStops(List<LineStop> stops) {
        this.stops = stops;
    }
}
