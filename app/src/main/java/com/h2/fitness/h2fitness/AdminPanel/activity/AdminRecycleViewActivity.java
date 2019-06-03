package com.h2.fitness.h2fitness.AdminPanel.activity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h2.fitness.h2fitness.AppMain.LoginActivity;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.util.List;
import java.util.Map;

/**
 * Created by HP on 11/10/2017.
 */

public class AdminRecycleViewActivity extends AppCompatActivity

{
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    TextView textView;
    List<VideoModel> mAllVideoModelOfDisease;
    VideoAdapter taskList = VideoAdapter.getInstance();
    FirebaseAuth auth;
    String mId = user.getUid();
    private AdminResultAdober mResultAdopter;
    ProgressDialog pdialog;

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
                    Intent i = new Intent(AdminRecycleViewActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    Toast.makeText(AdminRecycleViewActivity.this, "error while logout ", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        RecyclerView recyclerView = findViewById(R.id.recycleview);
          //updateUI();
        collectingData();

        mAllVideoModelOfDisease = taskList.getTasks();
        mResultAdopter = new AdminResultAdober(mAllVideoModelOfDisease);
        // recyclerView.setLayoutManager(new GridLayoutManager(getApplication(,2));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.HORIZONTAL));

        recyclerView.setAdapter(mResultAdopter);
        //mResultAdopter.notifyDataSetChanged();// was commented

        //updateUI();


    }

    private void collectingData() {
        if (!(mId == null)) {
            pdialog = ProgressDialog.show(AdminRecycleViewActivity.this, "Please Wait...", "Processing...");
            taskList.removeResultList();
            PrefManager pref = new PrefManager(getApplicationContext());
            String value = pref.getDetails();
            String type = pref.gettype();
            String ref = Config.FIREBASE_USER_FILES + value + "/" + type + "/";
            String t = ref;
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
                    collectDataFromUserDatabase((Map<String, Object>) dataSnapshot.getValue());
                    updateUI();
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
        //boolean found = false;
        taskList.removeResultList();
        try {

            for (Map.Entry<String, Object> entry : value.entrySet()) {

                //Get user map
                Map singleUser = (Map) entry.getValue();

                String tempName = (String) singleUser.get(Config.FIREBASE_FILENAME);
                String tempPath = (String) singleUser.get(Config.FIREBASE_FILEPATH);

                //Get phone field and append to list
                //   paymentRecordsList.add((String) singleUser.get("mId"));
                VideoModel videoModel = new VideoModel();
                videoModel.setmFileName(tempName);
                videoModel.setmFilePath(tempPath);
                taskList.addResult(videoModel);

            }
        } catch (Exception ignored) {
        }


    }
}