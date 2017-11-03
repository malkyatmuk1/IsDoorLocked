package com.example.malkyatmuk.dooropener;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.net.InetAddress;
import java.net.Socket;
import java.util.regex.Matcher;

public class Signup extends Activity {
    EditText pass,pass2,username,ip;


    Button btns;
    String txt;
    String txt2;
    View v;
    Toast toast;
    TextView welcome, sign,textView;
    private Socket socket;
    private Socket clientSocket;
    String modifiedSentence;

    private static final int SERVERPORT = 3030;
    private static String SERVER_IP;
    public static SharedPreferences settings;
    static SharedPreferences.Editor editor;

    private Matcher matcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pass = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);
        username = (EditText) findViewById(R.id.username);
        ip=(EditText) findViewById(R.id.ip);


        btns = (Button) findViewById(R.id.btnsignup);
        btns.setOnClickListener(btn);
        welcome = (TextView) findViewById(R.id.welcome);
        sign = (TextView) findViewById(R.id.sign);
        textView=(TextView) findViewById(R.id.tv);

        pass2.addTextChangedListener(textWatcher);
        pass.addTextChangedListener(textWatcher2);
        textView.setOnClickListener(signin);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/abc.ttf");

        welcome.setTypeface(custom_font, Typeface.BOLD);
        sign.setTypeface(custom_font, Typeface.BOLD);
        username.setTypeface(custom_font);
        pass.setTypeface(custom_font);
        pass2.setTypeface(custom_font);


    }
    View.OnClickListener signin= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),Signin.class);
            startActivity(intent);
            finish();
        }
    };


    TextWatcher textWatcher2 = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            txt = pass.getText().toString();
            txt2 = pass2.getText().toString();

            if (txt2.equals(txt)) {
                pass.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }

            if (pass.length() < 5)
                toast = Toast.makeText(getBaseContext(), "Your password is too short!", Toast.LENGTH_SHORT);

            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();
            pass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        }
    };
    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            txt = pass.getText().toString();
            txt2 = pass2.getText().toString();


            if (!txt2.equals(txt)) {
                pass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            }

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            txt = pass.getText().toString();
            txt2 = pass2.getText().toString();
            if (txt2.equals(txt)) {
                pass.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
            }


            if (!txt2.equals(txt)) {
                Toast.makeText(getBaseContext(), "The two passwords don't match!", Toast.LENGTH_SHORT).show();
                pass2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            }
        }


    };
    View.OnClickListener btn = new View.OnClickListener() {
        public void onClick( View v) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("ip", ip.getText().toString());
            editor.commit();
            //commits your edits
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                       SERVER_IP = sharedPreferences.getString("ip", "") ;

                        InetAddress ip = InetAddress.getByName(SERVER_IP);
                        clientSocket = new Socket(ip, SERVERPORT);


                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(String.format("signup %s %s \n",username.getText(),pass.getText()));
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();
                        if (modifiedSentence.equals("truesignup"))
                        {
                            Intent intent = new Intent(getApplicationContext(), Signin.class);
                            startActivity(intent);
                            finish();
                        }


                        else {



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