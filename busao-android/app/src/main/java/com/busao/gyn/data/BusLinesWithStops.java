package com.busao.gyn.data;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;

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
            entity = BusLine.class,
            projection = {"codigoPonto"})
    private List<LineStop> stops;

    public BusLinesWithStops() {

    }
}
