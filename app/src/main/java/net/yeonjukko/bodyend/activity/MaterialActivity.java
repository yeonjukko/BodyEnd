package net.yeonjukko.bodyend.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.OrientationHelper;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.settings.DefaultSettingActivity;
import net.yeonjukko.bodyend.activity.settings.ExerciseManagerActivity;
import net.yeonjukko.bodyend.activity.settings.WaterSettingActivity;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.fragment.RecordFragment;
import net.yeonjukko.bodyend.fragment.YoutubeMainActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserInfoModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;
import it.neokree.materialnavigationdrawer.elements.listeners.MaterialSectionListener;

public class MaterialActivity extends MaterialNavigationDrawer {
    private MaterialAccount account;
    private DBmanager dBmanager;
    int drawerBgrColor;
    int drawerTextColor;
    String drawerStimulus;
    public static final String DEFAULT_URL = "http://yeonjukko.net:7533/getDataSet";


    @Override
    public void init(Bundle savedInstanceState) {
        dBmanager = new DBmanager(this);
        final UserInfoModel userInfoModel = dBmanager.selectUserInfoDB();
        RelativeLayout drawer = (RelativeLayout) findViewById(R.id.drawer);
        ViewGroup.LayoutParams drawerParams = drawer.getLayoutParams();
        drawerParams.width = getResources().getDisplayMetrics().widthPixels;

        //custom drawer 설정
        final View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer, null);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject result = (JSONObject) getServer();
                if (result != null) {
                    JSONObject data = (JSONObject) result.get("contents");

                    drawerBgrColor = Color.parseColor("#" + data.get("drawer_background_color").toString());
                    drawerTextColor = Color.parseColor("#" + data.get("drawer_text_color").toString());
                    drawerStimulus = data.get("drawer_text").toString();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout drawerLayout = (LinearLayout) view.findViewById(R.id.layout_drawer);
                            TextView tvTitle = (TextView) view.findViewById(R.id.tv_stimulus_title);
                            TextView tvWord = (TextView) view.findViewById(R.id.tv_stimulus_word);
                            TextView tvUserName = (TextView) view.findViewById(R.id.tv_user_name);


                            drawerLayout.setBackgroundColor(drawerBgrColor);
                            tvTitle.setTextColor(drawerTextColor);
                            tvWord.setTextColor(drawerTextColor);
                            tvWord.setText(drawerStimulus);
                            tvUserName.setText(userInfoModel.getUserName());
                        }
                    });

                }
            }
        }).start();

        setDrawerHeaderCustom(view);


        addSection(newSection("홈", R.drawable.icon_home, new MainFragment()));
        MaterialSection calendarSection = newSection("캘린더", R.drawable.icon_calendar_2, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, CalendarActivity.class);
                startActivity(intent);

            }
        });
        addSection(calendarSection);

        MaterialSection youtubeSection = newSection("유투브 다이어트", R.drawable.icon_youtube_2, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, YoutubeMainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        addSection(youtubeSection);

        MaterialSection gallerySection = newSection("비포 & 애프터", R.drawable.icon_before_after, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
        addSection(gallerySection);

        MaterialSection graphSection = newSection("통계", R.drawable.icon_graph, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, GraphActivity.class);
                startActivity(intent);
            }
        });
        addSection(graphSection);

        MaterialSection betaSection = newSection("고객센터", R.mipmap.ic_launcher, new MaterialSectionListener() {
            @Override
            public void onClick(MaterialSection section) {
                Intent intent = new Intent(MaterialActivity.this, BetaActivity.class);
                startActivity(intent);
            }
        });
        addSection(betaSection);


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

    public Object getServer() {
        try {
            URL url = new URL(DEFAULT_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(3000);
            connection.connect();
            return JSONValue.parse(new InputStreamReader(connection.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
