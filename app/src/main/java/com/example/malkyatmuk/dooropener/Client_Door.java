package com.example.malkyatmuk.dooropener;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client_Door extends Fragment {

    private static final int SERVERPORT = 3030;
    private static  String SERVER_IP ;
    Button check;
    String modifiedSentence;
    private Socket clientSocket;
    TextView text;
    ImageView door;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_door, container, false);
        check = (Button) myFragmentView.findViewById(R.id.check);
        door = (ImageView) myFragmentView.findViewById(R.id.door);

        check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            clientSocket = new Socket(Global.ip, SERVERPORT);
                            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                            outToServer.writeBytes("take " +Global.username+" "+Global.password+'\n');
                            outToServer.flush();
                            modifiedSentence = inFromServer.readLine();
                            if (modifiedSentence.equals("open!")) {
                                getActivity().runOnUiThread(new Runnable() //run on ui thread
                                {
                                    public void run() {
                                        door.setImageResource(R.drawable.opendoor);
                                    }
                                });
                            } else {
                                getActivity().runOnUiThread(new Runnable() //run on ui thread
                                {
                                    public void run() {
                                        door.setImageResource(R.drawable.closeddoor);

                                    }
                                });
                            }
                            clientSocket.close();
                        } catch (IOException e) {
                            System.out.println("Exception " + e);
                        }
                        return;
                    }
                }).start();

            }
        });

        return myFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Door");
    }
}

