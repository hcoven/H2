package com.h2.fitness.h2fitness.AdminPanel.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/20/2017.
 */

public class AdminBlogFragment extends Fragment {

    public static final String workout = "blog/";
    public final int RESULT_LOAD_IMAGE = 100;
    public EditText mWriter, mTittle, mContent;
    public Button mSubmitButton;
    public ImageButton mImageView;
    public Uri selectedImage;
    ProgressDialog pdialog;
    String picName;
    String mCurrentTittle, mCurrentWriter, mCurrentContent;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new AdminBlogFragment();
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
        View view = inflater.inflate(R.layout.fragment_admin_blog, container, false);

        mWriter = (EditText) view.findViewById(R.id.blog_writer);
        mTittle = (EditText) view.findViewById(R.id.blog_tittle);
        mContent = (EditText) view.findViewById(R.id.blog_content);
        mImageView = (ImageButton) view.findViewById(R.id.blog_image);
        mSubmitButton = (Button) view.findViewById(R.id.blog_submit);


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromAlbum();
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!validate()) {
                        onLoginFailed();
                        return;
                    }
                    uploadThisFileTOfirebase(selectedImage);

                    // [END sign_in_with_email]

                } catch (Exception e) {
                }

            }
        });

        return view;
    }


    public boolean validate() {
        boolean valid = true;

        mCurrentTittle = mTittle.getText().toString();
        mCurrentWriter = mWriter.getText().toString();
        mCurrentContent = mContent.getText().toString();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mCurrentTittle.isEmpty()) {
                mTittle.setError("please enter the tittle  ");
                valid = false;
            } else {
                mTittle.setError(null);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mCurrentWriter.isEmpty()) {
                mWriter.setError("please enter the writer name");
                valid = false;
            } else {
                mTittle.setError(null);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (mCurrentContent.isEmpty()) {
                mContent.setError("please enter some content of the blog");
                valid = false;
            } else {
                mContent.setError(null);
            }
        }


        return valid;
    }

    public void onLoginFailed() {
        Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();


    }

    private void getImageFromAlbum() {
        try {
            Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE);
        } catch (Exception exp) {
            Log.i("Error", exp.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getApplicationContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            picName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));//was commented
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            picName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            picName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));//was commented
            Log.d("path", picturePath);
            Log.d("path", picName);
            cursor.close();

            Glide.with(this).load(selectedImage)
                    .thumbnail(0.5f)

                    .into(mImageView);


        }

    }

    private void uploadThisFileTOfirebase(Uri picturePath) {

        pdialog = ProgressDialog.show(getApplicationContext(), "Please Wait...", "Processing...");

        StorageReference storage;
        storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + workout + picName);
        try {
            if (!picName.equals(null)) {
                storage = FirebaseStorage.getInstance().getReferenceFromUrl(Config.FILE_LOCTION + workout + picName);


            }
        } catch (Exception e) {
        }


        UploadTask uploadTask = storage.putFile(picturePath);


        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                pdialog.dismiss();
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
                pdialog.dismiss();


                String ref = Config.FIREBASE_BLOG_FILES;
                DatabaseReference mdataq = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

                Map mParent1 = new HashMap();
                mParent1.put("tittle", mCurrentTittle);
                mParent1.put("writer", mCurrentWriter);
                mParent1.put("content", mCurrentContent);
                mParent1.put("picName", picName);
                mParent1.put("url", downlodurl);


                mdataq.push().setValue(mParent1);


            }
        });
    }

}
