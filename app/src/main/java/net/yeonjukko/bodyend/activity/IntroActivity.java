package net.yeonjukko.bodyend.activity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Shader;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.romainpiel.titanic.library.Titanic;
import com.romainpiel.titanic.library.TitanicTextView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.Typefaces;

public class IntroActivity extends AppCompatActivity {
    Handler h;
    Titanic titanic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        TitanicTextView tv = (TitanicTextView) findViewById(R.id.titanic_tv);

        // set fancy typeface
        tv.setTypeface(Typefaces.get(this, "Satisfy-Regular.ttf"));

        titanic = new Titanic();
        titanic.start(tv);

        h = new Handler();
        h.postDelayed(r, 1500);
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
