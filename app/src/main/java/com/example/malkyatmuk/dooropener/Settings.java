package com.example.malkyatmuk.dooropener;


import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.Socket;

/**
 * Created by malkyatmuk on 25.10.17.
 */

public class Settings extends Fragment  implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener{
    Button iamathome, wifi, meters;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP;
    private Socket clientSocket;
    String modifiedSentence;
    TextView cord;
    LocationManager locationManager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);


        final EditText meter = (EditText) myFragmentView.findViewById(R.id.metersedit);
        iamathome = (Button) myFragmentView.findViewById(R.id.iamathome);
        cord=(TextView) myFragmentView.findViewById(R.id.cordinates);
        GeofencingRequest request = new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .build();

        iamathome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Geofence geofence = new Geofence.Builder()
                        .setRequestId(requestId)
                        .setCircularRegion(latitude, longitude, radius)
                        .setExpirationDuration(NEVER_EXPIRE)
                        .build();
         }
        });
        /*
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        double latitude = intent.getDoubleExtra("EXTRA_LATITUDE", 0);
                        double longitude = intent.getDoubleExtra("EXTRA_LONGITUDE", 0);
                        double dis=intent.getDoubleExtra("DIS", 0);
                        cord.setText("Lat: " + latitude + ", Lng: " + longitude+", Dis: "+dis);
                    }
                }, new IntentFilter("ACTION_LOCATION_BROADCAST")
        );
        */


        meters = (Button) myFragmentView.findViewById(R.id.meters);
        meters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(meter.getText().toString()!=null)
                Global.meters= Double.parseDouble(meter.getText().toString());
            }
        });



        return myFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Settings");
    }


}
