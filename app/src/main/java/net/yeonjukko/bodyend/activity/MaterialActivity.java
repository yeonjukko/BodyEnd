package net.yeonjukko.bodyend.activity;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.settings.DefaultSettingActivity;
import net.yeonjukko.bodyend.activity.settings.ExerciseManagerActivity;
import net.yeonjukko.bodyend.activity.settings.WaterSettingActivity;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.fragment.RecordFragment;
import net.yeonjukko.bodyend.fragment.YoutubeMainActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class MaterialActivity extends MaterialNavigationDrawer {
    private MaterialAccount account;
    private DBmanager dBmanager;


    @Override
    public void init(Bundle savedInstanceState) {
        dBmanager = new DBmanager(this);
        UserInfoModel userInfoModel = dBmanager.selectUserInfoDB();
        RelativeLayout drawer = (RelativeLayout)findViewById(R.id.drawer);
        ViewGroup.LayoutParams drawerParams = drawer.getLayoutParams();
        drawerParams.width = getResources().getDisplayMetrics().widthPixels;

        //custom drawer 설정
        View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer,null);
        setDrawerHeaderCustom(view);

//        account = new MaterialAccount(getResources(),
//                userInfoModel.getUserName(),
//                "목표: " + userInfoModel.getStimulusWord(),
//                null, R.drawable.image_menu);
//        addAccount(account);

        addSection(newSection("홈", R.drawable.icon_home, new MainFragment()));
        MaterialSection calendarSection = newSection("캘린더", R.drawable.icon_calendar_2, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });
        addSection(calendarSection);

        MaterialSection youtubeSection = newSection("유투브다이어트", R.drawable.icon_youtube_2, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, YoutubeMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addSection(youtubeSection);

        MaterialSection gallerySection = newSection("비포&애프터", R.drawable.icon_before_after, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
        addSection(gallerySection);
        addDivisor();
        MaterialSection settingSection = newSection("기본 설정", R.drawable.icon_setting, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, DefaultSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addSection(settingSection);
        MaterialSection settingSection2 = newSection("물 설정", R.drawable.icon_bottle_gray_10, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, WaterSettingActivity.class);
                startActivity(intent);
            }
        });
        addSection(settingSection2);
        MaterialSection settingSection3 = newSection("My 운동 설정", R.drawable.icon_oxygen, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, ExerciseManagerActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addSection(settingSection3);
        addBottomSection(newSection("나가기", R.drawable.icon_exit, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                finish();
            }
        }));
        setDefaultSectionLoaded(0);
        this.disableLearningPattern();

        getSupportActionBar().hide();
    }

}
