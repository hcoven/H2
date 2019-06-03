package com.h2.fitness.h2fitness.Blog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.h2.fitness.h2fitness.AppMain.Fragments.FullBlogDetailShow;
import com.h2.fitness.h2fitness.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by HP on 10/2/2017.
 */


public class BlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private static final String TEXT_VALUE_KEY = "languageValue";
    final Handler textViewHandler3 = new Handler();
    public Blog mTask;
    public Context mContex;
    // private final ItemBinding binding;
    public TextView tittle, description, price, writer, timestamp;
    public ImageView thumbnail;
    String mTwriter, mTdescription, mTtittle;
    String languale;
    private String prefName = "My Pref";
    private SharedPreferences prefs;


    public BlogViewHolder(View itemView) {
        super(itemView);

        mContex = itemView.getContext();


        itemView.setOnClickListener(this);
        prefs = mContex.getSharedPreferences(prefName, MODE_PRIVATE);
        languale = prefs.getString(TEXT_VALUE_KEY, null);

        tittle = itemView.findViewById(R.id.tittle);

        writer = itemView.findViewById(R.id.writter);
        description = itemView.findViewById(R.id.description);
        //   price = view.findViewById(R.id.price);
        thumbnail = itemView.findViewById(R.id.urlimage);


    }


    @SuppressLint("StaticFieldLeak")
    public void bindTask(Blog task) {
        mTask = task;
        //   String tname = translate(task.getName(),languale);
        //  String ttittle = translate(task.getTittle(),languale)String tcontent = translate(task.getContent(),languale);
        String name = task.getName();
        String tittle1 = task.getTittle();
        String des = task.getContent();

        //  request();

        description.setText(task.getContent());
        Glide.with(mContex)
                .load(task.getImageUrl())
                .into(thumbnail);


        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyBoIR0Jruha8SuqkZQNCJgV4Blbj8dRiBE")
                            .build();

                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(des,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (description != null) {
                                description.setText(translation.getTranslatedText());
                            }
                        }
                    });

                    final Translation translation2 =
                            translate.translate(tittle1,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (tittle != null) {
                                tittle.setText(translation2.getTranslatedText());
                            }
                        }
                    });

                    final Translation translation3 =
                            translate.translate(name,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (writer != null) {
                                writer.setText(translation3.getTranslatedText());
                            }
                        }
                    });
                    return null;
                }
            }.execute();
        }*/
    }


    @Override
    public void onClick(View view) {
        //was commented
        FragmentManager fragmentManager = ((FragmentActivity) mContex).getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
        fragment =  FullBlogDetailShow.getInstance(mTask.getTittle(),mTask.getName(),mTask.getContent(),mTask.getImageUrl());
        fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();

        Intent intent = new Intent(mContex, FullBlog.class);
        intent.putExtra("tittle", mTask.getTittle());
        intent.putExtra("name", mTask.getName());
        intent.putExtra("image", mTask.getImageUrl());
        intent.putExtra("content", mTask.getContent());
        mContex.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View view) {
        // Handle long click
        Toast.makeText(mContex, "long pree ", Toast.LENGTH_LONG).show();
        return false;
    }


}


