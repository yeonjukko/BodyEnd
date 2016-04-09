package net.yeonjukko.bodyend.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;

import java.io.File;
import java.io.IOException;

public class StimulusFragment extends Fragment {
    Bitmap bitmap;
    DBmanager dBmanager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stimulus, container, false);
        dBmanager = new DBmanager(rootView.getContext());

        TextView tvDday = (TextView) rootView.findViewById(R.id.tv_d_day);
        TextView tvStimulusWord = (TextView) rootView.findViewById(R.id.tv_stimulus);
        ImageView btSetting = (ImageView) rootView.findViewById(R.id.bt_setting);
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RecordFragment.class);
                startActivity(intent);
            }
        });

        //레이아웃 데이터 설정
        DayCounter counter = new DayCounter();
        int day = counter.dayCounter(dBmanager.selectUserInfoDB().getGoalDate());
        String stimulusWord = dBmanager.selectUserInfoDB().getStimulusWord();

        tvDday.setText(day + "");
        tvStimulusWord.setText(stimulusWord);


        //<--레이아웃 배경화면 설정
        final ImageView layout = (ImageView) rootView.findViewById(R.id.layout_bgr);
        Toast.makeText(getContext(), dBmanager.selectUserInfoDB().getStimulusPicture(), Toast.LENGTH_LONG).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(dBmanager.selectUserInfoDB().getStimulusPicture(), options);
                final Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                drawable.setAlpha(190);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        layout.setBackground(drawable);

                    }
                });

            }
        }).start();


        //<--레이아웃 배경화면 설정-->


        return rootView;
    }


}
