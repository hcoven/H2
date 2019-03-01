package com.h2.fitness.h2fitness.Calender;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h2.fitness.h2fitness.Calender.calanderEvent.MyEventDay;
import com.h2.fitness.h2fitness.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class CalanderAcitivity extends AppCompatActivity {

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String mid = user.getUid();
    private CalendarView mCalendarView;
    private List<EventDay> mEventDays = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calander_acitivity);
        mCalendarView = (CalendarView) findViewById(R.id.calendarView);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(v -> addNote());


        mCalendarView.setOnDayClickListener(eventDay -> {


            try {

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("notes").child(mid);
                ref.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Get map of users in datasnapshot
                                try {
                                    collectPhoneNumbers((Map<String, Object>) dataSnapshot.getValue(), eventDay);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                //handle databaseError
                            }
                        });
                     previewNote(eventDay);//was commented

            } catch (Exception e) {
                e.fillInStackTrace();
                Toast.makeText(getApplicationContext(), "NO Notes, make notes", Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
            MyEventDay myEventDay = data.getParcelableExtra(RESULT);
            mCalendarView.setDate(myEventDay.getCalendar());
            mEventDays.add(myEventDay);
            mCalendarView.setEvents(mEventDays);
        }
    }

    private void addNote() {
        Intent intent = new Intent(this, AddNotesActivity.class);

        startActivityForResult(intent, ADD_NOTE);
    }

    private void previewNote(EventDay eventDay) {
        Intent intent = new Intent(this, NotesPreviewActivity.class);
        if (eventDay instanceof MyEventDay) {
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        startActivity(intent);
    }


    private void collectPhoneNumbers(Map<String, Object> users, EventDay eventDay) throws ParseException {

        ArrayList<Long> phoneNumbers = new ArrayList<>();

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();


            String date = (String) singleUser.get("date");
            String note = (String) singleUser.get("note");


            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            Date date1 = (Date) df.parse(date.toString());
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);


            MyEventDay myEventDay = new MyEventDay(cal, 0, note);


            String temp = eventDay.getCalendar().getTime().toString();

            StringTokenizer st = new StringTokenizer(temp, " ");

            String day = st.nextToken();
            String month = st.nextToken();
            String currentdate = st.nextToken();
            String waste = st.nextToken();
            String gmt = st.nextToken();
            String year = st.nextToken();

            String finalvalue = currentdate + "-" + month + "-" + year;

            Log.d("final", finalvalue);

            if (date.equals(finalvalue)) {

                Intent intent = new Intent(this, NotesPreviewActivity.class);
                intent.putExtra("value", note);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "No event ", Toast.LENGTH_LONG).show();
            }


        }
    }


}