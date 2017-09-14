package com.busao.gyn.events;

import com.busao.gyn.data.forecast.Forecast;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */

public class ForecastFetchedEvent {
    private Forecast forecast;

    public ForecastFetchedEvent(Forecast forecast) {
        this.forecast = forecast;
    }

    public Forecast getForecast() {
        return forecast;
    }
}
