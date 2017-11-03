package com.example.malkyatmuk.dooropener;


import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by malkyatmuk on 25.10.17.
 */

public class Settings extends Fragment  {
    Button iamathome,wifi,meters;
    private static final int SERVERPORT = 3030;
    private static  String SERVER_IP ;
    private Socket clientSocket;
    String modifiedSentence;
    LocationManager locationManager;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        iamathome = (Button) myFragmentView.findViewById(R.id.iamathome);
        final EditText wifissid=(EditText) myFragmentView.findViewById(R.id.wifissid);
        final EditText wifipassword=(EditText) myFragmentView.findViewById(R.id.wifipassword);
        final EditText meter=(EditText) myFragmentView.findViewById(R.id.meters);
        iamathome.setOnClickListener(new View.OnClickListener() {

         @Override
         public void onClick(View v) {
             locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
             if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                 // TODO: Consider calling
                 //    ActivityCompat#requestPermissions
                 // here to request the missing permissions, and then overriding
                 //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                 //                                          int[] grantResults)
                 // to handle the case where the user grants the permission. See the documentation
                 // for ActivityCompat#requestPermissions for more details.
                 return;
             }
             Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

             Global.latitudeHome = location.getLatitude();
             Global.longetudeHome = location.getLongitude();
         }
        });

        wifi = (Button) myFragmentView.findViewById(R.id.wifi);
        wifi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                            SERVER_IP = pref.getString("ip", "") ;
                            InetAddress ip = InetAddress.getByName(SERVER_IP);
                            clientSocket = new Socket(ip, SERVERPORT);


                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            outToServer.writeBytes("setWifi " +wifissid.getText().toString()+" "+wifipassword.getText().toString()+'\n');
                            outToServer.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }

                    }
                }).start();
            }

        });
        meters = (Button) myFragmentView.findViewById(R.id.meters);
        meters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
