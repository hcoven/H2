package com.h2.fitness.h2fitness.AdminPanel.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h2.fitness.h2fitness.AdminPanel.Fragment.AdminPagerViewFragment;
import com.h2.fitness.h2fitness.AppMain.LoginActivity;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;

import java.io.File;
import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {
    private static final int PICKFILE_RESULT_CODE = 1001;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private Button mUpload, mDelte;
    private TextView testing;
    // String filename;
    private ProgressDialog pdialog;
    private FirebaseAuth auth;
    private String mId = user.getUid();
    private String name, email;
    private ArrayList history = new ArrayList();

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
                    Intent i = new Intent(AdminPanel.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    Toast.makeText(AdminPanel.this, "error while logout ", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        //   setContentView(R.layout.activity_main_screen);


        //gets the admin email from the LoginActivity when Login is successful


        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder_admin);
        if (fragment == null) {

            fragment = createFragment();

            fragmentManager.beginTransaction().add(R.id.fragment_holder_admin, fragment).commit();
        }


    }


    /*
        mUpload =(Button) findViewById(R.id.uploadnewFile);
            mDelte =(Button)findViewById(R.id.delete);
              testing =(TextView)findViewById(R.id.testingtxt);
              try {
               String f =  getIntent().getStringExtra("f");
                  name =getIntent().getStringExtra("name");
                  Toast.makeText(AdminPanel.this,name,Toast.LENGTH_LONG).show();
                  if(f.equals("f")){
                      mDelte.setVisibility(View.GONE);
                  //     history();
                   //   deletehistory();

                  }
              }catch (Exception e){}

            mUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
          */
//intent.setType( "*/*");
  /*/              startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

        mDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminPanel.this,AdminRecycleViewActivity.class);
                startActivity(intent);
            }
        });

    }
*/
    protected Fragment createFragment() {
        return new AdminPagerViewFragment();
    }

// onActivityResult() was commented
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    pdialog = ProgressDialog.show(AdminPanel.this, "Please Wait...", "Processing...");
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    Toast.makeText(AdminPanel.this, path, Toast.LENGTH_LONG).show();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                                testing.setText(displayName);
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                        testing.setText(displayName);
                    }
                    StorageReference storage;
                    storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + testing.getText());
                    try {
                        if (!name.equals(null)) {
                            storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + name);


                        }
                    } catch (Exception e) {
                    }


                    UploadTask uploadTask = storage.putFile(uri);


                    // Register observers to listen for when the download is done or if it fails
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            pdialog.dismiss();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("ResourceType")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            //   Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //noinspection VisibleForTests
                            Uri download = taskSnapshot.getUploadSessionUri();
                            String downlod =    taskSnapshot.getUploadSessionUri().toString();
                            pdialog.dismiss();

                            String ref = Config.FIREBASE_USER_FILES;
                            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);
                            if (name != null) {
                                mdata.push().child("mFileName").setValue(name);

                            } else {
                                mdata.push().child("mFileName").setValue(testing.getText());
                            }

                        }
                    });
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
