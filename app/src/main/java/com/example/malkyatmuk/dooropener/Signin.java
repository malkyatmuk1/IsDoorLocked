package com.example.malkyatmuk.dooropener;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;


public class Signin extends Activity {
Button btn1;
EditText username,password;
    TextView signin;
    private Socket clientSocket;
    public String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP ;
    public String send;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_log_on);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        signin=(TextView) findViewById(R.id.tv);
        btn1=(Button) findViewById(R.id.signin);

        btn1.setOnClickListener(btn);
        signin.setOnClickListener(textview);
        username.setHint("Username");
        password.setHint("Password");


    }

    View.OnClickListener textview= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),Signup.class);
            startActivity(intent);
            finish();
        }
    };

    View.OnClickListener btn = new View.OnClickListener() {
        public void onClick(final View v) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SERVER_IP = sharedPreferences.getString("ip", "") ;
                        InetAddress ip = InetAddress.getByName(SERVER_IP);
                        clientSocket = new Socket(ip, SERVERPORT);
                        send="signin "+username.getText()+" "+password.getText()+'\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();
                        if(!modifiedSentence.equals("errorsignin") && !modifiedSentence.equals("error")) {
                            Intent intent = new Intent(getApplicationContext(),Start_menu.class);
                            Global.permission=modifiedSentence.charAt(0);
                            Global.username= username.getText().toString();

                            startActivity(intent);
                            finish();
                        }

                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("Exception " + e);
                    }

                }
            }).start();
        }
    };

}

