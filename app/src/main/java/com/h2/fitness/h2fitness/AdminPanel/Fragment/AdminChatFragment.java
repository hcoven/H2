package com.h2.fitness.h2fitness.AdminPanel.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h2.fitness.h2fitness.Adobter.FriendListAdober;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.AppMain.Fragments.FriendPagerViewFragment;
import com.h2.fitness.h2fitness.AppMain.Model.UserList;
import com.h2.fitness.h2fitness.AppMain.Model.Users;
import com.h2.fitness.h2fitness.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

//import com.google.android.gms.appinvite.AppInviteInvitation;

/**
 * Created by HP on 11/20/2017.
 */

public class AdminChatFragment extends Fragment {


    public static final int REQUEST_INVITE = 100;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String mFriendsKey;
    String INVITATION_TITLE = "Call your friends",
            INVITATION_MESSAGE = "Hey! check this awesome fitness app? :)",
            INVITATION_CALL_TO_ACTION = "Share";
    CallbackManager sCallbackManager;
    String mId = user.getUid();
    UserList mUserList = UserList.getInstance();
    Button mAddFriends, mInviteFreind;
    RecyclerView recyclerView;
    ProgressDialog pdialog;
    String method;
    private DatabaseReference mFriendsDatabase;
    private DatabaseReference mUsersDatabase;
    private FriendListAdober mFriendListAdopter;

