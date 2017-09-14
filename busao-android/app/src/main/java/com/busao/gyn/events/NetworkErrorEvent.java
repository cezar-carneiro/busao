package com.busao.gyn.events;

import java.io.IOException;

/**
 * Created by cezar.carneiro on 14/09/2017.
 */

public class NetworkErrorEvent {

    private IOException exception;

    public NetworkErrorEvent(IOException exception) {
        this.exception = exception;
    }

    public IOException getException() {
        return exception;
    }
}
