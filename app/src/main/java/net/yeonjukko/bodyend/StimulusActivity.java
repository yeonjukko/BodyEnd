package net.yeonjukko.bodyend;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;

import java.io.IOException;

public class StimulusActivity extends AppCompatActivity {
    Bitmap bitmap;
    DBmanager dBmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimulus);
        dBmanager = new DBmanager(this);

        TextView tvDday = (TextView)findViewById(R.id.tv_d_day);
        TextView tvStimulusWord = (TextView)findViewById(R.id.tv_stimulus);
        ImageView btSetting = (ImageView)findViewById(R.id.bt_setting);
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StimulusActivity.this,RecordActivity.class);
                startActivity(intent);
            }
        });

        //레이아웃 데이터 설정
        DayCounter counter = new DayCounter();
        int day = counter.dayCounter(dBmanager.selectUserInfoDB().getGoalDate());
        String stimulusWord = dBmanager.selectUserInfoDB().getStimulusWord();

        tvDday.setText(day+"");
        tvStimulusWord.setText(stimulusWord);


        //<--레이아웃 배경화면 설정
        ImageView layout = (ImageView) findViewById(R.id.layout_bgr);
        Toast.makeText(getContext(),dBmanager.selectUserInfoDB().getStimulusPicture(),Toast.LENGTH_LONG).show();
        Uri resultUri = Uri.parse(dBmanager.selectUserInfoDB().getStimulusPicture());

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            drawable.setAlpha(190);
            layout.setBackground(drawable);

        } catch (IOException e) {
            e.printStackTrace();
        }
        //<--레이아웃 배경화면 설정-->


    }

    private Context getContext() {
        return this;
    }

}
