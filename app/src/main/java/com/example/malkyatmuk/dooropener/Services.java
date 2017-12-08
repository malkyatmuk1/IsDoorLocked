package com.example.malkyatmuk.dooropener;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.TextView;

/**
 * Created by malkyatmuk on 10/27/17.
 */

public class Services extends Service {

    double latitude, longitude;
    private LocationManager locationManager;
    private LocationListener locationListener;
    
    @Override

    public int onStartCommand(Intent intent, int flags, int startId) {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                 Distence distence=new Distence(latitude,longitude,Global.latitudeHome,Global.longetudeHome);

                double dis = distence.getDistance(distence.Expression(distence.DegToRad(latitude),distence.DegToRad(longitude),distence.DegToRad(Global.latitudeHome),distence.DegToRad(Global.longetudeHome)),6371.0);
                //if the flag is true, the notification should be send
                dis=dis*1000;
                if (dis >= Global.meters) {
                    if(Global.flagforNotify){sendNotification();Global.flagforNotify=false;}
                }
                if(dis*2<=Global.meters) Global.flagforNotify=true;

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {
                Intent i = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);

        return Services.START_REDELIVER_INTENT;
    }

    @org.jetbrains.annotations.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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

    private void sendBroadcastMessage(double latitude,double longitude,double dis) {

            Intent intent = new Intent("ACTION_LOCATION_BROADCAST");
            intent.putExtra("EXTRA_LATITUDE", latitude);
            intent.putExtra("EXTRA_LONGITUDE", longitude);
            intent.putExtra("DIS", dis);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

    }


}
