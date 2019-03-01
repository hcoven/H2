package com.h2.fitness.h2fitness.AppMain;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.h2.fitness.h2fitness.AppMain.Model.History;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.io.File;
import java.util.Map;

public class RecycleViewClickedActivtiy extends AppCompatActivity {

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public Button mDownload;
    boolean check = false;
    TextView mfilename;
    String tempFilename;
    Dialog pdialog;
    boolean function = false;
    Uri mUri;
    String mId = user.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_recycle_view_clicked_activtiy);
        mDownload = (Button) findViewById(R.id.download);
        mfilename = (TextView) findViewById(R.id.mfilenameTextview);

        tempFilename = getIntent().getStringExtra("mfileName");
        mfilename.setText(tempFilename);


        mDownload.setOnClickListener(view -> {
            pdialog = ProgressDialog.show(RecycleViewClickedActivtiy.this, "Please Wait...", "Processing!");

            collectingData();

            //downlownloadvlause();//was commented


        });
    }

    private void collectingData() {
        if (!(mId == null)) {
            String ref = Config.FIREBASE_HISTORY + mId;
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        collectDataFromUserDatabase((Map<String, Object>) dataSnapshot.getValue());
                    } else {
                        Toast.makeText(getApplicationContext(), "no data found ", Toast.LENGTH_LONG).show();
                        pdialog.dismiss();
                        downlownloadvlause();
                    }
//

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "id is null", Toast.LENGTH_LONG).show();
        }
    }

    private void collectDataFromUserDatabase(Map<String, Object> value) {
        String tempName = "";
        String tempdownload = "";
        String tempid = "";

        try {
            for (Map.Entry<String, Object> entry : value.entrySet()) {
                //  pdialog.dismiss();
                Map singleUser = (Map) entry.getValue();
                tempName = (String) singleUser.get(Config.FIREBASE_FILENAME);
                //  tempdownload = (String) singleUser.get(Config.FIREBASE_DOWNLOAD);
                if (tempName.equals(tempFilename)) {
                    pdialog.dismiss();
                    check = true;
                    Intent intent = new Intent(RecycleViewClickedActivtiy.this, VideoActivity.class);
                    intent.putExtra("uri", "/data/user/0/com.h2.fitness.h2fitness/files/" + tempFilename);

                    startActivity(intent);
                    Toast.makeText(RecycleViewClickedActivtiy.this, "you already download this file", Toast.LENGTH_LONG).show();
                    break;
                }else {
                    downlownloadvlause();
                }
            }
        } catch (Exception e) {
        }

        /*if (!check) {
            pdialog.dismiss();

            downlownloadvlause();

        }*/
    }

    public void downlownloadvlause() {

        PrefManager pref = new PrefManager(getApplicationContext());
        String tempvalue = pref.getDetails();
        String type = pref.gettype();
        final FirebaseStorage storage = FirebaseStorage.getInstance();
        Toast.makeText(getApplicationContext(), Config.FILE_LOCTION + tempvalue + "/" + tempFilename, Toast.LENGTH_LONG).show();

        final StorageReference storageRef = storage.getReferenceFromUrl(Config.FILE_LOCTION + tempvalue + "/" + type + "/" + tempFilename);
        //if there is not any file

        try {

            final File localFile = new File(getApplicationContext().getFilesDir(), tempFilename);
            final FileDownloadTask task = storageRef.getFile(localFile);
            pdialog = ProgressDialog.show(RecycleViewClickedActivtiy.this, "Please Wait...", "Processing...");
            /*pdialog = new Dialog(RecycleViewClickedActivtiy.this);
            pdialog.setContentView(R.layout.dialog_popup);
            Button dialogButton = (Button) pdialog.findViewById(R.id.cancel_button);


            ImageView imageView = (ImageView) pdialog.findViewById(R.id.dialogImageview);
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(imageView);
            Glide.with(this).load(R.drawable.progress).into(imageView);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //    pdialog.dismiss();
                    //   task.cancel();
                    boolean canBeCancelled = task.cancel();
                    if (canBeCancelled == true) {
                        Intent intent = new Intent(RecycleViewClickedActivtiy.this, RecycleViewClickedActivtiy.class);
                        startActivity(intent);
                        finish();
                        pdialog.dismiss();
                        function = true;

                    }
                }

            });
            pdialog.show();*/
            storageRef.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
                pdialog.dismiss();
                Log.e("firebase ", ";local tem file created  created " + localFile.toString());
                Toast.makeText(RecycleViewClickedActivtiy.this, localFile.toString(), Toast.LENGTH_LONG).show();
                //updateDb(timestamp,localFile.toString(),position);
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id = user.getUid();
                DatabaseReference mnew_user = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_HISTORY + id);
                History history = new History();
                history.setmFileName(tempFilename);
                history.setmDownload("t");//was commented
                mnew_user.push().setValue(history);
                //Toast.makeText(RecycleViewClickedActivtiy.this,uri.toString(),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RecycleViewClickedActivtiy.this, VideoActivity.class);
                intent.putExtra("uri", localFile.toString());

                startActivity(intent);

            }).addOnFailureListener(exception -> {
                pdialog.dismiss();
                exception.printStackTrace();
                Log.e("firebase ", ";local tem file not created " + exception.toString());
            });


        } catch (Exception e) {
        }

    }

}

