package com.example.malkyatmuk.dooropener;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by malkyatmuk on 24.11.17.
 */

public class Constants {

    public static final String GEOFENCE_ID_HOME = "Home";
    public static final float GEOFENCE_RADIUS_IN_METERS = 100;
    public static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS=20000;

    /**
     * Map for storing information about home.
     */
    public static final HashMap<String, LatLng> AREA_LANDMARKS = new HashMap<String, LatLng>();

    static {

        AREA_LANDMARKS.put(GEOFENCE_ID_HOME, new LatLng(Global.latitudeHome,Global.longetudeHome));
    }
}
