package com.example.malkyatmuk.dooropener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class FirstFirstPage extends AppCompatActivity {

    int TIME_OUT=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_first_page);

        TextView txv=(TextView) findViewById(R.id.tittle);
        TextView txv1=(TextView) findViewById(R.id.text);
        Typeface custom_font=Typeface.createFromAsset(getAssets(), "fonts/abc.ttf");
        txv1.setTypeface(custom_font,Typeface.BOLD);
        txv.setTypeface(custom_font,Typeface.BOLD);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(FirstFirstPage.this, FirstPage.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Global.ip = sharedPreferences.getString("ip", "");


    }
}
