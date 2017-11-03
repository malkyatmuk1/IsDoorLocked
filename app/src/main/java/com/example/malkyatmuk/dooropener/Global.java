package com.example.malkyatmuk.dooropener;

import android.app.Application;

/**
 * Created by malkyatmuk on 10/22/17.
 */

public class Global extends Application {

    public static String username;
    public static char permission;
    public static String[] usernamees=new String[10];
    public static double longetudeHome;
    public static double latitudeHome;
    public static double meters=100;

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
}