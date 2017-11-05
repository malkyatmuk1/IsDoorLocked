package com.example.malkyatmuk.dooropener;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by malkyatmuk on 10/22/17.
 */

public class Global extends Application {

    public static String username;
    public static String password;
    public static char permission;
    public static String[] usernamees=new String[10];
    public static double longetudeHome;
    public static double latitudeHome;
    public static double meters=100;
    public static String wifiusername="";
    public static String wifiPassword="";
    public static String ip="";
    public static String directip="192.168.4.1";
    public static boolean ipsignin=false;
    public static boolean checksignin=false;
    public static boolean checksignup=false;
    public static boolean goback;

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString("ip", ip);
        editor.commit();

    }
}