package net.yeonjukko.bodyend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.MaterialActivity;

public class YoutubeMainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_youtube);

        FragmentManager fm = getSupportFragmentManager();
        ((ViewGroup) findViewById(R.id.fragment3View)).removeAllViews();
        YoutubeFragmentSub1 fragment = new YoutubeFragmentSub1();
        fm.beginTransaction()
                .replace(R.id.fragment3View, fragment)
                .commit();
    }

    @Override
    public void finish() {
        Intent intent = new Intent(this, MaterialActivity.class);
        startActivity(intent);
        super.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
