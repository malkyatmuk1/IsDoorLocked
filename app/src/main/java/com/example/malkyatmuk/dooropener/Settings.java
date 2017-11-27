package com.example.malkyatmuk.dooropener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
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

/**
 * Created by malkyatmuk on 10/14/17.
 */

public class Settings extends Fragment {

    Button iamathome,meters;
    LocationManager locationManager;
    EditText metersedit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        iamathome=(Button) myFragmentView.findViewById(R.id.iamathome);
        meters=(Button) myFragmentView.findViewById(R.id.meters);
        metersedit =(EditText) myFragmentView.findViewById(R.id.metersedit);

        iamathome.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    // return;
                }
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);


                Global.latitudeHome = location.getLatitude();
                Global.longetudeHome = location.getLongitude();
                getActivity().stopService(new Intent(getContext(),Services.class));
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
}
