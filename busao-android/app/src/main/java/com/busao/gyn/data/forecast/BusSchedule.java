package com.busao.gyn.data.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */
public class BusSchedule {

    @SerializedName("Qualidade")
    private String quality;

    @SerializedName("NumeroOnibus")
    private String busNumber;

    @SerializedName("HoraChegadaPlanejada")
    private Date plannedArrival;

    @SerializedName("HoraChegadaPrevista")
    private Date expectedArrival;

    @SerializedName("PrevisaoChegada")
    private Integer minutes;

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public Date getPlannedArrival() {
        return plannedArrival;
    }

    public void setPlannedArrival(Date plannedArrival) {
        this.plannedArrival = plannedArrival;
    }

    public Date getExpectedArrival() {
        return expectedArrival;
    }

    public void setExpectedArrival(Date expectedArrival) {
        this.expectedArrival = expectedArrival;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }
}
