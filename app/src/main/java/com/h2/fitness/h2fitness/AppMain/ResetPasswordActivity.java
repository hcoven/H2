package com.h2.fitness.h2fitness.AppMain;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.goldrushcomputing.inapptranslation.InAppTranslation;
import com.google.firebase.auth.FirebaseAuth;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.databinding.ActivityResetPasswordBinding;


public class ResetPasswordActivity extends AppCompatActivity {

    private static final String TEXT_VALUE_KEY = "languageValue";
    ActivityResetPasswordBinding binding;
    private EditText inputEmail;
    private Button btnReset, btnBack;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private String prefName = "My Pref";
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        String language = prefs.getString(TEXT_VALUE_KEY, null);
        //   InAppTranslation.setSourceLanguage("en");
        InAppTranslation.clearCache(this);

        InAppTranslation.setTargetLanguage(language);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password);
        //   setContentView(R.layout.activity_reset_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        getSupportActionBar().hide();
        inputEmail = (EditText) findViewById(R.id.email);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnBack = (Button) findViewById(R.id.btn_back);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        auth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(v -> finish());

        btnReset.setOnClickListener(v -> {

            String email = inputEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }

                        progressBar.setVisibility(View.GONE);
                    });
        });
    }

}
