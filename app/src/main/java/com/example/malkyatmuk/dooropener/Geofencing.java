package com.example.malkyatmuk.dooropener;

import android.content.Context;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

/**
 * Created by malkyatmuk on 11/26/17.
 */

public  class Geofencing  {
private static Context context;


    private static Geofence geofence;
Geofencing(Context context)
{
    this.context=context;
}
public void neshto()
{

}
public static void makeGeofence() {
    geofence = new Geofence.Builder()
            // Set the request ID of the geofence. This is a string to identify this
            // geofence.
            .setRequestId("home")

            .setCircularRegion(
                    Global.latitudeHome,
                    Global.longetudeHome,
                    Global.meters

            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                    Geofence.GEOFENCE_TRANSITION_EXIT)
            .build();
}
public static GeofencingRequest makeGeofenceReguest() {
            GeofencingRequest getGeofencingRequest = new GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build();
             return getGeofencingRequest;
}
}
