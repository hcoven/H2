package com.h2.fitness.h2fitness.AdminPanel.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.AppMain.SelectType;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/20/2017.
 */

public class AdminFlexibilityFragment extends Fragment {

    public static final String workout = "flexibilty/";
    private static final int PICKFILE_RESULT_CODE = 1001;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Button mUpload, mDelte;
    TextView testing;
    Spinner mType;
    FirebaseAuth auth;
    String mId = user.getUid();
    String name;
    String value;
    String[] countries = new String[]{
            "select",
            "entry",
            "intermediate",
            "expert"

    };
    ProgressDialog progressDialog;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new AdminFlexibilityFragment();
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
        View view = inflater.inflate(R.layout.fragment_upload_data, container, false);

        mUpload = (Button) view.findViewById(R.id.uploadnewFile);
        mDelte = (Button) view.findViewById(R.id.delete);
        testing = (TextView) view.findViewById(R.id.testingtxt);
        mType = (Spinner) view.findViewById(R.id.select_type);


        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Uploading");
// try and catch block was commented
        /*try {
            Bundle args = getArguments();
            if (args != null) {
                String f = args.getString("f");


                name = args.getString("name");
                //    Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();
                if (f.equals("f")) {
                    mDelte.setVisibility(View.GONE);

                }
                  *//*String f =  getIntent().getStringExtra("f");
                    name =getIntent().getStringExtra("name");
                    Toast.makeText(getContext(),name,Toast.LENGTH_LONG).show();
                  if(f.equals("f")){
                     mDelte.setVisibility(View.GONE);
                     history();
                   deletehistory();
*//*
            }
        } catch (Exception e) {
        }*/

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext()
                , android.R.layout.simple_spinner_item, countries);

        // Set the Adapter
        mType.setAdapter(arrayAdapter);

        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int item = mType.getSelectedItemPosition();

                value = countries[position];
                Toast.makeText(getApplicationContext(), (countries[position]), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (value != null && value != "select") {

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, PICKFILE_RESULT_CODE);
                } else {
                    Toast.makeText(getApplicationContext(), "please select the level first", Toast.LENGTH_SHORT).show();
                }


            }
        });
// mDelte was commented
        mDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PrefManager pref = new PrefManager(getApplicationContext());
                pref.clearSession();
                pref.createfile("flexibilty");

                Intent intent = new Intent(getApplicationContext(), SelectType.class);
                startActivity(intent);
            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);
                    String path = myFile.getAbsolutePath();
                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
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

                    storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + workout + value + "/" + displayName);
                    try {
                        if (!name.equals(null)) {
                            storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + workout + value + "/" + name);


                        }
                    } catch (Exception e) {
                    }


                    UploadTask uploadTask = storage.putFile(uri);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            exception.fillInStackTrace();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @SuppressWarnings("ResourceType")
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                            //   Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //noinspection VisibleForTests
                            Uri download = taskSnapshot.getUploadSessionUri();
                            String downlodurl = taskSnapshot.getUploadSessionUri().toString();
                            progressDialog.dismiss();


                            String ref = Config.FIREBASE_USER_FILES + workout + value + "/";
                            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);
                            if (name != null) {
                                Map mParent = new HashMap();
                                mParent.put("mFileName", name);
                                mParent.put("url", downlodurl);


                                mdata.push().setValue(mParent);
                                mdata.child("mFileName").setValue(name);//was commented
                                mdata.push().child("url").setValue(download);//was commented

                            } else {

                                Map mParent = new HashMap();
                                mParent.put("mFileName", testing.getText());
                                mParent.put("url", downlodurl);


                                mdata.push().setValue(mParent);
                                mdata.child("mFileName").setValue(testing.getText());//was commented
                                mdata.push().child("url").setValue(download);//was commented


                            }

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.show();
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");

                        }
                    });
                }
                break;
        }

    }


}
