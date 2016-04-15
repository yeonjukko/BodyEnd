package net.yeonjukko.bodyend.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.libs.DayCounter;
import net.yeonjukko.bodyend.libs.Int2DayCalculator;
import net.yeonjukko.bodyend.model.ExerciseSortInfoModel;
import net.yeonjukko.bodyend.model.YoutubeRecordModel;

import java.util.Calendar;

import static com.google.android.youtube.player.YouTubePlayer.*;


public class YoutubePlayerActivity extends YouTubeBaseActivity implements
        OnInitializedListener {
    public String id;
    public String title;
    private DBmanager dBmanager;
    YouTubePlayer player;
    private MyPlaybackEventListener myPlaybackEventListener;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    Boolean flag_check = false;
    String log = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_player);
        dBmanager = new DBmanager(this);
        myPlaybackEventListener = new MyPlaybackEventListener();

        Intent intent = getIntent();
        title = intent.getStringExtra("videoTitle");
        id = intent.getStringExtra("videoId");

        TextView tvSelectTitle = (TextView) findViewById(R.id.tv_selected_yt_title);
        ImageButton ibBack = (ImageButton) findViewById(R.id.ib_icon_back);
        tvSelectTitle.setText(title);
        ibBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
        playerView.initialize(getString(R.string.youtube_api_key), this);

    }

    @Override
    public void onInitializationFailure(Provider provider,
                                        YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = String.format(
                    getString(R.string.error_player), errorReason.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onInitializationSuccess(Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        if (!wasRestored) {
//            player.cueVideo(id);
            this.player = player;
            player.loadVideo(id);
            // Hiding player controls
            // player.setPlayerStyle(PlayerStyle.MINIMAL);
            player.setPlayerStyle(PlayerStyle.DEFAULT);
            player.setPlaybackEventListener(myPlaybackEventListener);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            // Retry initialization if user performed a recovery action
            getYouTubePlayerProvider().initialize(getString(R.string.youtube_api_key), this);
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtubePlayerView);
    }


    private final class MyPlaybackEventListener implements PlaybackEventListener {
        private void updateLog(String prompt) {
            log += "MyPlaybackEventListener" + "\n" +
                    prompt + "\n\n=====";
            Log.d("mox", log);
        }

        @Override
        public void onPlaying() {
            updateLog("onPlaying");
        }

        @Override
        public void onPaused() {
            //updateLog("onPaused");

        }

        @Override
        public void onStopped() {
            updateLog("onStopped");
            if ((player.getCurrentTimeMillis() == player.getDurationMillis() && player.getCurrentTimeMillis() != 0) && !flag_check) {
                flag_check = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.MyDialog);
                RadioButton radioButton = new RadioButton(getContext());
                radioButton.setChecked(true);
                radioButton.setText(title);
                radioButton.setTextColor(getResources().getColor(R.color.Primary_text));
                builder.setTitle("운동을 완료하셨습니다! 짝짝짝!   오늘의 운동에 추가하세요.")
                        .setView(radioButton)
                        .setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                YoutubeRecordModel model = new YoutubeRecordModel();
                                model.setYoutubeId(id);
                                if (title.contains("'")) {
                                    title = title.replace("'", "''");
                                }
                                model.setYoutubeTitle(title);
                                model.setExerciseDate(new DayCounter().getToday());
                                dBmanager.insertVideoCheck(model);


                                finish();



                            }
                        })
                        .show();
            }

        }

        @Override
        public void onBuffering(boolean b) {
            //updateLog("onBuffering");

        }

        @Override
        public void onSeekTo(int i) {
           // updateLog("onSeekTo");

        }
    }

    public Context getContext() {
        return this;
    }
}
