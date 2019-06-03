package com.h2.fitness.h2fitness.AppMain;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
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

import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends AppCompatActivity

{
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView textView;
    List<VideoModel> mAllVideoModelOfDisease;
    VideoAdapter taskList = VideoAdapter.getInstance();
    FirebaseAuth auth;
    String mId = user.getUid();
    ProgressDialog pdialog;
    private RecyclerView recyclerView;
    private ResultAdober mResultAdopter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sigout: {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                if (auth.getCurrentUser() == null) {
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    Toast.makeText(MainActivity.this, "error while logout ", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        Fabric.with(fabric);

        collectingData();

        mAllVideoModelOfDisease = taskList.getTasks();

        mResultAdopter = new ResultAdober(mAllVideoModelOfDisease);
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        //recyclerView.setLayoutManager(new GridLayoutManager(getApplication(,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL));


        recyclerView.setAdapter(mResultAdopter);
        mResultAdopter.notifyDataSetChanged();

        updateUI();
    }


    private void collectingData() {
        if (!(mId == null)) {
            pdialog = ProgressDialog.show(MainActivity.this, "Please Wait...", "Processing...");

            String ref = Config.FIREBASE_USER_FILES;
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
            });pdialog.dismiss();

        } else {
            Toast.makeText(getApplicationContext(), "id is null", Toast.LENGTH_LONG).show();
        }
    }

    private void updateUI() {


        if (mResultAdopter != null) {

            mResultAdopter.notifyDataSetChanged();
        }
    }

    private void collectDataFromUserDatabase(Map<String, Object> value) {
        //   boolean found = false;
        pdialog.dismiss();
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
    protected void onResume() {
        super.onResume();

        // register connection status listener
        //     MyApplication.getInstance().setConnectivityListener(this);
    }

    // @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {

            Toast.makeText(MainActivity.this, "Online", Toast.LENGTH_LONG).show();
        } else {

            Toast.makeText(MainActivity.this, "Offline", Toast.LENGTH_LONG).show();

        }

        //  Snackbar snackbar = Snackbar
        //        .make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG);

        //   View sbView = snackbar.getView();
        //  TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        // textView.setTextColor(color);
        //  snackbar.show();
    }
}
