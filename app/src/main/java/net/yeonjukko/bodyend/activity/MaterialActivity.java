package net.yeonjukko.bodyend.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.settings.DefaultSettingActivity;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.fragment.RecordFragment;
import net.yeonjukko.bodyend.fragment.YoutubeMainActivity;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class MaterialActivity extends MaterialNavigationDrawer {
    private MaterialAccount account;


    @Override
    public void init(Bundle savedInstanceState) {

//        account = new MaterialAccount(getResources(),
//                userInfoModel.getUserName(),
//                "목표:" + userInfoModel.getStimulusWord(),
//                null, null);
        account = new MaterialAccount(getResources(),
                "김연주",
                "목표:사랑하기",
                null, null);
        addAccount(account);

        addSection(newSection("홈", R.drawable.icon_home, new MainFragment()));
        MaterialSection calendarSection = newSection("캘린더", R.drawable.icon_calendar_2, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, CalendarActivity.class);
                intent.putExtra("gid", getIntent().getStringExtra("gid"));
                startActivity(intent);
            }
        });
        addSection(calendarSection);

        addSection(newSection("유투브다이어트", R.drawable.icon_youtube_2, new YoutubeMainActivity()));
        // addSection(newSection("비포&애프터", R.drawable.icon_before_after, new AttendanceFragment()));
        addBottomSection(newSection("설정", R.drawable.icon_setting, new DefaultSettingActivity()));
        setDefaultSectionLoaded(0);
        this.disableLearningPattern();
    }

}
