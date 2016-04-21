package net.yeonjukko.bodyend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.fragment.RecordFragment;
import net.yeonjukko.bodyend.fragment.StimulusFragment;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

public class MainFragment extends Fragment {
    int MAX_PAGE = 2;
    private Fragment cur_fragment;
    public Fragment record_fragment;
    private MaterialAccount account;
    UserInfoModel userInfoModel;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main, null, false);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new Adapter(getActivity().getSupportFragmentManager()));
        userInfoModel = new DBmanager(getContext()).selectUserInfoDB();
        Intent intent = getActivity().getIntent();
        if (intent.getIntExtra("showDate", 0) != 0) {
            viewPager.setCurrentItem(1);
        }
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {

                    if (((RecordFragment) record_fragment).adapter != null) {
                        Log.d("mox", "onPageSelected");
                        ((RecordFragment) record_fragment).adapter.notifyDataSetChanged();
                    } else {
                        Log.d("mox", "null");

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return rootView;
    }

    private class Adapter extends FragmentPagerAdapter {
        public Adapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position < 0 || MAX_PAGE <= position) {
                return null;
            }
            switch (position) {

                case 0:
                    cur_fragment = new StimulusFragment();
                    break;
                case 1:
                    cur_fragment = new RecordFragment();
                    record_fragment = cur_fragment;
                    break;


            }

            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }


}
