package com.example.malkyatmuk.dooropener;

/**
 * Created by malkyatmuk on 11/27/17.
 */

public class Distance {
   private double lat1,  long1, lat2,long2;
   private double distance;
    public Distance(double lat1, double long1, double lat2, double long2)
    {
        this.lat1=lat1;
        this.long1=long1;
        this.lat2=lat2;
        this.long2=long2;

    }

    public double DegToRad(double degrees)
    {
        return degrees*Math.PI/180.0;
    }
    public double Expression(double lat1,double long1,double lat2, double long2)
    {
        double a=Math.pow(Math.sin((lat2-lat1)/2),2)+Math.cos(lat1)*Math.cos(lat2)*Math.pow(Math.sin((long2-long1)/2),2);
        return Math.sqrt(a);

    }
    public double getDistance(double expression,double r)
    {
        return 2*r*Math.asin(expression);
    }

}

