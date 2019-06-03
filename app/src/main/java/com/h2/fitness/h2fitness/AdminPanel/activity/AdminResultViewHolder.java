package com.h2.fitness.h2fitness.AdminPanel.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h2.fitness.h2fitness.AdminPanel.Fragment.AdminWorkOutFragment;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/10/2017.
 */

public class AdminResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final int PICKFILE_RESULT_CODE = 1001;
    private final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private final CharSequence[] items = {"Update video", "delete video"};
    private VideoModel adminTask;
    public Context mContex;
    private TextView mNameOfFile;
    private String mId = user.getUid();
    private ProgressDialog pdialog;
    private String filename;
    private VideoView mVideoPreView;

    AdminResultViewHolder(View itemView) {
        super(itemView);

        mContex = itemView.getContext();

        //itemView.setOnLongClickListener(this);
        itemView.setOnClickListener(this);
        mNameOfFile = (TextView) itemView.findViewById(R.id.disname);
        mVideoPreView = itemView.findViewById(R.id.video_preview);

        //  mImageView =(NetworkImageView)itemView.findViewById(R.id.board_photo);


    }

    void bindTask(VideoModel task) {
        adminTask = task;
        mNameOfFile.setText(task.getmFileName());
        filename = task.getmFileName();


        mVideoPreView.setVideoURI(Uri.parse(task.getmFilePath()));

        mVideoPreView.seekTo(100);


    }


    @Override
    public void onClick(View view) {
       //was commented
        //Intent i = new Intent(view.getContext(), RecycleViewClickedActivtiy.class);
        //i.putExtra("mfileName", adminTask.getmFileName());
        //mContex.startActivity(i);

        //openFileChooserDialog(adminTask.getmFileName());
        //deletefile(adminTask.getmFileName());


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContex);
        alertDialog.setTitle("Danger!");
        alertDialog.setMessage("Do You want to Delete this video ");
        alertDialog.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
        alertDialog.setNegativeButton("YES", (dialog, which) -> {

            // DO SOMETHING HERE
            deletefile(adminTask.getmFileName());


        });

        AlertDialog dialog = alertDialog.create();
        dialog.show();


    }
    @Override
    public boolean onLongClick(View view) {
        deletefile(adminTask.getmFileName());

        //Toast.makeText(mContex, "long pree ", Toast.LENGTH_LONG).show();
        return true;
    }

    private void openFileChooserDialog(final String name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContex);
        builder.setTitle("DETAILS");
        builder.setItems(items, (dialog, which) -> {
            switch (which) {
                case 0:

                    //  initCameraPermission();
                    deleteFileForUpdate(name);

                    break;
                case 1:
                    deletefile(name);
                    Toast.makeText(mContex, " delete task", Toast.LENGTH_LONG).show();

                    break;
                default:
            }
        });
        builder.show();
    }

    private void deleteFileForUpdate(String displayName) {
        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + displayName);


        //pdialog = ProgressDialog.show(mContex, "Please Wait...", "Processing...");//was commented
        // Register observers to listen for when the download is done or if it fails
        storage.delete().addOnSuccessListener(aVoid -> {
            pdialog.dismiss();

            de1();
            //collectingData();
        }).addOnFailureListener(Throwable::printStackTrace);

    }

    private void de1() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        Query applesQuery = ref.child("files").child("workout").orderByChild("mFileName").equalTo(filename);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    //history();//was commented
                    Intent intent = new Intent(mContex, AdminPanel.class);
                    intent.putExtra("f", "f");
                    intent.putExtra("name", filename);
                    mContex.startActivity(intent);


                    FragmentManager fragmentManager = ((AppCompatActivity) mContex).getSupportFragmentManager();


                    Bundle bundle = new Bundle();
                    bundle.putString("f", "f");
                    bundle.putString("name", filename);

                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder_admin);
                    fragment.setArguments(bundle);
                    fragment = AdminWorkOutFragment.getInstance();
                    fragmentManager.beginTransaction().replace(R.id.fragment_holder_admin, fragment).commit();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void deletefile(String displayName) {
        PrefManager pref = new PrefManager(getApplicationContext());
        String type = pref.getDetails();

        StorageReference storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + type + "/" + displayName);
        Toast.makeText(mContex, " delete in progress", Toast.LENGTH_SHORT).show();


// this code is to handle download, trying to make code to handle delete
        //pdialog = ProgressDialog.show(mContex, "Please Wait...", "Processing delete");
        // Register observers to listen for when the download is done or if it fails
        storage.delete().addOnSuccessListener(aVoid -> {

            Toast.makeText(mContex, " delete successful", Toast.LENGTH_LONG).show();

            //history();
            de1();
            //pdialog.dismiss();

        }).addOnFailureListener(Throwable::printStackTrace);

    }

    private void de() {

        PrefManager pref = new PrefManager(getApplicationContext());
        String type = pref.getDetails();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        Query applesQuery = ref.child("files").child(type).orderByChild("mFileName").equalTo(adminTask.getmFileName());

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                    /*Intent intent = new Intent(mContex, AdminRecycleViewActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContex.startActivity(intent);*/
                    //((Activity)mContex).finish();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //   pdialog.dismiss();
                Log.e("check me", "onCancelled", databaseError.toException());
            }
        });
    }

    private void history() {
        if (!(mId == null)) {
            String ref = Config.FIREBASE_USER;//formerly firebase_users
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);
            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());

                    collectDataFromUserDatabase((Map<String, Object>) dataSnapshot.getValue());
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(mContex, "id is null", Toast.LENGTH_LONG).show();
        }


    }

    private void collectDataFromUserDatabase(Map<String, Object> value) {
        //boolean found = false;//was commented

        //pdialog.dismiss();//was commented
        try {
            String f = "";
            for (Map.Entry<String, Object> entry : value.entrySet()) {

                //Get user map
                Map singleUser = (Map) entry.getValue();

                String userId = (String) singleUser.get(Config.FIREBASE_ID);

                //Get phone field and append to list
                //  history.add((String) singleUser.get("mId"));

                DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_HISTORY);
                //  DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query applesQuery = ref.child(userId).orderByChild("mFileName").equalTo(filename);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("checkMe", "onCancelled", databaseError.toException());
                    }
                });

            }
            //  Toast.makeText(AdminPanel.this,f,Toast.LENGTH_LONG).show();
        } catch (Exception e) {
        }
    }


}