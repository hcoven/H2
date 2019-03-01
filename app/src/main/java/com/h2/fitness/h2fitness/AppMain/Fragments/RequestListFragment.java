package com.h2.fitness.h2fitness.AppMain.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.AppMain.Model.FriendRequestList;
import com.h2.fitness.h2fitness.AppMain.Model.Users;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.AppMain.ProfileActivity;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RequestListFragment extends Fragment {


    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String mCurrentName = "tempname";
    Users valueusers = new Users();
    Context mContex;
    String mId = user.getUid();
    private FirebaseAuth auth;
    private ProgressDialog mProgressDialog;
    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mRootRef;
    private DatabaseReference mFriendDatabase;
    private RecyclerView mUsersList;
    private DatabaseReference mUsersDatabase;
    private LinearLayoutManager mLayoutManager;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new AbsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friend_list, container, false);
        //   mRootRef = FirebaseDatabase.getInstance().getReference();
        // mFriendReqDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_FRIEND_REQUEST).child(mId);


        //  mUsersDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_FRIEND_REQUEST);

        mLayoutManager = new LinearLayoutManager(mContex);
        mUsersList = (RecyclerView) view.findViewById(R.id.users_list);
        mUsersList.setHasFixedSize(true);
        mUsersList.setLayoutManager(mLayoutManager);

        FirebaseRecyclerAdapter<FriendRequestList,
                RequestListFragment.UsersViewHolders>
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter
                <FriendRequestList, RequestListFragment.UsersViewHolders>(

                FriendRequestList.class,
                R.layout.users_single_layout,
                RequestListFragment.UsersViewHolders.class,
                mUsersDatabase

        ) {
            @Override
            protected void populateViewHolder(RequestListFragment.UsersViewHolders usersViewHolder, FriendRequestList users, int position) {
                final String request_type = users.getRequest_type();
                final String emailId = users.getUser_email();

                usersViewHolder.setDisplayName(emailId);
                usersViewHolder.setUserStatus(request_type);


                final String user_id = getRef(position).getKey();


                usersViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
                        profileIntent.putExtra("user_id", user_id);
                        //   profileIntent.putExtra("user_email",request_type);
                        //   profileIntent.putExtra("user_status",tempuserId);


                        startActivity(profileIntent);
                        Toast.makeText(mContex, user_id + " :::: tyeis ::: " + request_type, Toast.LENGTH_LONG).show();

                    }
                });

            }
        };


        mUsersList.setAdapter(firebaseRecyclerAdapter);


        return view;
    }

    private void collectDataFromUserDatabase(Map<String, Object> value) {

        for (Map.Entry<String, Object> entry : value.entrySet()) {

            //Get user map
            Map singleUser = (Map) entry.getValue();

            String email = (String) singleUser.get(Config.USER_EMAIL);
            String status = (String) singleUser.get(Config.USER_STATUS);
            String image = (String) singleUser.get(Config.USER_IMAGE);
            //Get phone field and append to list
            //   paymentRecordsList.add((String) singleUser.get("mId"));
            valueusers.setName(email);
            valueusers.setStatus(status);
            //       Toast.makeText(getApplicationContext(),email+ status,Toast.LENGTH_LONG).show();

        }


    }

    public static class UsersViewHolders extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolders(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(String name) {

            TextView userNameView = (TextView) mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserStatus(String status) {

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.default_avatar).into(userImageView);

        }


    }


}