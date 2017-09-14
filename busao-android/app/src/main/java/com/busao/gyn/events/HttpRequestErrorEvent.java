package com.busao.gyn.events;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */

public class HttpRequestErrorEvent {

    private Integer code;
    private String message;

    public HttpRequestErrorEvent(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
