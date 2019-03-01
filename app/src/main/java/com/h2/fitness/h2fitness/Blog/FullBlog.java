package com.h2.fitness.h2fitness.Blog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.h2.fitness.h2fitness.R;

public class FullBlog extends AppCompatActivity {

    private static final String TEXT_VALUE_KEY = "languageValue";
    final Handler textViewHandler3 = new Handler();
    String tittle1, writer, content, imgurl;
    String languale;
    TextView mTitle, mWriter, mContext;
    ImageView imageView;
    private String prefName = "My Pref";
    private SharedPreferences prefs;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_blog_detail_show);
        prefs = getApplicationContext().getSharedPreferences(prefName, MODE_PRIVATE);
        languale = prefs.getString(TEXT_VALUE_KEY, null);

        Intent b = getIntent();
        if (b != null) {
            tittle1 = b.getStringExtra("tittle");
            writer = b.getStringExtra("name");
            imgurl = b.getStringExtra("image");
            content = b.getStringExtra("content");
        }

        mTitle = (TextView) findViewById(R.id.blog_detial_tittle_text);
        mWriter = (TextView) findViewById(R.id.blog_detial_writer_name);
        mContext = (TextView) findViewById(R.id.blog_detial_content);
        imageView = (ImageView) findViewById(R.id.blog_detail_image);

        Glide.with(getApplicationContext()).load(imgurl).into(imageView);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                    TranslateOptions options = TranslateOptions.newBuilder()
                            .setApiKey("AIzaSyBoIR0Jruha8SuqkZQNCJgV4Blbj8dRiBE")
                            .build();

                    Translate translate = options.getService();
                    final Translation translation =
                            translate.translate(tittle1,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mTitle != null) {
                                mTitle.setText(translation.getTranslatedText());
                            }
                        }
                    });

                    final Translation translation2 =
                            translate.translate(writer,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(() -> {
                        if (mWriter != null) {
                            mWriter.setText(translation2.getTranslatedText());
                        }
                    });

                    final Translation translation3 =
                            translate.translate(content,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(() -> {
                        if (mContext != null) {
                            mContext.setText(translation3.getTranslatedText());
                        }
                    });
                    return null;
                }
            }.execute();
        }


    }


}
