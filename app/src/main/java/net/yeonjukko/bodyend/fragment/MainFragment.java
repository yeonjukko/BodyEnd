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
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

public class MainFragment extends Fragment {
    int MAX_PAGE = 2;
    private StimulusFragment mStimulusFragment;
    private RecordFragment mRecordFragment;
    private Fragment curFragment;
    private MaterialAccount account;
    UserInfoModel userInfoModel;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_main, null, false);
        final ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        userInfoModel = new DBmanager(getContext()).selectUserInfoDB();

        //null adapter 방지를 위해 fragment객체를 미리 생성
        mStimulusFragment = new StimulusFragment();
        mRecordFragment = new RecordFragment();

        viewPager.setAdapter(new Adapter(getActivity().getSupportFragmentManager()));

        //캘린더를 통해 접근한 경우는 1페이지로 바로 접근
        Intent intent = getActivity().getIntent();
        if (intent.getIntExtra("showDate", 0) != 0) {
            viewPager.setCurrentItem(1);
        }

       

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
                    return mStimulusFragment;
                case 1:
                    return mRecordFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }


}
