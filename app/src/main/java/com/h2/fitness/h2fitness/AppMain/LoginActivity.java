package com.h2.fitness.h2fitness.AppMain;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.goldrushcomputing.inapptranslation.InAppTranslation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h2.fitness.h2fitness.AdminPanel.activity.AdminPanel;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.databinding.ActivityLoginBinding;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_STORAGE_PERMISSION = 2;
    private static final int REQUEST_READ_PERMISSION1 = 3;
    private static final int RC_SIGN_IN = 123;
    private static final String TEXT_VALUE_KEY = "languageValue";
    ActivityLoginBinding binding;
    public EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset, signInGoogleButton;
    private String prefName = "My Pref";
    private SharedPreferences prefs;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        String language = prefs.getString(TEXT_VALUE_KEY, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //   InAppTranslation.setSourceLanguage("en");
        InAppTranslation.clearCache(this);
        InAppTranslation.setTargetLanguage(language);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              permission();
        }

        getSupportActionBar().hide();
        //checkConnection();
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        // set the view now


        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);
        signInGoogleButton = (Button) findViewById(R.id.socialmedia);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, SignupActivity.class)));

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(v -> LoginWithEamil());

        signInGoogleButton.setOnClickListener(view -> startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(true)
                        .setAvailableProviders(Arrays.asList(
                                //new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.FacebookBuilder().build(),
                                new AuthUI.IdpConfig.TwitterBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()


                        ))
                        .build(), RC_SIGN_IN));

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String id = user.getUid();
                //  Uri imgurl= user.getPhotoUrl();
                String mail = user.getEmail();
                DatabaseReference mnew_user = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_USER + id);
                mnew_user.child("user_id").setValue(id);
                mnew_user.child("name").setValue(mail);
                mnew_user.child("status").setValue("HI , I'm using health  App.");
                mnew_user.child("user_image").setValue("default");
                mnew_user.child("thumb_image").setValue("default");


                startActivity(new Intent(LoginActivity.this, MainScreen.class));
                finish();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Log.e("Login", "Login canceled by User");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Log.e("Login", "No Internet Connection");
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Log.e("Login", "Unknown Error");
                    return;
                }
            }
            Log.e("Login", "Unknown sign in response");
        }
    }


    public void LoginWithEamil() {

        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    progressBar.setVisibility(View.GONE);
                    if (!task.isSuccessful()) {
                        // there was an error
                        if (password.length() < 6) {
                            inputPassword.setError(getString(R.string.minimum_password));
                        } else {
                            Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (email.equals("admin@gmail.com")) {
                            Intent intent = new Intent(LoginActivity.this, AdminPanel.class);
                            intent.putExtra("email",email);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("adminEmail",email);
                            editor.commit();
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainScreen.class);
                            intent.putExtra("email",email);
                            startActivity(intent);
                            finish();
                        }

                    }
                });


    }
// checkConnection() was commented
    /*private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }*/

    private void showSnack(boolean isConnected) {
        String message;
        int color;
        if (isConnected) {
            message = "Online";
            color = Color.WHITE;
            Toast.makeText(LoginActivity.this, "Online", Toast.LENGTH_LONG).show();
        } else {
            message = "Offline";

            Toast.makeText(LoginActivity.this, "Offline", Toast.LENGTH_LONG).show();
        }

        //    Snackbar snackbar = Snackbar
        //          .make(findViewById(R.id.coordinator_layout), message, Snackbar.LENGTH_LONG);

//        View sbView = snackbar.getView();
        //      TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        //    textView.setTextColor(color);
        //  snackbar.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.GET_ACCOUNTS)) {
                Toast.makeText(getApplicationContext(), "Permission to read Storage", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.GET_ACCOUNTS}, REQUEST_STORAGE_PERMISSION);
        }

    }



}