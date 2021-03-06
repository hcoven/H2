package com.h2.fitness.h2fitness.AppMain.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h2.fitness.h2fitness.AdminPanel.activity.ResultAdober;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.AdminPanel.activity.VideoModel;
import com.h2.fitness.h2fitness.AdminPanel.activity.VideoAdapter;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/20/2017.
 */

public class WeightTrainingFragment extends Fragment {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    List<VideoModel> mAllVideoModelOfWeight;
    VideoAdapter taskList = VideoAdapter.getInstance();
    FirebaseAuth auth;
    String mId = user.getUid();
    ProgressDialog pdialog;
    PrefManager pref;
    //TextView textView;
    private RecyclerView recyclerView;
    private ResultAdober mWeightAdopter;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new WeightTrainingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = new PrefManager(getApplicationContext());
        taskList.removeResultList();
        if (mAllVideoModelOfWeight != null) {
            mAllVideoModelOfWeight.clear();
        }
//
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weight_tranning, container, false);


        mAllVideoModelOfWeight = taskList.getTasks();
        PrefManager pref = new PrefManager(getApplicationContext());
        pref.clearSession();
        pref.createfile("weight");
        collectingData();
        mWeightAdopter = new ResultAdober(mAllVideoModelOfWeight);
        recyclerView = (RecyclerView) view.findViewById(R.id.weightrecycleview);
        // recyclerView.setLayoutManager(new GridLayoutManager(getApplication(,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL));


        recyclerView.setAdapter(mWeightAdopter);
        //    mResultAdopter.notifyDataSetChanged();

        updateUI();

        return view;
    }


    private void collectingData() {
        if (!(mId == null)) {
//            pdialog = ProgressDialog.show(getContext(), "Please Wait...", "Processing...");


            String type = pref.gettype();
            String ref = Config.FIREBASE_USER_FILES + "weight/" + type + "/";
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                    collectDataFromUserDatabase((Map<String, Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "id is null", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI() {


        if (mWeightAdopter != null) {

            mWeightAdopter.notifyDataSetChanged();
        }
    }

    private void collectDataFromUserDatabase(Map<String, Object> value) {
        //   pdialog.dismiss();
        taskList.removeResultList();
        try {

            for (Map.Entry<String, Object> entry : value.entrySet()) {

                //Get user map
                Map singleUser = (Map) entry.getValue();

                String tempName = (String) singleUser.get(Config.FIREBASE_FILENAME);
                String temppath = (String) singleUser.get(Config.FIREBASE_FILEPATH);
                //Get phone field and append to list
                //   paymentRecordsList.add((String) singleUser.get("mId"));
                VideoModel videoModel = new VideoModel();
                videoModel.setmFileName(tempName);
                videoModel.setmFilePath(temppath);
                taskList.addResult(videoModel);
                updateUI();


            }

        } catch (Exception e) {
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


            mAllVideoModelOfWeight = taskList.getTasks();
            PrefManager pref = new PrefManager(getApplicationContext());
            pref.clearSession();
            pref.createfile("weight");
            collectingData();

            // Toast.makeText(getApplicationContext(),pref.getDetails(),Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(getApplicationContext(), "back click", Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                    //fragment = MainFragment.getInstance();
                    fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();


                    return true;
                }
                return false;
            }
        });

    }
}
