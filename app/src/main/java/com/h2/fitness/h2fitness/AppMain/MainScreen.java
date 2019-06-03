package com.h2.fitness.h2fitness.AppMain;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h2.fitness.h2fitness.Calender.CalanderAcitivity;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.AppMain.Fragments.FriendPagerViewFragment;
import com.h2.fitness.h2fitness.AppMain.Fragments.PagerViewFragment;
import com.h2.fitness.h2fitness.AppMain.Model.Payment;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.Utils.DateUtils.DateTimeUnits;
import com.h2.fitness.h2fitness.Utils.DateUtils.DateTimeUtils;
import com.h2.fitness.h2fitness.alarm.AActivity;
import com.h2.fitness.h2fitness.storage.PrefManager;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.trialy.library.Trialy;
import io.trialy.library.TrialyCallback;
import mp.MpUtils;
import mp.PaymentRequest;
import mp.PaymentResponse;

import static io.trialy.library.Constants.STATUS_TRIAL_JUST_ENDED;
import static io.trialy.library.Constants.STATUS_TRIAL_JUST_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_NOT_YET_STARTED;
import static io.trialy.library.Constants.STATUS_TRIAL_OVER;
import static io.trialy.library.Constants.STATUS_TRIAL_RUNNING;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int REQUEST_CODE = 1234; // Can be anything
    private static String SERVICE_ID = "4e6406b82d8cb3bf5f84c0ad2c8cc242";
    private static String APP_SECRET = "ee9de32bfa326e54c5587c8af5f6c52a";
    FirebaseAuth auth;
    String mCurrentUserId;
    String mCurrentUserName = "";
    String mCurrentmail = "";
    boolean checking = false;


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String userid;
    boolean ver = false, ver2 = false;
    private TrialyCallback mTrialyCallback = (status, timeRemaining, sku) -> {
        switch (status) {
            case STATUS_TRIAL_JUST_STARTED:
                //The trial has just started - enable the premium features for the user
                Log.d("start_version", "start trial_version : " + timeRemaining);
                break;
            case STATUS_TRIAL_RUNNING:
                Log.d("start_version", "trial version is running : " + timeRemaining);
                //The trial is currently running - enable the premium features for the user
                PrefManager pref = new PrefManager(getApplicationContext());
                String version = pref.getVersion();

                if (version != null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                    if (fragment == null) {

                        fragment = createFragment();

                        fragmentManager.beginTransaction().add(R.id.fragment_holder, fragment).commit();
                    }

                } else {


                    CheckpaidTime();

                    if (ver == false) {

                        selfDestruct();

                    }
                }
                break;
            case STATUS_TRIAL_JUST_ENDED:
                Log.d("start_version", "trial version is end");
                dialogbox();
                //The trial has just ended - block access to the premium features
                break;
            case STATUS_TRIAL_NOT_YET_STARTED:

                Log.d("start_version", "trial version is not started yet");
                //The user hasn't requested a trial yet - no need to do anything
                selfDestruct();
                break;
            case STATUS_TRIAL_OVER:

                Log.d("start_version", "trial version time  ended ");
                checking = true;
                CheckpaidTime();
                break;
        }

        Log.i("TRIALY", "Returned status: " + Trialy.getStatusMessage(status));
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  checkAndRequestPermissions();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }

//        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        String id=  user.getUid();
//        String mail =   user.getEmail();
//        String name = user.getDisplayName();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date c = Calendar.getInstance().getTime();
//        String formattedDate = df.format(c);
//        DatabaseReference mnew_user  = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_PAYMENT+id);
//        mnew_user.child("user_id").setValue(id);
//        mnew_user.child("name").setValue(name);
//        mnew_user.child("email" ).setValue(mail);
//        mnew_user.child("version").setValue("paid");
//        mnew_user.child("time").setValue(formattedDate);


        try {

            CheckpaidTime();

        } catch (Exception e) {
            e.fillInStackTrace();
        }

