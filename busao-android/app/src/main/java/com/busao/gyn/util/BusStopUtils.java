package com.busao.gyn.util;

/**
 * Created by cezar on 21/02/17.
 */

public class BusStopUtils {

    private static final int MIN_LENGTH = 4;

    public static String formatBusStop(Integer code){
        return formatBusStop(String.valueOf(code));
    }

    public static String formatBusStop(String code){
        while(code.length() < MIN_LENGTH) code = "0" + code;
        return code;
    }

    public static String formatBusStop(Integer code, String address){
        return formatBusStop(code) + " - " + address;
    }

    public static String formatBusStop(String code, String address){
        return formatBusStop(code) + " - " + address;
    }

}
