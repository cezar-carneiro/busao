package com.busao.gyn.util;

import android.support.annotation.NonNull;

/**
 * Created by cezar on 21/02/17.
 */

public class BusStopUtils {

    private static final int MIN_LENGTH = 4;

    public static String formatBusStop(@NonNull Integer code){
        return formatBusStop(String.valueOf(code));
    }

    public static String formatBusStop(@NonNull String code){
        String formated = code;
        while(formated.length() < MIN_LENGTH) formated = "0" + formated;
        return formated;
    }

    public static String formatBusStop(@NonNull Integer code, @NonNull String address){
        return formatBusStop(code) + " - " + address;
    }

    public static String formatBusStop(@NonNull String code, @NonNull String address){
        return formatBusStop(code) + " - " + address;
    }

}
