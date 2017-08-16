package com.busao.gyn.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

import java.util.List;

/**
 * Created by cezar.carneiro on 16/08/2017.
 */

public class BusStopWithLines {

    @Embedded
    private BusStop stop;

    @Relation(
            parentColumn = "codigoPonto",
            entityColumn = "codigoPonto",
            entity = LineStop.class,
            projection = {"codigoLinha"})
    private List<LineStop> lines;

    public BusStopWithLines() {

    }

    public BusStop getStop() {
        return stop;
    }

    public void setStop(BusStop stop) {
        this.stop = stop;
    }

    public List<LineStop> getLines() {
        return lines;
    }

    public void setLines(List<LineStop> lines) {
        this.lines = lines;
    }
}
