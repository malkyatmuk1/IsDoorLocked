package com.example.malkyatmuk.dooropener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;


public class Signin extends Activity implements GoogleApiClient.ConnectionCallbacks,
                GoogleApiClient.OnConnectionFailedListener {
    Button btn1;
    AutoCompleteTextView username;
    EditText password;
    TextView signup, ip, settings;
    private Socket clientSocket;
    public String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP;
    public String send;
    CheckBox check;
    Thread thr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_log_on);
        Global.ipsignin = true;
        username = (AutoCompleteTextView) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Global.users);
        username.setThreshold(1);
        username.setAdapter(adapter);
        signup = (TextView) findViewById(R.id.tv);
        ip = (TextView) findViewById(R.id.ip);
        btn1 = (Button) findViewById(R.id.signin);
        settings = (TextView) findViewById(R.id.wifiset);
        check = (CheckBox) findViewById(R.id.check);
        btn1.setOnClickListener(btn);
        settings.setOnClickListener(settingslistener);

        signup.setOnClickListener(textview);
        ip.setOnClickListener(iplistener);
        if (Global.username.isEmpty() && Global.password.isEmpty()) {
            username.setHint("Username");
            password.setHint("Password");
        } else {
            username.setText(Global.username);
            password.setText(Global.password);
        }


    }

    View.OnClickListener textview = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), Signup.class);
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener iplistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), IP.class);
            Global.ipsignin = true;
            startActivity(intent);
            finish();
        }
    };
    View.OnClickListener settingslistener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), WifiSettings.class);
            Global.goback = true;
            startActivity(intent);

        }
    };

    View.OnClickListener btn = new View.OnClickListener() {
        public void onClick(final View v) {
            Global.username = username.getText().toString();
            Global.password = password.getText().toString();
            if(!Global.users.contains(username.getText().toString()))Global.users.add(username.getText().toString());
            if (check.isChecked()) {
                Global.setIP(Global.directip, getApplicationContext());
            } else {
                if (Global.ip.isEmpty()) {
                    Intent intent = new Intent(getApplicationContext(), IP.class);
                    startActivity(intent);
                    finish();
                }
                Global.setIP(Global.ip, getApplicationContext());
            }

            thr = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SERVER_IP = sharedPreferences.getString("ip", "");
                        InetAddress ip = InetAddress.getByName(SERVER_IP);
                        clientSocket = new Socket(ip, SERVERPORT);
                        send = "signin " + username.getText() + " " + password.getText() + '\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();
                        if (!modifiedSentence.equals("errorsignin") && !modifiedSentence.equals("error")) {
                            Intent intent = new Intent(getApplicationContext(), Start_menu.class);
                            Global.permission = modifiedSentence.charAt(0);
                            Global.username = username.getText().toString();
                            Global.password = password.getText().toString();


                            if (Global.longetudeHome != 0 && Global.latitudeHome != 0) {
                                startService(new Intent(getApplicationContext(), Services.class));
                            }


                            startActivity(intent);
                            finish();
                        }

                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("Exception " + e);
                    }
                    return;
                }
            });
            thr.start();

        thr.interrupt();
        }

    };



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}


