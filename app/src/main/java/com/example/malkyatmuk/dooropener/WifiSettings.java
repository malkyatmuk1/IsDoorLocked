package com.example.malkyatmuk.dooropener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class WifiSettings extends Activity {
    Button apply;
    EditText wifiusername,wifipassword,username,password;
    TextView goback;

    private Socket clientSocket;
    public static String modifiedSentence;
    private static final int SERVERPORT = 3030;
    private String send;
    Thread trd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifisettings);

        Toast toast=Toast.makeText(getApplicationContext(),"You should be connected to isdoor WiFi!",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
        toast.show();
        apply=(Button) findViewById(R.id.apply);
        apply.setOnClickListener(applylistener);

        goback=(TextView) findViewById(R.id.goback);
        goback.setOnClickListener(gobacklistener);

        wifipassword=(EditText) findViewById(R.id.wifipass);
        wifiusername=(EditText) findViewById(R.id.wifissid);
        username=(EditText) findViewById(R.id.username);
        password=(EditText) findViewById(R.id.password);
        if(Global.wifiPassword!="") wifipassword.setText(Global.wifiPassword);
        if(Global.wifiusername!="") wifiusername.setText(Global.wifiusername);
    }
    View.OnClickListener gobacklistener=new View.OnClickListener() {

        public void onClick(View view) {
            if(Global.goback) {
                Intent intent = new Intent(view.getContext(), Signin.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Intent intent = new Intent(view.getContext(), Signup.class);
                startActivity(intent);
                finish();
            }
        }
    };

    View.OnClickListener applylistener=new View.OnClickListener() {

        public void onClick(final View view) {
            Global.wifiusername=wifiusername.getText().toString();
            Global.wifiPassword=wifipassword.getText().toString();


      trd= new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        clientSocket = new Socket("192.168.4.1", SERVERPORT);
                        send="setWifi "+wifiusername.getText().toString()+" "+wifipassword.getText().toString()+" "+username.getText().toString()+" "+password.getText().toString()+'\n';

                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(send);
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();


                        clientSocket.close();
                    } catch (IOException e) {
                        System.out.println("Exception " + e);
                    }

                }
            });
      trd.start();
            try {
                trd.join(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(modifiedSentence==null) modifiedSentence="";
            else if(!modifiedSentence.equals("false") && !modifiedSentence.equals("error")) {
                Toast toast=Toast.makeText(view.getContext(),"The wifi was set",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
                toast.show();

            }
            else
            {
                Toast toast=Toast.makeText(view.getContext(),"The wifi is not set",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
    };

}
//JohnAndWillow
//IamSuperProgrammer

