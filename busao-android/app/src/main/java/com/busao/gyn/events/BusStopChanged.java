package com.busao.gyn.events;

import com.busao.gyn.stops.BusStop;

/**
 * Created by cezar.carneiro on 12/05/2017.
 */

public class BusStopChanged {

    private BusStop stop;

    public BusStopChanged(BusStop stop) {
        this.stop = stop;
    }

    public BusStop getStop() {
        return stop;
    }
}
