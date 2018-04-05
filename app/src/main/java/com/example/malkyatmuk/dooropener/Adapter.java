package com.example.malkyatmuk.dooropener;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
    private String[] spliter;
    private String modif;

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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            holder.textView = (TextView) convertView.findViewById(R.id.name);
            holder.DelButton = (Button) convertView.findViewById(R.id.delete);
            holder.switchP = (Switch) convertView.findViewById(R.id.switchPerm);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.DelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                mList.remove(position);
                class LongOperation extends AsyncTask<String, Void, Void> {
                    private static final int SERVERPORT = 3030;
                    private String SERVER_IP;
                    private Socket clientSocket;

                    protected Void doInBackground(String... Param) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

                         try {
                            InetAddress ip = InetAddress.getByName(Global.ip);
                            clientSocket = new Socket(ip, SERVERPORT);

                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            outToServer.writeBytes("del " + holder.textView.getText().toString() + " " + Global.username + " " + Global.password + '\n');
                            outToServer.flush();


                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        notifyDataSetChanged();
                    }

                    @Override
                    protected void onPreExecute() {}

                    @Override
                    protected void onProgressUpdate(Void... values) {}
                }

                new LongOperation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });


        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         final boolean isChecked) {
                if (isChecked) {
                    holder.switchP.setText("user");
                } else holder.switchP.setText("non-user");
                class LongOperation extends AsyncTask<String, Void, Void> {
                    private static final int SERVERPORT = 3030;
                    private Socket clientSocket;

                    protected Void doInBackground(String... Param) {
                        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);

                        try {

                            InetAddress ip = InetAddress.getByName(Global.ip);
                            clientSocket = new Socket(ip, SERVERPORT);

                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            if (isChecked)
                                outToServer.writeBytes("setPermission " + holder.textView.getText().toString() + " p " + Global.username + " " + Global.password + '\n');
                            else
                                outToServer.writeBytes("setPermission " + holder.textView.getText().toString() + " d " + Global.username + " " + Global.password + '\n');
                            modif = inFromServer.readLine();
                            mList.set(position, holder.textView.getText().toString() + " " + modif);

                            outToServer.flush();
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {

                    }

                    @Override
                    protected void onPreExecute() {}

                    @Override
                    protected void onProgressUpdate(Void... values) {}
                }
                new LongOperation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);;


            }
        };

        spliter=mList.get(position).split(" ");
        holder.textView.setText(spliter[0]);
        if(spliter[1].equals("d")) holder.switchP.setText("non-user");
        else holder.switchP.setText("user");

        if(spliter[1].equals("p") || spliter[1].equals("a")){holder.switchP.setOnCheckedChangeListener(null);holder.switchP.setChecked(true);holder.switchP.setOnCheckedChangeListener(checkListener);}
        else {holder.switchP.setOnCheckedChangeListener (null);holder.switchP.setChecked(false);holder.switchP.setOnCheckedChangeListener(checkListener);}
        return convertView;
    }

    class ViewHolder{
        TextView textView;
        Button DelButton;
        Switch switchP;
    }
}