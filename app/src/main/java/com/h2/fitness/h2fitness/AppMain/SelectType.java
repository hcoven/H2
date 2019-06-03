package com.h2.fitness.h2fitness.AppMain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.h2.fitness.h2fitness.AdminPanel.activity.AdminRecycleViewActivity;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.util.Objects;


public class SelectType extends AppCompatActivity {


    private Spinner mSpinner;
    private Button mSubmitButton;
    private String mLanguageSelected;
    private SharedPreferences prefs;
    //String userType = "";
    private EditText inputEmail, inputPassword;
    private String email, email1;
    private String prefName = "My Pref";
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mSpinner = findViewById(R.id.spinner_type);
        mSubmitButton = findViewById(R.id.sumbit_type);
        mSubmitButton.setEnabled(true); // was commented

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;
        email1 = mUser.getEmail();

        Intent i = getIntent();
        if (i != null) email = i.getStringExtra("");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getApplicationContext(),
                R.array.video_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                mLanguageSelected = mSpinner.getSelectedItem().toString();


                Toast.makeText(getApplicationContext(), mLanguageSelected, Toast.LENGTH_SHORT).show();

                // if (mSpinner.getSelectedItem().toString().trim().equals("Select Lanugage")) {
                //     mSubmitButton.setEnabled(false);
                // }
                //   mSubmitButton.setEnabled(true);
            }


            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        });

        mSubmitButton.setOnClickListener((View view) -> {

            if (mLanguageSelected.equals("select")) {
                Toast.makeText(getApplicationContext(), "Please select difficulty first ", Toast.LENGTH_SHORT).show();

            } else if (email1.equalsIgnoreCase("admin@gmail.com")) {
                Log.d("admin", email1);
                PrefManager pref = new PrefManager(getApplicationContext());
                pref.setType(mLanguageSelected);
                Intent intent = new Intent(SelectType.this, AdminRecycleViewActivity.class);
                startActivity(intent);
            } else {
                PrefManager pref = new PrefManager(getApplicationContext());
                pref.setType(mLanguageSelected);
                Intent intent = new Intent(SelectType.this, MainScreen.class);
                startActivity(intent);
            }

        });

    }
}

