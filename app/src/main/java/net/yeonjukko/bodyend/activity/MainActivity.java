package net.yeonjukko.bodyend.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.fragment.RecordFragment;
import net.yeonjukko.bodyend.fragment.StimulusFragment;
import net.yeonjukko.bodyend.fragment.YoutubeMainFragment;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class MainActivity extends MaterialNavigationDrawer {
    int MAX_PAGE = 3;
    private Fragment cur_fragment;
    public Fragment record_fragment;
    private MaterialAccount account;
    UserInfoModel userInfoModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new Adapter(getSupportFragmentManager()));
        userInfoModel = new DBmanager(this).selectUserInfoDB();
        Intent intent = getIntent();
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


    }

    @Override
    public void init(Bundle savedInstanceState) {

        account = new MaterialAccount(getResources(),
                userInfoModel.getUserName(),
                "목표:" + userInfoModel.getStimulusWord(),
                null, null);
        addAccount(account);

        addSection(newSection("", R.drawable.icon_home, new RecordFragment()));
        MaterialSection teamSection = newSection("캘린더", R.drawable.icon_calendar_2, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MainActivity.this, TeamInfoActivity.class);
                intent.putExtra("gid", getIntent().getStringExtra("gid"));
                startActivity(intent);
            }
        });
        addSection(teamSection);
        addSection(newSection("유투브다이어트", R.drawable.icon_youtube_2, new CalendarFragment()));
        addSection(newSection("비포&애프터", R.drawable.icon_before_after, new AttendanceFragment()));
        addBottomSection(newSection("설정", R.drawable.icon_setting, new);

        this.disableLearningPattern();
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