    public AdminChatFragment() {
        // Required empty public constructor
    }

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new AdminChatFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.chat_fragment, container, false);


        mAddFriends = (Button) view.findViewById(R.id.addFriends);
        mInviteFreind = (Button) view.findViewById(R.id.inviteFriends);
        recyclerView = (RecyclerView) view.findViewById(R.id.friendList);
        //  mAuth = FirebaseAuth.getInstance();
        //  mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("Friends").child(mId);
        mFriendsDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mUsersDatabase.keepSynced(true);


        //    friendListData();
        List<Users> mFirnedListInfo = mUserList.getTasks();
        mFriendListAdopter = new FriendListAdober(mFirnedListInfo);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mFriendListAdopter);


        mAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                fragment = FriendPagerViewFragment.createFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment_holder_admin, fragment).commit();
            }
        });
        mInviteFreind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(method.equals("facebook")) {
                //   Toast.makeText(getContext(), "facebook method", Toast.LENGTH_LONG).show();
                //  openDialogInvite(ChatFragment.this);

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Android Health App!");
                shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I am using this app join me  Click here: http://www.yourdomain.com");

                Intent chooserIntent = Intent.createChooser(shareIntent, "Share with");
                chooserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chooserIntent);


                /*


                    String appLinkUrl, previewImageUrl;
                    appLinkUrl = "app url(create it from facebook)"; //your applink url
                    previewImageUrl = "image url";//your image url
                    if (AppInviteDialog.canShow()) {
                        AppInviteContent content = new AppInviteContent.Builder()
                                .setApplinkUrl(appLinkUrl)
                                .setPreviewImageUrl(previewImageUrl)
                                .build();
                        AppInviteDialog.show(getActivity(), content);
                    }
                }else
                 if(method.equals("google"))
                   {
                       Toast.makeText(getContext(), "google method", Toast.LENGTH_LONG).show();
                   Intent intent = new AppInviteInvitation.IntentBuilder(INVITATION_TITLE)
                            .setMessage(INVITATION_MESSAGE)
                            .setCallToActionText(INVITATION_CALL_TO_ACTION)

                           .build();
                    startActivityForResult(intent, REQUEST_INVITE);


                }
*/
            }
        });

        //    updateUI();


        //   Toast.makeText(getContext(),"checkinsg 545454544654654654545 ",Toast.LENGTH_LONG).show();


        return view;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("check_me", "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);

        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                    Log.d("check_me", "onActivityResult: sent invitation " + id);
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
    }


    private void updateUI() {


        if (mFriendListAdopter != null) {

            mFriendListAdopter.notifyDataSetChanged();
        }
    }

    private void checkSignINMethod() {
        for (UserInfo user : FirebaseAuth.getInstance().getCurrentUser().getProviderData()) {

            if (user.getProviderId().equals("facebook.com")) {
                //For linked facebook account
                Toast.makeText(getApplicationContext(), "facebook.com", Toast.LENGTH_LONG).show();
                Log.d("check_ok", "User is signed in with Facebook");
                method = "facebook";


            } else if (user.getProviderId().equals("google.com")) {
                //For linked Google account
                Log.d("check_ok", "User is signed in with Google");
                Toast.makeText(getApplicationContext(), "google.com", Toast.LENGTH_LONG).show();
                method = "google";

            } else if (user.getProviderId().equals("twitter.com")) {
                //For linked Twitter account
                Log.d("check_ok", "User is signed in with Twitter");
                Toast.makeText(getApplicationContext(), "twitter.com", Toast.LENGTH_LONG).show();
                method = "twitter";
            }

        }
    }

    private void facebookFriendList() {

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        /* handle the result */
                        Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();
                        try {
                            //
                            JSONObject jObject = response.getJSONObject();
                            JSONArray jArray = jObject.getJSONArray("data");
                            for (int i = 0; i < jArray.length(); i++) {

                                JSONObject jsonObject = jArray.getJSONObject(i);


                                String name = jsonObject.getString("name");
                                String id = jsonObject.getString("id");
                                String imageURL = "http://graph.facebook.com/" + id + "/picture?type=large";

                                Log.e("check_ok", "friend name is " + name);
                                Log.e("check_ok", "friend id is " + id);
                                Log.e("check_ok", "friend imge url is " + imageURL);


                                // UserBoardList.getInstance().removeUserBoard();
                                //     FriendList.getInstance().addResult(task);

                                updateUI();

                            }

                            //   output.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

        ).executeAsync();

        /* make the API call */


    }

    public void openDialogInvite(final Fragment fragment) {
        String AppURl = "http://play.google.com/store/apps/details?id=" + getActivity().getPackageName();  //Generated from //fb developers

        String previewImageUrl = "http://someurl/13_dp.png";

        sCallbackManager = CallbackManager.Factory.create();

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl(AppURl).setPreviewImageUrl(previewImageUrl)
                    .build();

            AppInviteDialog appInviteDialog = new AppInviteDialog(fragment);
            appInviteDialog.registerCallback(sCallbackManager,
                    new FacebookCallback<AppInviteDialog.Result>() {
                        @Override
                        public void onSuccess(AppInviteDialog.Result result) {
                            Log.d("Invitation", "Invitation Sent Successfully");
                            getActivity().finish();
                        }

                        @Override
                        public void onCancel() {
                        }

                        @Override
                        public void onError(FacebookException e) {
                            Log.d("Invitation", "Error Occured");
                        }
                    });

            appInviteDialog.show(content);
        }

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {


            //  mAllResultOfweek= taskList.getTasks();
            //  PrefManager pref = new PrefManager(getApplicationContext());
            //  pref.clearSession();
            //  pref.createfile("weekly");
            friendListData();

        }

    }

    public void friendListData() {

        if (!(mId == null)) {
//            pdialog = ProgressDialog.show(getContext(), "Please Wait...", "Processing...");

            mUserList.removeResultList();
            String ref = Config.FIREBASE_FIREND_USERS + mId;
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("key_value", dataSnapshot.getKey());
                    String mFriendsKey = dataSnapshot.getKey();


                    collectDataFromUserDatabase((Map<String, Object>) dataSnapshot.getValue());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "id is null", Toast.LENGTH_LONG).show();
        }

    }

    private void collectDataFromUserDatabase(Map<String, Object> value) {
        //   boolean found = false;

        //     mUserList.removeResultList();
        try {

            for (Map.Entry<String, Object> entry : value.entrySet()) {

                //Get user map
                String singleUser = entry.getKey();


                mFriendsKey = singleUser;


                mUsersDatabase.child(mFriendsKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //                     pdialog.dismiss();
                        String key = dataSnapshot.getKey();
                        final String userName = dataSnapshot.child("name").getValue().toString();
                        String userThumb = dataSnapshot.child("thumb_image").getValue().toString();
                        String status = dataSnapshot.child("status").getValue().toString();
                        String userId = dataSnapshot.child("user_id").getValue().toString();
                        Log.d("key_value", userName);
                        Log.d("key_value", dataSnapshot.toString());
                        Log.d("key_value", status);
                        Users value = new Users();
                        value.setName(userName);
                        value.setThumb_image(userThumb);
                        value.setStatus(status);
                        value.setUser_id(userId);
                        UserList.getInstance().addResult(value);
                        updateUI();
                        //   if(dataSnapshot.hasChild("online")) {

                        //     String userOnline = dataSnapshot.child("online").getValue().toString();
                        //      friendsViewHolder.setUserOnline(userOnline);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });


            }

        } catch (Exception e) {
        }

    }
}


