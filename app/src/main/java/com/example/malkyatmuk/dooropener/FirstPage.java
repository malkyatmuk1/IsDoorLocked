package com.example.malkyatmuk.dooropener;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FirstPage extends AppCompatActivity {

    Button signin,signup;
    TextView getstart,createorlog,isdoorlocked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);


        signin=(Button) findViewById(R.id.signin);
        signin.setOnClickListener(signInListener);
        signup=(Button) findViewById(R.id.signup);
        signup.setOnClickListener(signUpListener);
        isdoorlocked=(TextView) findViewById(R.id.isdoorlocked);
        getstart=(TextView) findViewById(R.id.getstarted);
        createorlog=(TextView) findViewById(R.id.logorcreate);

        Typeface custom_font=Typeface.createFromAsset(getAssets(), "fonts/abc.ttf");

        signin.setTypeface(custom_font,Typeface.BOLD);
        signin.setTypeface(custom_font, Typeface.BOLD);
        isdoorlocked.setTypeface(custom_font,Typeface.BOLD);

        getstart.setTypeface(custom_font,Typeface.BOLD);
        createorlog.setTypeface(custom_font);
        SharedPreferences settings = getSharedPreferences("ip", Context.MODE_PRIVATE);
        String ip = settings.getString("ip", "");
        Global.ip=ip;
    }

   View.OnClickListener signInListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), Signin.class);
            startActivity(intent);
            finish();
        }
    };

    View.OnClickListener signUpListener=new View.OnClickListener() {

        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(),Signup.class);
            startActivity(intent);
            finish();
        }
    };

}
