package net.yeonjukko.bodyend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.model.UserInfoModel;

import java.io.IOException;

public class StimulusActivity extends AppCompatActivity {
    Bitmap bitmap;
    DBmanager dBmanager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stimulus);
        dBmanager = new DBmanager(this);

        DayCounter counter = new DayCounter();
        Log.d("mox", "stimilus" + dBmanager.PrintData());

        int day = counter.dayCounter(dBmanager.selectUserInfoDB().getGoalDate());

        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_bgr);
        Uri resultUri = Uri.parse(dBmanager.selectUserInfoDB().getStimulusPicture());

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), resultUri);
            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
            drawable.setAlpha(150);
            layout.setBackground(drawable);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private Context getContext() {
        return this;
    }

}
