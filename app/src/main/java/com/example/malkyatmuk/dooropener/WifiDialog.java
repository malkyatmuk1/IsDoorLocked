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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
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
    public WifiManager wifiManager;
    public ProgressBar progressBar;
    Socket clientSocket;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        return  AutoOrHandDialog(getContext());

    }

     class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) { }
     }
   public void PasswordDialog(final Context context)
    {
        AlertDialog.Builder builderPass = new AlertDialog.Builder(context);
        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builderPass .setTitle("Password for "+ networkSSID)
                .setView(input)
                .setPositiveButton(R.string.connectButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String pass;
                        networkPass=input.getText().toString();
                        if(networkPass.isEmpty()) networkPass="";
                        conf = new WifiConfiguration();
                        conf.SSID = "\"" + networkSSID + "\"";
                        conf.preSharedKey = "\""+ networkPass +"\"";
                        wifiManager = (WifiManager)context.getSystemService(WIFI_SERVICE);
                        wifiManager.setWifiEnabled(true);
                        Boolean flag=false;
                        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
                        for( WifiConfiguration i : list ) {
                            if (i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                                flag=false;
                                wifiManager.disconnect();
                                wifiManager.enableNetwork(i.networkId, true);
                                wifiManager.reconnect();
                                break;
                            }
                            else if(i.SSID!=null)
                            {
                                flag=true;
                            }
                        }
                        if(flag)
                        {
                            wifiManager.addNetwork(conf);
                            wifiManager.disconnect();
                            wifiManager.enableNetwork(0, true);
                            wifiManager.reconnect();
                        }
                        Log.e("tukotPass","prediLong");
                        LongOperation lg=new  LongOperation(context,wifiManager);
                        Log.e("tukotPass","SledLog1");
                        lg.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                        progressBar.setVisibility(View.VISIBLE);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       networkPass="";
                    }
                }).create().show();

        Global.setIP(Global.ip,context);
    }
     Dialog AutoOrHandDialog(final Context context){
        AlertDialog.Builder builderPass = new AlertDialog.Builder(getActivity());

       TextView inputAuto = new TextView(getActivity());
        String auto= getResources().getString(R.string.auto);
        inputAuto.setText(auto);
        final TextView inputHand = new TextView(getContext());
        inputAuto.setText(getResources().getString(R.string.hand));

                builderPass .setTitle("Set IP")
                            .setMessage(getResources().getString(R.string.auto)+"\n"+getResources().getString(R.string.hand))
                            .setPositiveButton(R.string.AutoButton, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    wifiDialog(context).show();

                                }
                            })
                            .setNegativeButton(R.string.HandButton, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(context, IP.class);
                                    startActivity(intent);
                                }
                            });
        return builderPass.create();

    }
    Dialog wifiDialog(final Context context){

        final ArrayList<String> WifiArray = new ArrayList<String>();
        mainWifi = (WifiManager) context.getSystemService(WIFI_SERVICE);
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
                        PasswordDialog(context);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { }
                });
        // Create the AlertDialog object and return it
        return builderWifi.create();
    }
}
class LongOperation extends AsyncTask<String, Void, Void> {
    private static final int SERVERPORT = 3030;
    private String SERVER_IP;
    private Socket clientSocket;
    public Context mContext;
    public View mView;
    public ProgressBar progressBar;
    public WifiManager wifi;

    LongOperation (Context context,WifiManager wifiManager)
    {

        super();
        Log.e("hh","vKonstruktora");
        this.mContext=context;
        this.wifi=wifiManager;
    }
    protected Void doInBackground(String ...Params) {


                try {

                    int SERVERPORT = 3030;
                    InetAddress ip = InetAddress.getByName(Global.directip);
                    Log.e("p","tuk");
                    Thread.sleep(2000);
                    clientSocket = new Socket(ip, SERVERPORT);
                    Log.e("phg","tuk");
                    String send = "ip\n";

                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes(send);
                    outToServer.flush();
                    String modifiedSentence = inFromServer.readLine();
                    Global.ip = modifiedSentence;
                    Global.setIP(modifiedSentence,mContext);
                    wifi.disconnect();
                    clientSocket.close();
                }
                catch (IOException e) {
                    System.out.println("Exception " + e);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        return null;
    }
    protected void onPostExecute(Void result) { }
    @Override
    protected void onPreExecute() { }

    @Override
    protected void onProgressUpdate(Void... values) { }
}
