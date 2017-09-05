package com.busao.gyn.events;

import com.busao.gyn.data.stop.BusStopWithLines;

/**
 * Created by cezar.carneiro on 11/05/2017.
 */

public class StopMapIconClickEvent {

    private BusStopWithLines stop;

    public StopMapIconClickEvent(BusStopWithLines stop) {
        this.stop = stop;
    }

    public BusStopWithLines getStop() {
        return stop;
    }
}
