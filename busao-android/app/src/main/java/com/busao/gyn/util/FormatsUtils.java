package com.busao.gyn.util;

import android.support.annotation.NonNull;

/**
 * Created by cezar on 21/02/17.
 */

public class FormatsUtils {

    private static final int STOP_MIN_LENGTH = 4;
    private static final int LINE_MIN_LENGTH = 3;

    public static String formatBusStop(@NonNull Integer code){
        return formatNumber(String.valueOf(code), STOP_MIN_LENGTH);
    }

    public static String formatBusStop(@NonNull Integer code, @NonNull String address){
        return formatBusStop(code) + " - " + address;
    }

    public static String formatBusStop(@NonNull String code, @NonNull String address){
        return formatNumber(code, STOP_MIN_LENGTH) + " - " + address;
    }

    public static String formatBusLine(@NonNull String line){
        return formatNumber(line, LINE_MIN_LENGTH);
    }

    public static String formatBusLine(@NonNull Integer line){
        return formatNumber(String.valueOf(line), LINE_MIN_LENGTH);
    }

    public static String formatNumber(@NonNull String code, int zeros){
        String formated = code;
        while(formated.length() < zeros) formated = "0" + formated;
        return formated;
    }

}
