package com.busao.gyn.data.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */

public class Forecast {

    @SerializedName("status")
    private String status;

    @SerializedName("mensagem")
    private String message;

    @SerializedName("data")
    private List<Bus> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Bus> getData() {
        return data;
    }

    public void setData(List<Bus> data) {
        this.data = data;
    }
}
