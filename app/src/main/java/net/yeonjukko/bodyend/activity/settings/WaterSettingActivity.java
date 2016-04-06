package net.yeonjukko.bodyend.activity.settings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.IRangeBarFormatter;
import com.appyvet.rangebar.RangeBar;
import com.fenjuly.mylibrary.ToggleExpandLayout;
import com.zcw.togglebutton.ToggleButton;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.WaterAlarmInfoModel;

import java.util.Calendar;

public class WaterSettingActivity extends AppCompatActivity {
    DBmanager dBmanager;
    ToggleExpandLayout alarmToggleLayout;
    int mRightPinValue, mLeftPinValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water_setting);
        final LinearLayout periodLayout = (LinearLayout) findViewById(R.id.id3);
        final LinearLayout timeLayout = (LinearLayout) findViewById(R.id.id2);
        TextView tvGoal = (TextView) findViewById(R.id.tv_water_goal);
        final TextView tvPeriod = (TextView) findViewById(R.id.tv_alarm_period);
        final TextView tvStartTime = (TextView) findViewById(R.id.tv_start_time);
        final TextView tvStopTime = (TextView) findViewById(R.id.tv_stop_time);
        ToggleButton switchButton = (ToggleButton) findViewById(R.id.switch_button);
        alarmToggleLayout = (ToggleExpandLayout) findViewById(R.id.toogleLayout);

        dBmanager = new DBmanager(this);


        WaterAlarmInfoModel model = dBmanager.selectWaterAlarmInfoDB();
        final int alarmPeriod = model.getWaterAlarmPeriod();
        int alarmStartTime = model.getAlarmTimezoneStart();
        int alarmStopTime = model.getAlarmTimezoneStop();

        //알람 expandlayout 세팅에 따른 on/off 설정
        tvGoal.setText((int) dBmanager.selectUserInfoDB().getUserCurrWeight() * 33 / 300 + "");
        tvPeriod.setText(alarmPeriod + "");
        tvStartTime.setText(alarmStartTime + "시 ~ ");
        tvStopTime.setText(alarmStopTime + "시");
        alarmToggleLayout.setOnToggleTouchListener(new ToggleExpandLayout.OnToggleTouchListener() {
            @Override
            public void onStartOpen(int height, int originalHeight) {

            }

            @Override
            public void onOpen() {
                dBmanager.updateWaterAlarmStatus(1);

            }

            @Override
            public void onStartClose(int height, int originalHeight) {

            }

            @Override
            public void onClosed() {
                dBmanager.updateWaterAlarmStatus(0);
            }
        });

        switchButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    alarmToggleLayout.open();
                } else {
                    alarmToggleLayout.close();
                }
            }
        });

        //알람 주기 레이아웃
        periodLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CharSequence[] items = {"15분", "30분", "45분", "60분"};
                int selected = (dBmanager.selectWaterAlarmInfoDB().getWaterAlarmPeriod() / 15) - 1;

                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("알람 주기 설정")
                        .setSingleChoiceItems(items, selected, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dBmanager.updateWaterAlarmPeriod((which + 1) * 15);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tvPeriod.setText(dBmanager.selectWaterAlarmInfoDB().getWaterAlarmPeriod() + "");
                                periodLayout.invalidate();

                            }
                        })
                        .show();


            }
        });

        timeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_water_time_setting, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


                builder.setView(view)
                        .setTitle("알람할 시간을 설정해주세요.");

                RangeBar rangeBar = (RangeBar) view.findViewById(R.id.rangebar);
                rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                    @Override
                    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                                      int rightPinIndex,
                                                      String leftPinValue, String rightPinValue) {
                        mLeftPinValue = Integer.parseInt(leftPinValue);
                        mRightPinValue = Integer.parseInt(rightPinValue);
                    }
                });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dBmanager.updateWaterAlarmTime(mLeftPinValue,mRightPinValue);
                        tvStartTime.setText(mLeftPinValue + "시 ~ ");
                        tvStopTime.setText(mRightPinValue+"시");
                        timeLayout.invalidate();
                    }
                });
                builder.show();
            }
        });


    }


    Handler mHandler = new Handler();

    @Override
    protected void onStart() {
        super.onStart();
        ToggleButton switchButton = (ToggleButton) findViewById(R.id.switch_button);

        final int alarmStatus = dBmanager.selectWaterAlarmInfoDB().getWaterAlarmStatus();
        if (alarmStatus == 0) switchButton.setToggleOff();
        else if (alarmStatus == 1) switchButton.setToggleOn();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alarmStatus == 1)
                    alarmToggleLayout.open();
            }
        }, 400);

    }

    public Context getContext(){
        return this;
    }


}
