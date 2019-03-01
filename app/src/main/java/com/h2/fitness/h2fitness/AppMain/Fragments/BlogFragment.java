package com.h2.fitness.h2fitness.AppMain.Fragments;

import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.goldrushcomputing.inapptranslation.InAppTranslation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.h2.fitness.h2fitness.Blog.BlogListAdapter;
import com.h2.fitness.h2fitness.Constrain.Config;
import com.h2.fitness.h2fitness.Blog.Blog;
import com.h2.fitness.h2fitness.Blog.BlogList;
import com.h2.fitness.h2fitness.R;
import com.h2.fitness.h2fitness.Utils.MyDividerItemDecoration;
import com.h2.fitness.h2fitness.AppMain.MainActivity;
import com.h2.fitness.h2fitness.databinding.FragmentBlogBinding;
import com.h2.fitness.h2fitness.storage.PrefManager;

import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

//import net.azurewebsites.irrationnelle.testandroidstudio.databinding.MainRecyclerItemsBinding;


/**
 * Created by HP on 11/20/2017.
 */

public class BlogFragment extends Fragment {

    private static final String URL = "https://api.androidhive.info/json/shimmer/menu.php";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TEXT_VALUE_KEY = "languageValue";
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public SharedPreferences prefs;
    FragmentBlogBinding binding;
    String lanugae;
    List<Blog> mAllResultOfBlog;
    BlogList blogList = BlogList.getInstance();
    String mId = user.getUid();
    private RecyclerView recyclerView;
    private List<Blog> cartList;
    private String prefName = "My Pref";
    private BlogListAdapter mAdapter;
    private ShimmerFrameLayout mShimmerViewContainer;

    public static Fragment getInstance() {
        Bundle bundle = new Bundle();
        Fragment fragment = new BlogFragment();
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_blog, container, false);
        View view = binding.getRoot();
        prefs = getApplicationContext().getSharedPreferences(prefName, MODE_PRIVATE);
        String language = prefs.getString(TEXT_VALUE_KEY, null);
        InAppTranslation.clearCache(getApplicationContext());
        InAppTranslation.setTargetLanguage(language);
        //   View view = inflater.inflate(R.layout.fragment_blog, container, false);

        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        //   mShimmerViewContainer.startShimmerAnimation();
        mAllResultOfBlog = blogList.getTasks();
        recyclerView = view.findViewById(R.id.recycler_view);
        //   cartList = new ArrayList<>();
        collectingData();
        mAdapter = new BlogListAdapter(mAllResultOfBlog);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);


        //     fetchRecipes();

        return view;
    }


    private void collectingData() {
        if (!(mId == null)) {


            String ref = Config.FIREBASE_BLOG_FILES;
            DatabaseReference mdata = FirebaseDatabase.getInstance().getReferenceFromUrl(ref);

            mdata.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    collectPhoneNumbers((Map<String,Object>) dataSnapshot.getValue());
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

        blogList.removeResultList();
        try {

            for (Map.Entry<String, Object> entry : value.entrySet()) {

                //Get user map
                Map singleUser = (Map) entry.getValue();

                String imageName = (String) singleUser.get(Config.FIREBASE_IMAGE_NAME);
                //     imageName = translate(imageName,lanugae);
                String imageUrl = (String) singleUser.get(Config.FIREBASE_IMAGE_URL);
                String tittle = (String) singleUser.get(Config.FIREBASE_TITTLE);
                //  tittle = translate(tittle,lanugae);
                String content = (String) singleUser.get(Config.FIREBASE_DESCRIPTION);
                //  content = translate(content,lanugae);
                String writerName = (String) singleUser.get(Config.FIREBASE_NAME);
                //   writerName = translate(writerName,lanugae);


                Blog blog = new Blog();
                blog.setContent(content);
                blog.setImageName(imageName);
                blog.setImageUrl(imageUrl);
                blog.setName(writerName);
                blog.setTittle(tittle);

                blogList.addResult(blog);

                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);


                updateUI();


            }

        } catch (Exception e) {
        }

    }

    private void updateUI() {


        if (mAdapter != null) {

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            mAllResultOfBlog = blogList.getTasks();
            PrefManager pref = new PrefManager(getApplicationContext());
            pref.clearSession();
            pref.createfile("blog");
            lanugae = pref.getLanguageDetails();
            collectingData();

        }

    }
/*
    private String  translate(String textToTranslate, String targetLanguage) {
        try {
            TranslateOptions options = TranslateOptions.newBuilder()
                    .setApiKey("AIzaSyBoIR0Jruha8SuqkZQNCJgV4Blbj8dRiBE")
						.build();
            Translate trService = options.getService();
            Translation translation = trService.translate(textToTranslate, Translate.TranslateOption.targetLanguage(targetLanguage));
           // callback.onSuccess(translation.getTranslatedText());
            String  t = translation.getTranslatedText();
            return t ;
        }
        catch(Exception e) {

        }
        return "teem";
    }

*/


    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(getApplicationContext(), "back click", Toast.LENGTH_LONG).show();

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                    //fragment = MainFragment.getInstance();
                    fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();


                    return true;
                }
                return false;
            }
        });

    }


}
