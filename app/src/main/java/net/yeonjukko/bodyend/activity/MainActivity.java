package net.yeonjukko.bodyend.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import net.yeonjukko.bodyend.R;

public class MainActivity extends AppCompatActivity {
    int MAX_PAGE = 3;
    private Fragment cur_fragment;
    Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        adapter = new Adapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        Intent intent = getIntent();
        if (intent.getIntExtra("showDate",0) != 0) {
            viewPager.setCurrentItem(1);
        }


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
                    break;
                case 2:
                    cur_fragment = new YoutubeMainFragment();

            }

            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }


}