//
//
        if (ver == false) {
            Trialy mTrialy = new Trialy(this, "VBA1JCKJ9060K9I98KC");
            mTrialy.startTrial("default", mTrialyCallback);
        }


         //mTrialy.checkTrial("default", mTrialyCallback);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    Fragment createFragment() {

        //  return new MainFragment();

        // if show the old view
        return new PagerViewFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_sigout: {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                if (auth.getCurrentUser() == null) {
                    Intent i = new Intent(MainScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {

                    Toast.makeText(MainScreen.this, "error while logout ", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case R.id.menu_lanugage: {

                Intent i = new Intent(MainScreen.this, LanguageActivity.class);
                i.putExtra("value", "value");
                startActivity(i);
                finish();

                break;
            }

            case R.id.menu_friend_invite: {


                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Health App!");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I am using this app join me  Click here: http://www.yourdomain.com");

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);

                break;
            }

            case R.id.menu_add_friend: {

                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                fragment = FriendPagerViewFragment.createFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();

                break;
            }

            case R.id.menu_type: {

                Intent i = new Intent(MainScreen.this, SelectType.class);
                i.putExtra("user", "user");
                startActivity(i);
                finish();
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_calander) {
            Intent i = new Intent(MainScreen.this, CalanderAcitivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_alarm) {

            Intent intent = new Intent(MainScreen.this, AActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_about_us) {

        } else if (id == R.id.nav_sign_out) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void selfDestruct() {

        new FancyAlertDialog.Builder(this)
                .setTitle(" Version  Detail")
                .setBackgroundColor(Color.parseColor("#A0A0A0"))  //Don't pass R.color.colorvalue
                .setMessage("Please select the version")
                .setNegativeBtnText("Full Version")
                .setPositiveBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Trial Version")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(() -> {

                    PrefManager pref = new PrefManager(getApplicationContext());
                    pref.setVersion("version");

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);

                    //if show the old view
                    fragment = PagerViewFragment.createFragment();

                    //fragment = MainFragment.getInstance();


                    fragmentManager.beginTransaction().add(R.id.fragment_holder, fragment).commit();


                })
                .OnNegativeClicked(() -> {

                    PaymentRequest.PaymentRequestBuilder builder = new PaymentRequest.PaymentRequestBuilder();
                    builder.setService(SERVICE_ID, APP_SECRET);
                    builder.setDisplayString("iFit");
                    builder.setProductName("personal trainer");  // non-consumable purchases are restored using this value
                    builder.setType(MpUtils.PRODUCT_TYPE_CONSUMABLE);
                    builder.setIcon(R.drawable.ic_alarm);
                    PaymentRequest pr = builder.build();
                    makePayment(pr);

                })
                .build();

    }


    public void dialogbox() {

        new FancyAlertDialog.Builder(this)
                .setTitle("Subscribe")
                .setBackgroundColor(Color.parseColor("#A0A0A0"))  //Don't pass R.color.colorvalue
                .setMessage("Subscribe to Use this service")
                .setPositiveBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Subscribe")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))
                //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(false)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(() -> {
                    PaymentRequest.PaymentRequestBuilder builder = new PaymentRequest.PaymentRequestBuilder();
                    builder.setService(SERVICE_ID, APP_SECRET);
                    builder.setDisplayString("iFit Personal Trainer");
                    builder.setProductName("iFit");  // non-consumable purchases are restored using this value
                    builder.setType(MpUtils.PRODUCT_TYPE_CONSUMABLE);
                    builder.setIcon(R.drawable.ic_add);
                    PaymentRequest pr = builder.build();
                    makePayment(pr);

                })

                .build();

    }

    protected final void makePayment(PaymentRequest payment) {
        startActivityForResult(payment.toIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                return;
            }

            // OK
            if (resultCode == RESULT_OK) {
                super.onActivityResult(requestCode, resultCode, data);
                PaymentResponse response = new PaymentResponse(data);

                switch (response.getBillingStatus()) {
                    case MpUtils.MESSAGE_STATUS_BILLED:

                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String id = user.getUid();
                        String mail = user.getEmail();
                        String name = user.getDisplayName();
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date c = Calendar.getInstance().getTime();
                        String formattedDate = df.format(c);
                        DatabaseReference mnew_user = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_PAYMENT + id);
                        mnew_user.child("user_id").setValue(id);
                        mnew_user.child("name").setValue(name);
                        mnew_user.child("email").setValue(mail);
                        mnew_user.child("version").setValue("paid");
                        mnew_user.child("time").setValue(formattedDate);


                        try {
                            FragmentManager fragmentManager = getSupportFragmentManager();

                            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                            if (fragment == null) {

                                fragment = createFragment();

                                fragmentManager.beginTransaction().add(R.id.fragment_holder, fragment).commit();
                            }
                        } catch (Exception e) {
                            e.fillInStackTrace();
                            Toast.makeText(getApplicationContext(), "error in showing view ", Toast.LENGTH_LONG).show();
                        }

                        break;
                    case MpUtils.MESSAGE_STATUS_FAILED:
                        // ...

                        Toast.makeText(getApplicationContext(), "bill failed ", Toast.LENGTH_LONG).show();
                        dialogbox();
                        break;
                    case MpUtils.MESSAGE_STATUS_PENDING:
                        // ...

                        Toast.makeText(getApplicationContext(), "bill is pending ", Toast.LENGTH_LONG).show();
                        break;
                }
            } else {
                Toast.makeText(getApplicationContext(), "not found  ", Toast.LENGTH_LONG).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    /*

    private boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

    */


    private void CheckpaidTime() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userid = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl(Config.FIREBASE_PAYMENT + userid);

// Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Payment post = dataSnapshot.getValue(Payment.class);

                try {
                    String time = post.getTime();


                    if (time != null) {
                        ver = true;
                        ver2 = true;
                        int days = DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate(time), DateTimeUnits.DAYS);
                        Log.d("temp_day", "Diff in days >> " +
                                DateTimeUtils.getDateDiff(new Date(), DateTimeUtils.formatDate(time), DateTimeUnits.DAYS));


                        if (days > 1) {

                            dialogbox();

                        } else {
                            FragmentManager fragmentManager = getSupportFragmentManager();

                            Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                            if (fragment == null) {

                                fragment = PagerViewFragment.createFragment();

                                //if show new view
                                //fragment = MainFragment.getInstance();

                                fragmentManager.beginTransaction().add(R.id.fragment_holder, fragment).commit();

                            }
                        }
                    }


                } catch (Exception e) {
                    e.fillInStackTrace();

                }

                if (checking == true && ver2 == false) {
                    dialogbox();
                } else {
                    Toast.makeText(getApplicationContext(), "hello world ", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }


}
