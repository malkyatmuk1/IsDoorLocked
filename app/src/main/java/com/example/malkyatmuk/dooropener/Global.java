package com.example.malkyatmuk.dooropener;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.location.GeofencingClient;

import java.util.ArrayList;

/**
 * Created by malkyatmuk on 10/22/17.
 */

public class Global extends Application {

    public static String username="";
    public static String password="";
    public static char permission;
    public static ArrayList<String> usernames=new ArrayList<String>();
    public static double longetudeHome=0;
    public static double latitudeHome=0;
    public static float meters=100;
    public static String wifiusername="";
    public static boolean flagforNotify=true;
    public static String wifiPassword="";
    public static String ip="";
    public static String directip="192.168.4.1";
    public static boolean ipsignin=false;
    public static boolean checksignin=false;
    public static boolean checkProgress=false;
    public static boolean goback;

    public static GeofencingClient mGeofencingClient;
    public static ArrayList<String> users= new ArrayList<String>();


    public String getUsername() {
        return username;
    }
    public char getPermission()
    {
        return permission;
    }

    public void setSomeVariable(String user,char perm) {
        this.username = user;
        this.permission=perm;
    }
    public static void setIP(String ip, Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ip",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("ip", ip);
        editor.apply();
    }
}