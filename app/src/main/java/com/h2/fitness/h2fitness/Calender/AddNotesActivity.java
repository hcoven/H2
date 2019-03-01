package com.h2.fitness.h2fitness.Calender;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import com.applandeo.materialcalendarview.CalendarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.Calender.calanderEvent.MyEventDay;
import com.h2.fitness.h2fitness.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Adini on 5/6/2018.
 */

public class AddNotesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_notes);

        final CalendarView datePicker = (CalendarView) findViewById(R.id.datePicker);
        Button button = (Button) findViewById(R.id.addNoteButton);
        final EditText noteEditText = (EditText) findViewById(R.id.noteEditText);
        button.setOnClickListener(v -> {
            Intent returnIntent = new Intent();

            MyEventDay myEventDay = new MyEventDay(datePicker.getSelectedDate(),
                    R.drawable.plus, noteEditText.getText().toString());

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String id = user.getUid();
            //  Uri imgurl= user.getPhotoUrl();
            String mail = user.getEmail();
            String name = user.getDisplayName();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            Date c = Calendar.getInstance().getTime();
            String formattedDate = df.format(c);
            String ref = Config.FIREBASE_NOTES + id + "/";
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);
            Map mParent = new HashMap();
            mParent.put("note", noteEditText.getText().toString());
            mParent.put("date", formattedDate);
            mdata.push().setValue(mParent);
            returnIntent.putExtra(CalanderAcitivity.RESULT, myEventDay);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }
}
