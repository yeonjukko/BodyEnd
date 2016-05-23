package net.yeonjukko.bodyend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

public class MainFragment extends Fragment {
    int MAX_PAGE = 2;
    private final int FLAG_PICTURE_FRAGMENT = 0;
    private final int FLAG_RECORD_FRAGMENT = 1;

    private StimulusFragment mStimulusFragment;
    private RecordFragment mRecordFragment;
    UserInfoModel userInfoModel;
    View rootView;
    ViewPager viewPager;
    ImageView ivPic;
    ImageView ivRecord;
    LinearLayout indicator;
    Animation animationFade;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        rootView = inflater.inflate(R.layout.activity_main, null, false);
        setLayout();
        userInfoModel = new DBmanager(getContext()).selectUserInfoDB();

        //null adapter 방지를 위해 fragment객체를 미리 생성
        mStimulusFragment = new StimulusFragment();
        mRecordFragment = new RecordFragment();


        //캘린더를 통해 접근한 경우는 1페이지로 바로 접근
        Intent intent = getActivity().getIntent();
        if (intent.getIntExtra("showDate", 0) != 0) {
            viewPager.setCurrentItem(FLAG_RECORD_FRAGMENT);
        }


        return rootView;

    }

    private void setLayout() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new Adapter(getActivity().getSupportFragmentManager()));
        ivPic = (ImageView) rootView.findViewById(R.id.iv_pic);
        ivRecord = (ImageView) rootView.findViewById(R.id.iv_edit);
        indicator = (LinearLayout) rootView.findViewById(R.id.indicator);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicator.setVisibility(View.VISIBLE);

                switch (position) {
                    case FLAG_PICTURE_FRAGMENT:
                        ivPic.setImageDrawable(getResources().getDrawable(R.drawable.icon_pic_white));
                        ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.icon_edit_black));
                        break;

                    case FLAG_RECORD_FRAGMENT:
                        ivPic.setImageDrawable(getResources().getDrawable(R.drawable.icon_pic_black));
                        ivRecord.setImageDrawable(getResources().getDrawable(R.drawable.icon_edit_white));

                        break;
                }
                fadeAnimation(indicator);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void fadeAnimation(final View indicator) {
        indicator.setAlpha(1f);
        int min = 1000;
        animationFade = AnimationUtils.loadAnimation(getContext(), R.anim.indicator_fade_out);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                indicator.setAlpha(1f);
                indicator.startAnimation(animationFade);
                indicator.setVisibility(View.GONE);
            }
        }, min);


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
