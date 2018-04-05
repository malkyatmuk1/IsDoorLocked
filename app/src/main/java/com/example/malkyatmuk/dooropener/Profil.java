package com.example.malkyatmuk.dooropener;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by malkyatmuk on 10/14/17.
 */

public class Profil extends Fragment {

    TextView usernameT,permissionT;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_profil, container, false);
        usernameT = (TextView) myFragmentView.findViewById(R.id.usernameprofil);
        permissionT = (TextView) myFragmentView.findViewById(R.id.permissionprofil);
        return myFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        usernameT.setText(Global.username);
        if(Global.permission=='a')permissionT.setText("admin");
        else if(Global.permission=='p')permissionT.setText("user");
        else permissionT.setText("non-user");
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Profile");
    }
}
