package net.yeonjukko.bodyend.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import net.yeonjukko.bodyend.R;

public class YoutubeMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_youtube);

        FragmentManager fm = getSupportFragmentManager();
        ((ViewGroup)findViewById(R.id.fragment3View)).removeAllViews();
        YoutubeFragmentSub1 fragment = new YoutubeFragmentSub1();
        fm.beginTransaction()
                .replace(R.id.fragment3View, fragment)
                .commit();
    }





}
