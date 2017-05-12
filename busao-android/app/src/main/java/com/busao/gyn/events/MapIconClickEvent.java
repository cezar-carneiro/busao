package com.busao.gyn.events;

import com.busao.gyn.stops.BusStop;

/**
 * Created by cezar.carneiro on 11/05/2017.
 */

public class MapIconClickEvent {

    private BusStop stop;

    public MapIconClickEvent(BusStop stop) {
        this.stop = stop;
    }

    public BusStop getStop() {
        return stop;
    }
}
