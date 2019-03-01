package com.h2.fitness.h2fitness.Adobter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.h2.fitness.h2fitness.AppMain.Model.Users;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.AppMain.ChatActivity;

import static android.content.Context.MODE_PRIVATE;

public class FriendListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final String TEXT_VALUE_KEY = "languageValue";
    final Handler textViewHandler3 = new Handler();
    public Users mTask;
    public Context mContex;
    TextView mName, mStatus;
    String user_name, user_id;
    String languale;
    private SharedPreferences prefs;
    private String prefName = "My Pref";


    public FriendListViewHolder(View itemView) {
        super(itemView);

        mContex = itemView.getContext();
        prefs = mContex.getSharedPreferences(prefName, MODE_PRIVATE);
        languale = prefs.getString(TEXT_VALUE_KEY, null);

        itemView.setOnClickListener(this);
        mName = (TextView) itemView.findViewById(R.id.user_single_name);
        mStatus = (TextView) itemView.findViewById(R.id.user_single_status);
        //  mImageView =(NetworkImageView)itemView.findViewById(R.id.board_photo);


    }

    @SuppressLint("StaticFieldLeak")
    public void bindTask(Users task) {
        mTask = task;
        //    mName.setText(task.getName());


        //  mStatus.setText(task.getStatus());
        String name = task.getName();
        String message = task.getStatus();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyBoIR0Jruha8SuqkZQNCJgV4Blbj8dRiBE")
                            .build();

                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(name,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mName != null) {
                                mName.setText(translation.getTranslatedText());
                            }
                        }
                    });

                    final Translation translation2 =
                            translate.translate(message,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mStatus != null) {
                                mStatus.setText(translation2.getTranslatedText());
                            }
                        }
                    });
                    return null;
                }
            }.execute();
        }
        //  mImageUrl =
        // task.getThumb_image();
        //  try{
        //    Glide.with(mContex).load(mImageUrl)
        //          .thumbnail(0.5f)
        //        .crossFade()
        //      .diskCacheStrategy(DiskCacheStrategy.ALL)
        //    .into(mImageView);
        //      }catch (Exception e){}


    }


    @Override
    public void onClick(View view) {

        Intent i = new Intent(view.getContext(), ChatActivity.class);
        String t, n;
        String name = mTask.getName();
        t = name;
        String id = mTask.getUser_id();
        n = id;
        i.putExtra("name", mTask.getName());
        i.putExtra("user_id", mTask.getUser_id());
        mContex.startActivity(i);
        //   Toast.makeText(mContex,adminTask.getmFileName(), Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onLongClick(View view) {
        // Handle long click
        Toast.makeText(mContex, "long pree ", Toast.LENGTH_LONG).show();
        return false;
    }


}
