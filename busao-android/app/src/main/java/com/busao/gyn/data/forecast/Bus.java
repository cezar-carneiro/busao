package com.busao.gyn.data.forecast;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */
public class Bus {

    @SerializedName("Linha")
    private String line;

    @SerializedName("Destino")
    private String destination;

    @SerializedName("Proximo")
    private BusSchedule next;

    @SerializedName("Seguinte")
    private BusSchedule following;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public BusSchedule getNext() {
        return next;
    }

    public void setNext(BusSchedule next) {
        this.next = next;
    }

    public BusSchedule getFollowing() {
        return following;
    }

    public void setFollowing(BusSchedule following) {
        this.following = following;
    }
}
