package com.h2.fitness.h2fitness.AppMain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

public class splishScreen extends AppCompatActivity {
    private static final String TEXT_VALUE_KEY = "languageValue";
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private SharedPreferences prefs;
    private String prefName = "My Pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        //printhashkey();
        PrefManager pref = new PrefManager(getApplicationContext());

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity

            auth = FirebaseAuth.getInstance();

            if (auth.getCurrentUser() != null) {
                String mid = auth.getCurrentUser().getUid();
                String email = auth.getCurrentUser().getEmail();
                if (email.equals("admin@gmail.com")) {
                    startActivity(new Intent(splishScreen.this, com.h2.fitness.h2fitness.AdminPanel.activity.AdminPanel.class));
                    finish();
                } else if (pref.gettype() == null) {

                    Intent i = new Intent(splishScreen.this, SelectType.class);
                    i.putExtra("user", "user");
                    startActivity(i);
                    finish();
                } else {
                    startActivity(new Intent(splishScreen.this, MainScreen.class));
                    finish();
                }
            } else if (prefs.getString(TEXT_VALUE_KEY, null) == null) {

                startActivity(new Intent(splishScreen.this, LanguageActivity.class));
                finish();
            } else {
                startActivity(new Intent(splishScreen.this, LoginActivity.class));
                finish();
            }

            finish();
        }, 1000);
    }


    /*public void printhashkey() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mytrendin.keyhash",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    Log.d("key1", Base64.encodeToString(md.digest(), Base64.DEFAULT));
                }
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }*/

}


