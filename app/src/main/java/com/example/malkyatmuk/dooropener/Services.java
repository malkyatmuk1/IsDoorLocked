package com.example.malkyatmuk.dooropener;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by malkyatmuk on 10/27/17.
 */

public class Services extends Service {

    double latitude, longitude;
    private TextView t;
    private LocationManager locationManager;
    private LocationListener locationListener;Location location;


    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                 Distence distence=new Distence(latitude,longitude,Global.latitudeHome,Global.longetudeHome);

                double dis = distence.getDistance(distence.Expression(distence.DigToRad(latitude),distence.DigToRad(longitude),distence.DigToRad(Global.latitudeHome),distence.DigToRad(Global.longetudeHome)),6371.0);
                if (dis >= Global.meters) {
                    sendNotification();
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        return Services.START_REDELIVER_INTENT;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static double distFrom(double lat1, double lon1, double lat2, double lon2) {
        Double earthRadiusKm = 6371000.0;

        Double dLat = deg2rad(lat2-lat1);
        Double dLon = deg2rad(lon2-lon1);

        lat1 = deg2rad(lat1);
        lat2 = deg2rad(lat2);

        Double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
    public void sendNotification()
    {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle("IsDoorLocked")
                        .setContentText("Don't forget to lock your door!");
        NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(001, mBuilder.build());
    }
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
	/*::	This function converts radians to decimal degrees						 :*/
	/*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    private void sendBroadcastMessage(double latitude,double longitude,double dis) {

            Intent intent = new Intent("ACTION_LOCATION_BROADCAST");
            intent.putExtra("EXTRA_LATITUDE", latitude);
            intent.putExtra("EXTRA_LONGITUDE", longitude);
            intent.putExtra("DIS", dis);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }
    public  void loc(Location location) {

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double dis = distFrom(latitude, longitude, Global.latitudeHome, Global.longetudeHome);
        Log.d("CREATION", String.valueOf(dis));
        sendBroadcastMessage(latitude,longitude,dis);
        if (dis >= Global.meters) {

            sendNotification();
        }
    }

}
