package com.example.malkyatmuk.dooropener;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class IP extends AppCompatActivity {
    EditText ip;
    Button apply;
    TextView goback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip);

        ip=(EditText) findViewById(R.id.ip);
        goback=(TextView) findViewById(R.id.goback);
        goback.setOnClickListener(gobacklistener);
        apply= (Button) findViewById(R.id.apply);
        apply.setOnClickListener(apllylistener);

    }
    View.OnClickListener gobacklistener=new View.OnClickListener() {

        public void onClick(View view) {
            if(Global.ipsignin) {
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
    View.OnClickListener apllylistener=new View.OnClickListener() {

        public void onClick(View view) {
            Global.ip=ip.getText().toString();
            Global.setIP(Global.ip,getApplicationContext());
            Toast toast=Toast.makeText(getApplicationContext(),"The IP was set",Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
            toast.show();
        }
    };
}
