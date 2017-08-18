package com.busao.gyn.events;

import com.busao.gyn.data.stop.BusStop;
import com.busao.gyn.data.stop.BusStopWithLines;

/**
 * Created by cezar.carneiro on 11/05/2017.
 */

public class MapIconClickEvent {

    private BusStopWithLines stop;

    public MapIconClickEvent(BusStopWithLines stop) {
        this.stop = stop;
    }

    public BusStopWithLines getStop() {
        return stop;
    }
}
