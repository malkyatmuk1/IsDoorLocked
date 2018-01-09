package com.example.malkyatmuk.dooropener;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class Client_List extends Fragment {


    String[] gen=new String[]{"There are no other users!"};
    private static final int SERVERPORT = 3030;
    private static  String SERVER_IP ;
    private Socket clientSocket;
    public void readUsers(View view) {
        Thread thr;
        ListView listView;
        listView = (ListView)view.findViewById(R.id.list);
       // for (int i=0;i<10;i++) Global.usernames.add("no users");
        Global.usernames.clear();
        thr = new Thread(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                try {
                    SERVER_IP = pref.getString("ip", "");
                    InetAddress ip = InetAddress.getByName(SERVER_IP);
                    clientSocket = new Socket(ip, SERVERPORT);

                    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    outToServer.writeBytes("list " +Global.username+" "+Global.password+ '\n');
                    outToServer.flush();
                    int i = 0;
                    String line;
                    String[] spliter;
                    while (true) {
                        line = inFromServer.readLine();
                        if (!line.equals("stop") && !line.equals("error")) {
                            Global.usernames.add(line);
                        }
                        else break;

                    }

                    clientSocket.close();
                } catch (IOException e) {
                    System.out.println("Exception " + e);
                }
            }
        });
        thr.start();

        try {
            thr.join(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter mAdapter;
        if(Global.usernames.isEmpty()) {
            mAdapter = new ArrayAdapter(getContext(), R.layout.listview_general,R.id.name_general, gen);
            listView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
        else{
            Adapter adapter = new Adapter(getContext(), Global.usernames);

            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
     //   adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, Global.usernamees);
    }

  @Nullable
  @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
      View view = inflater.inflate(R.layout.fragment_adduser, container, false);
      readUsers(view);
      return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("User List");
        view.setFocusable(false);

//        listView.setAdapter(adapter);

    }
}

