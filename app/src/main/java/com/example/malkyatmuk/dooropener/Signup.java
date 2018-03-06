package com.example.malkyatmuk.dooropener;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Signup extends Activity {
    EditText pass,pass2;
    AutoCompleteTextView username;

    Button btnsignup;
    String txt;
    String txt2;
    TextView welcome, sign,textView,ip,settingsbutton;
    private Socket clientSocket;
    String modifiedSentence;
    CheckBox check;
    private static final int SERVERPORT = 3030;
    private static String SERVER_IP;
    public static SharedPreferences settings;
    private TextInputLayout pass1TextLayout,pass2TextLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Global.ipsignin=false;
        pass = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);

        username = (AutoCompleteTextView) findViewById(R.id.username);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,Global.users);
        username.setThreshold(1);
        username.setAdapter(adapter);

        pass1TextLayout=(TextInputLayout) findViewById(R.id.pass1TextLayout);
        pass2TextLayout=(TextInputLayout) findViewById(R.id.pass2TextLayout);


        btnsignup = (Button) findViewById(R.id.btnsignup);
        settingsbutton=(TextView) findViewById(R.id.wifiset);
        check=(CheckBox) findViewById(R.id.check);

        welcome = (TextView) findViewById(R.id.welcome);
        sign = (TextView) findViewById(R.id.sign);
        textView=(TextView) findViewById(R.id.tv);
        ip=(TextView) findViewById(R.id.ip);

        pass2.addTextChangedListener(textWatcherPassAgain);
        pass.addTextChangedListener(textWatcherPass);

        textView.setOnClickListener(signin);
        //ip.setOnClickListener(iplistener);
        settingsbutton.setOnClickListener(settingslistener);
        btnsignup.setOnClickListener(btn);

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
    /*
    View.OnClickListener iplistener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(),IP.class);
            startActivity(intent);
            finish();
        }
    };
    */
    View.OnClickListener settingslistener= new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Global.goback=false;
            Intent intent = new Intent(v.getContext(),WifiSettings.class);

            startActivity(intent);

        }
    };



    TextWatcher textWatcherPass= new TextWatcher() {

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

            if (pass.length() < 5)
            {
                pass1TextLayout.setHint("Your password is too short!");
            }
            if(pass.length()<5)
            pass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
            else {pass.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);pass1TextLayout.setHint("Password");}
            if(txt.length()==0) {
                pass1TextLayout.setHint("Password");
                pass.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }
        }
    };
    TextWatcher textWatcherPassAgain = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable arg0) {
            txt = pass.getText().toString();
            txt2 = pass2.getText().toString();

            if (!txt2.equals(txt)) {
                pass2TextLayout.setHint("The two passwords don't match!");
                pass.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            } else {
                pass2TextLayout.setHint("The two passwords match!");
                pass.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }


            if(txt2.length()==0) {
                pass2TextLayout.setHint("Rewrite your password");
                pass.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                pass2.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
            }

        }


    };
    View.OnClickListener btn = new View.OnClickListener() {
        public void onClick(View v) {
            if(!Global.users.contains(username.getText().toString()))Global.users.add(username.getText().toString());
            if (check.isChecked() ) {
                Global.setIP(Global.directip,getApplicationContext());
            }
            else {
                if(Global.ip.isEmpty())
                {
                    Intent intent = new Intent(getApplicationContext(), IP.class);
                    startActivity(intent);
                    finish();
                }
                Global.setIP(Global.ip,getApplicationContext());
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {



                        clientSocket = new Socket(Global.ip, SERVERPORT);


                        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        outToServer.writeBytes(String.format("signup %s %s \n", username.getText(), pass.getText()));
                        outToServer.flush();
                        modifiedSentence = inFromServer.readLine();
                        if (modifiedSentence.equals("truesignup")) {
                            Intent intent = new Intent(getApplicationContext(), Signin.class);
                            startActivity(intent);
                            finish();
                        } else {


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