package net.yeonjukko.bodyend.activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.adapter.RecordRecyclerViewAdapter;
import net.yeonjukko.bodyend.fragment.YoutubeFragmentSub2;
import net.yeonjukko.bodyend.fragment.YoutubeFragmentSub3;
import net.yeonjukko.bodyend.fragment.YoutubeMainActivity;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.Typefaces;

import org.json.simple.JSONObject;

public class IntroActivity extends AppCompatActivity {
    Handler h;
    Titanic titanic;
    DBmanager dBmanager;

    Intent intent;
    String action;
    String type;

    public static final int REQUEST_CAMERA_PERMISSION = 202;
    public static final int REQUEST_LOCATION_PERMISSION = 203;

    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);
        dBmanager = new DBmanager(getContext());

        intent = getIntent();
        action = intent.getAction();
        type = intent.getType();

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        titanic = new Titanic();
        titanic.start(tv);

        //유투브를 추가한 상황일 때 intro -> youtubeMain
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);    // 가져온 인텐트의 텍스트 정보
                Intent mYoutubeIntent = new Intent(getContext(), YoutubeMainActivity.class);
                mYoutubeIntent.putExtra("youtubeId",sharedText);
                startActivity(mYoutubeIntent);
                finish();

            } else {
                h = new Handler();
                h.postDelayed(r, 1500);
            }
        } else {
            h = new Handler();
            h.postDelayed(r, 1500);
        }

    }

    Runnable r = new Runnable() {
        @Override
        public void run() {

            Intent intent = new Intent(getContext(), InitInfoActivity.class);
            startActivity(intent);
            finish();
        }
    };

    public Context getContext() {
        return this;
    }


}
