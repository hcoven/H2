package com.h2.fitness.h2fitness.AppMain.Fragments;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.h2.fitness.h2fitness.R;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/20/2017.
 */

public class FullBlogDetailShow extends Fragment {

    private static final String TEXT_VALUE_KEY = "languageValue";
    final Handler textViewHandler3 = new Handler();
    String tittle1, writer, content, imgurl;
    String languale;
    TextView mTitle, mWriter, mContext;
    ImageView imageView;
    private String prefName = "My Pref";
    private SharedPreferences prefs;

    public static Fragment getInstance(String tittle, String writer, String content, String imgurl) {
        Bundle bundle = new Bundle();
        bundle.putString("tittle", tittle);
        bundle.putString("writer", writer);
        bundle.putString("content", content);
        bundle.putString("imgurl", imgurl);
        Fragment fragment = new FullBlogDetailShow();

        fragment.setArguments(bundle);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.full_blog_detail_show, container, false);

        prefs = getApplicationContext().getSharedPreferences(prefName, MODE_PRIVATE);
        languale = prefs.getString(TEXT_VALUE_KEY, null);
        Bundle b = getArguments();

        mTitle = (TextView) view.findViewById(R.id.blog_detial_tittle_text);
        tittle1 = b.getString("tittle");
        //  mTitle.setText(tittle1);
        mWriter = (TextView) view.findViewById(R.id.blog_detial_writer_name);
        writer = b.getString("writer");
        // mWriter.setText(writer);
        mContext = (TextView) view.findViewById(R.id.blog_detial_content);
        content = b.getString("content");
        //  mContext.setText(content);
        imageView = (ImageView) view.findViewById(R.id.blog_detail_image);
        imgurl = b.getString("imgurl");
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
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mWriter != null) {
                                mWriter.setText(translation2.getTranslatedText());
                            }
                        }
                    });

                    final Translation translation3 =
                            translate.translate(content,
                                    Translate.TranslateOption.targetLanguage(languale));
                    textViewHandler3.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mContext != null) {
                                mContext.setText(translation3.getTranslatedText());
                            }
                        }
                    });
                    return null;
                }
            }.execute();
        }


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                    fragment = new BlogFragment();
                    fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();
                    Toast.makeText(getActivity(), "Back press", Toast.LENGTH_SHORT).show();

                    return true;
                }
                return false;
            }
        });

    }


}
