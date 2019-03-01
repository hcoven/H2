package com.h2.fitness.h2fitness.AdminPanel.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.Utils.VideoThumnail;
import com.h2.fitness.h2fitness.AppMain.RecycleViewClickedActivtiy;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by HP on 10/2/2017.
 */


public class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private static final String TEXT_VALUE_KEY = "languageValue";
    private final Handler textViewHandler = new Handler();
    final Handler textViewHandler3 = new Handler();
    private VideoModel userTask;
    public Context mContex;
    private TextView mFilename;
    private String tempfilename = "";
    private View mView;
    Handler mHandler;
    Bitmap bmThumbnail;
    private Bitmap bitmap;

    VideoViewHolder(View itemView) {
        super(itemView);

        mContex = itemView.getContext();

        String prefName = "My Pref";
        SharedPreferences prefs = mContex.getSharedPreferences(prefName, MODE_PRIVATE);
        String languale = prefs.getString(TEXT_VALUE_KEY, null);
        itemView.setOnClickListener(this);
        mFilename = (TextView) itemView.findViewById(R.id.disname);


        VideoView mVideoPreView = (VideoView) itemView.findViewById(R.id.video_preview);

        //  mImageView =(NetworkImageView)itemView.findViewById(R.id.board_photo);


    }

    @SuppressLint("StaticFieldLeak")
    void bindTask(VideoModel task) {
        userTask = task;
            mFilename.setText(task.getmFileName());//was commented

        String name = task.getmFileName();
        tempfilename = task.getmFilePath();

        new Thread(new Task(itemView)).start();

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
                    textViewHandler3.post(new Runnable(){
                        @Override
                        public void run() {
                            if (mFilename != null) {
                                mFilename.setText(translation.getTranslatedText());
                            }
                        }
                    });
                    return null;
                }
            }.execute();
        }
*/

    }


    @Override
    public void onClick(View view) {

        Intent i = new Intent(view.getContext(), RecycleViewClickedActivtiy.class);
        i.putExtra("mfileName", userTask.getmFileName());
        mContex.startActivity(i);
        Toast.makeText(mContex, userTask.getmFileName(), Toast.LENGTH_LONG).show();


    }

    @Override
    public boolean onLongClick(View view) {
        // Handle long click
        Toast.makeText(mContex, "long pree ", Toast.LENGTH_LONG).show();
        return false;
    }


    class Task implements Runnable {

        public Task(View context) {
            context = mView;
        }

        @Override
        public void run() {

            try {
                bitmap = VideoThumnail.retriveVideoFrameFromVideo(tempfilename);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            textViewHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (bitmap != null) {
                        bitmap = Bitmap.createScaledBitmap(bitmap, 80, 80, false);
                        //mVideoPreView.setImageBitmap(bitmap);
                    } else {
                        Toast.makeText(mContex, "show me ", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

    }

}