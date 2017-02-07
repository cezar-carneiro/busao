package com.busao.gyn.util;

import android.graphics.PointF;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by cezar on 23/01/17.
 */

public class GeometryUtils {

    public static final double EARTH_RADIUS = 6371000;

    /**
     * Returns a square around the 'center' point.
     * @param center
     * @param range in meters
     * @return
     */
    public static LatLng[] areaNearPosition(LatLng center, double range){
        LatLng[] cordinates = new LatLng[4];
        cordinates[0] = calculateDerivedPosition(center, range, 0);
        cordinates[1] = calculateDerivedPosition(center, range, 90);
        cordinates[2] = calculateDerivedPosition(center, range, 180);
        cordinates[3] = calculateDerivedPosition(center, range, 270);
        return cordinates;
    }

    /**
     * @param center
     * @param range in meters
     * @param bearing in degrees
     * @return
     */
    public static LatLng calculateDerivedPosition(LatLng center, double range, double bearing){
        double latA = Math.toRadians(center.latitude);
        double lonA = Math.toRadians(center.longitude);
        double angularDistance = range / EARTH_RADIUS;
        double trueCourse = Math.toRadians(bearing);

        double lat = Math.asin(
                Math.sin(latA) * Math.cos(angularDistance) +
                        Math.cos(latA) * Math.sin(angularDistance)
                                * Math.cos(trueCourse));

        double dlon = Math.atan2(
                Math.sin(trueCourse) * Math.sin(angularDistance)
                        * Math.cos(latA),
                Math.cos(angularDistance) - Math.sin(latA) * Math.sin(lat));

        double lon = ((lonA + dlon + Math.PI) % (Math.PI * 2)) - Math.PI;

        lat = Math.toDegrees(lat);
        lon = Math.toDegrees(lon);

        LatLng newPoint = new LatLng( lat,  lon);

        return newPoint;
    }
}
