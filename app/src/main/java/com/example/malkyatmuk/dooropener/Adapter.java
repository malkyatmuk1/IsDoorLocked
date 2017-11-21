package com.example.malkyatmuk.dooropener;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by malkyatmuk on 10/24/17.
 */


public class Adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> mList;
    private static final int SERVERPORT = 3030;
    private static  String SERVER_IP ;
    private Socket clientSocket;
    Thread thrSwitch,thrButton;
    String[] spliter;

    public Adapter(Context context,ArrayList<String> list){
        mContext=context;
        mList=list;
    }


    @Override
    public int getCount() {
     return    mList.size();
    }

    @Override
    public Object getItem(int position) {
       return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        //use convertView recycle
        if(convertView==null){
            holder=new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder.textView= (TextView) convertView.findViewById(R.id.name);
           holder.DelButton= (Button) convertView.findViewById(R.id.delete);
            holder.switchP=(Switch) convertView.findViewById(R.id.switchPerm);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.DelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                thrButton = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                        try {
                            SERVER_IP = pref.getString("ip", "");
                            InetAddress ip = InetAddress.getByName(SERVER_IP);
                            clientSocket = new Socket(ip, SERVERPORT);

                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            outToServer.writeBytes("del " +holder.textView.getText().toString() +'\n');
                            outToServer.flush();
                            mList.remove(position);

                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }
                    }
                });
                thrButton.start();

                try {
                    thrButton.join(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });
        holder.switchP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         final boolean isChecked) {

                thrSwitch = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                        try {
                            SERVER_IP = pref.getString("ip", "");
                            InetAddress ip = InetAddress.getByName(SERVER_IP);
                            clientSocket = new Socket(ip, SERVERPORT);

                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            if(isChecked)outToServer.writeBytes("setPermission " +holder.textView.getText().toString() +Global.username+" "+Global.password+'\n');
                            else outToServer.writeBytes("setPermission " +holder.textView.getText().toString() +Global.username+" "+Global.password+'\n');
                            outToServer.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }
                    }
                });
                thrSwitch.start();

                try {
                    thrSwitch.join(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        spliter=mList.get(position).split(" ");
        holder.textView.setText(spliter[0]);
        if(spliter[1].equals("d")) holder.switchP.setText("non-user");
        else holder.switchP.setText("user");


        if(spliter[1].equals("p") || spliter[1].equals("a"))holder.switchP.setChecked(true);
        else holder.switchP.setChecked(false);
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        Button DelButton;
        Switch switchP;

    }
}