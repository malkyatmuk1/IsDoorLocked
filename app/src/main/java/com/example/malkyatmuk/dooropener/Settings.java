package com.example.malkyatmuk.dooropener;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.LocationResult;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by malkyatmuk on 10/14/17.
 */

public class Settings extends Fragment implements LocationListener {

    Button iamathome,meters;
    LocationManager locationManager;
    EditText metersedit;
    private Button b, b2;
    private TextView t;
    private LocationListener listener;
    double myHomeLatitude, getMyHomeLongitude;
    double latitude, longitude;
    Location location;
    LocationResult locationResult;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        iamathome=(Button) myFragmentView.findViewById(R.id.iamathome);
        meters=(Button) myFragmentView.findViewById(R.id.meters);
        metersedit =(EditText) myFragmentView.findViewById(R.id.metersedit);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


        iamathome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);

                    }
                    location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                Global.latitudeHome = location.getLatitude();
                Global.longetudeHome = location.getLongitude();
                Global.flagforNotify=true;
                getActivity().startService(new Intent(getContext(),Services.class));
            }
        });
        meters.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Global.meters = Integer.parseInt(metersedit.getText().toString());
            }
        });
        return myFragmentView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Settings");
    }


    @Override
    public void onLocationChanged(final Location location) {

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
}
