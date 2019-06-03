package com.h2.fitness.h2fitness.AppMain.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.h2.fitness.h2fitness.AdminPanel.activity.VideoAdapter;
import com.h2.fitness.h2fitness.R;

/**
 * Created by HP on 11/20/2017.
 */

public class DialogFragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    VideoAdapter taskList = VideoAdapter.getInstance();
    FirebaseAuth auth;
    String mId = user.getUid();
    ProgressDialog pdialog;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new DialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_abs, container, false);


        return view;
    }


}
