package com.h2.fitness.h2fitness.Calender;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.h2.fitness.h2fitness.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesPreviewActivity extends AppCompatActivity {

    public static String getFormattedDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        return df.format(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_preview);
        Intent intent = getIntent();
        TextView note = (TextView) findViewById(R.id.note);
        if (intent != null) {

            String tempnote = intent.getStringExtra("value");
            note.setText(tempnote);
        }
       /*
        if (intent != null) {
            Object event = intent.getParcelableExtra(CalanderAcitivity.EVENT);
            if(event instanceof MyEventDay){
                MyEventDay myEventDay = (MyEventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(myEventDay.getCalendar().getTime()));
                note.setText(myEventDay.getNote());
                return;
            }
            if(event instanceof EventDay){
                EventDay eventDay = (EventDay)event;
                getSupportActionBar().setTitle(getFormattedDate(eventDay.getCalendar().getTime()));
            }
        }
        */
    }
}