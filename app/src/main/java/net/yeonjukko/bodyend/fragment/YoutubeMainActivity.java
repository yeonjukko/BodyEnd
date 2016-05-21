package net.yeonjukko.bodyend.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
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
        ((ViewGroup) findViewById(R.id.fragmentyoutubeMain)).removeAllViews();

        //유투브를 추가한 상황일 때 youtubeMain -> youtubeFragmentSub3
        if (getIntent().getData() != null) {
                YoutubeFragmentSub3 fragment = new YoutubeFragmentSub3();
                Bundle data = new Bundle();
                data.putString("youtubeId", getIntent().getStringExtra("youtubeId"));
                fragment.setArguments(data);

                fm.beginTransaction()
                        .replace(R.id.fragmentyoutubeMain, fragment)
                        .commit();


        } else {    //default
            YoutubeFragmentSub1 fragment = new YoutubeFragmentSub1();
            fm.beginTransaction()
                    .replace(R.id.fragmentyoutubeMain, fragment)
                    .commit();
        }


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
