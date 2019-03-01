package com.h2.fitness.h2fitness.AppMain.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.h2.fitness.h2fitness.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by HP on 11/20/2017.
 */

public class PagerViewFragment extends Fragment {

    private static final String TEXT_VALUE_KEY = "languageValue";
    final Handler textViewHandler3 = new Handler();
    public ViewPager mViewPager;
    public TabLayout tabLayout;
    public Context mContex;
    String languale;
    String prefName = "My Pref";
    private SharedPreferences prefs;

    public static Fragment createFragment() {
        Bundle bundle = new Bundle();
        Fragment fragment = new PagerViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("StaticFieldLeak")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.pager_view_fragment, container, false);
        mViewPager = view.findViewById(R.id.viewPager);
        setupViewPager(mViewPager);
        prefs = getContext().getSharedPreferences(prefName, MODE_PRIVATE);
        languale = prefs.getString(TEXT_VALUE_KEY, null);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new WorkOutFragment(), "WORK OUT");
        adapter.addFragment(new FlexibilityFragment(), " FLEXIBILTY ");
        adapter.addFragment(new WeightTrainingFragment(), " WEIGHT TRAINING");
        adapter.addFragment(new WeeklyProgramFragment(), "WEEKLY PROGRAM");
        adapter.addFragment(new AbsFragment(), "ABS");
        adapter.addFragment(new ChatFragment(), "CHAT");
        adapter.addFragment(new BlogFragment(), "BLOG");

        viewPager.setAdapter(adapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    Toast.makeText(mContex, "back click", Toast.LENGTH_LONG).show();

                    //    FragmentManager fragmentManager = getFragmentManager();
                    //  Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_holder);
                    //   fragment = UserBoardFragment.createFragment();
                    //    fragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit();


                    return true;
                }
                return false;
            }
        });

    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
