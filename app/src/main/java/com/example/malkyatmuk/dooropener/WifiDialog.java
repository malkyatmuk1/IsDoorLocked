package com.example.malkyatmuk.dooropener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

/**
 * Created by malkyatmuk on 3/3/18.
 */

public class WifiDialog extends DialogFragment {


    private WifiManager mainWifi;
    private WifiReceiver receiverWifi;
    private String networkPass;
    private WifiConfiguration conf;
    private String networkSSID;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        final ArrayList<String> WifiArray = new ArrayList<String>();
        mainWifi = (WifiManager) getContext().getApplicationContext().getSystemService(WIFI_SERVICE);
        receiverWifi = new WifiReceiver();
        getContext().getApplicationContext().registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mainWifi.startScan();
        for (ScanResult wifi : mainWifi.getScanResults())
        {
            WifiArray.add(wifi.SSID);
        }



        final CharSequence[] WifiArrayChar = WifiArray.toArray(new String[WifiArray.size()]);


        AlertDialog.Builder builderWifi = new AlertDialog.Builder(getActivity());

        builderWifi .setTitle(R.string.wifi)
                .setItems(WifiArrayChar,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int selectedIndex) {
                        networkSSID = WifiArray.get(selectedIndex);
                        PasswordDialog(getContext().getApplicationContext());
                        conf = new WifiConfiguration();
                        conf.SSID = "\"" + networkSSID + "\"";
                        conf.preSharedKey = "\""+ networkPass +"\"";

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int id) {

                     }
                });
        // Create the AlertDialog object and return it
        return builderWifi.create();

    }

     class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
     }
    void PasswordDialog(final Context context)
    {
        AlertDialog.Builder builderPass = new AlertDialog.Builder(getActivity());
        final EditText input = new EditText(getContext());
        builderPass .setTitle("Password for "+ networkSSID)
                .setView(input)
                .setPositiveButton(R.string.connectButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String pass;
                        networkPass=input.getText().toString();
                        WifiManager wifiManager = (WifiManager)context.getSystemService(WIFI_SERVICE);
                        wifiManager.addNetwork(conf);
                        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                        for( WifiConfiguration i : list ) {
                            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\""))
                            {
                                wifiManager.disconnect();
                                wifiManager.enableNetwork(i.networkId, true);
                                wifiManager.reconnect();
                                break;
                            }
                        }

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       networkPass="";
                    }
                }).create().show();

    }

}

