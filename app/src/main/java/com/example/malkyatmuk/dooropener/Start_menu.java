package com.example.malkyatmuk.dooropener;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Start_menu extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ListView LV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        //Intent i= new Intent(this, Services.class);
       // i.putExtra("notification", "Value to be used by the service");
        //this.startService(i);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View txtusern=(View) navigationView.getHeaderView(0);
        TextView txt=txtusern.findViewById(R.id.username);
        txt.setText(Global.username.toString());
        navigationView.setNavigationItemSelectedListener(this);
        displaySelectedScreen(R.id.nav_menu1);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displaySelectedScreen(item.getItemId());
        return true;
    }
    private void displaySelectedScreen(int itemId) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_menu1:

                fragment = new Profil();
                break;
            case R.id.nav_menu2:
                if(Global.permission!='d')
                    fragment = new Client_Door();
                else {Toast toast=Toast.makeText(this,"You don't have permissions!",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
                    toast.show();}
                break;
            case R.id.nav_admin:
                if(Global.permission=='a')
                    fragment = new Client_List();
                else {Toast toast=Toast.makeText(this,"You don't have permissions!",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 0);
                toast.show();}
                break;
            case R.id.nav_signout:
                Intent intent = new Intent();
                intent.setClassName("com.example.malkyatmuk.dooropener","com.example.malkyatmuk.dooropener.Signin");
                startActivity(intent);
                finish();
                break;
            case R.id.nav_settings:
                fragment=new Settings();
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}