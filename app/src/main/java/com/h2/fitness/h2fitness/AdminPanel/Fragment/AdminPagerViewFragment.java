package com.h2.fitness.h2fitness.AdminPanel.Fragment;

import android.os.Bundle;
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

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by HP on 11/20/2017.
 */

public class AdminPagerViewFragment extends Fragment {

    public ViewPager mViewPager;
    public TabLayout tabLayout;

    public static Fragment createFragment() {
        Bundle bundle = new Bundle();
        Fragment fragment = new AdminPagerViewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.admin_pager_view_fragment, container, false);
        mViewPager = view.findViewById(R.id.adminviewPager);
        setupViewPager(mViewPager);

        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new AdminWorkOutFragment(), "WORK OUT");
        adapter.addFragment(new AdminFlexibilityFragment(), " FLEXIBILTY ");
        adapter.addFragment(new AdminWeightTrainingFragment(), " WEIGHT TRAINING");
        adapter.addFragment(new AdminWeeklyProgramFragment(), "WEEKLY PROGRAM");
        adapter.addFragment(new AdminAbsFragment(), "ABS");
        adapter.addFragment(new AdminChatFragment(), "CHAT");
        adapter.addFragment(new AdminBlogFragment(), "BLOG");
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
                    Toast.makeText(getApplicationContext(), "back click", Toast.LENGTH_LONG).show();

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
