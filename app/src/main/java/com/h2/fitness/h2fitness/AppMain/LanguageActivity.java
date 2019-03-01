package com.h2.fitness.h2fitness.AppMain;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.storage.PrefManager;

public class LanguageActivity extends AppCompatActivity {

    private static final String TEXT_VALUE_KEY = "languageValue";
    Spinner mSpinner;
    Button mSubmitButton;
    String mLanguageSelected;
    private SharedPreferences prefs;
    private String prefName = "My Pref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        getSupportActionBar().hide();
        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSubmitButton = (Button) findViewById(R.id.lanugage_submit);
        //     mSubmitButton.setEnabled(false);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getApplicationContext(),
                R.array.language_name, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                mLanguageSelected = mSpinner.getSelectedItem().toString();
                PrefManager pref = new PrefManager(getApplicationContext());
                pref.setLanguage(mLanguageSelected);
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

        mSubmitButton.setOnClickListener(view -> {
            if (mLanguageSelected.equals("Select Language")) {
                Toast.makeText(getApplicationContext(), "Please select the language ", Toast.LENGTH_SHORT).show();

            } else {
                String spilt[] = mLanguageSelected.split(" ");
                String m1 = spilt[0];
                String m2 = spilt[1];
                prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(TEXT_VALUE_KEY, m2);
                editor.commit();
                Intent intent1 = getIntent();
                String id = intent1.getStringExtra("value");
                if (id == null) {
                    Intent intent = new Intent(LanguageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(LanguageActivity.this, MainScreen.class);
                    startActivity(intent);
                    finish();
                }


            }

        });

    }
}
