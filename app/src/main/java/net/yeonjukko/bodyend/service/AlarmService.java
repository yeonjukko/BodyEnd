package net.yeonjukko.bodyend.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import net.yeonjukko.bodyend.R;
import net.yeonjukko.bodyend.activity.MaterialActivity;
import net.yeonjukko.bodyend.fragment.MainFragment;
import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.WaterAlarmInfoModel;

import java.util.Calendar;

/**
 * Created by MoonJongRak on 2016. 4. 9..
 */
public class AlarmService extends Service {

    public static final int ALARM_CODE = 7050;
    public static final int CALL_MAIN_ACTIVITY_CODE = 3357;

    private Calendar time;
    private AlarmManager mAlarmManager;
    private WaterAlarmInfoModel mWaterAlarmInfoModel;
    private PendingIntent mPendingIntentAlarmService;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        DBmanager mDBmanager = new DBmanager(getContext());
        mWaterAlarmInfoModel = mDBmanager.selectWaterAlarmInfoDB();
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent intent = new Intent(getContext(), this.getClass());
        mPendingIntentAlarmService = PendingIntent.getService(getContext(), ALARM_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        mAlarmManager.cancel(mPendingIntentAlarmService);

        time = Calendar.getInstance();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //켜진상태
        if (mWaterAlarmInfoModel.getWaterAlarmStatus() == 1) {
            Calendar alarmTime = Calendar.getInstance();

            int nowHour = time.get(Calendar.HOUR_OF_DAY);

            if (nowHour >= mWaterAlarmInfoModel.getAlarmTimezoneStart() && nowHour < mWaterAlarmInfoModel.getAlarmTimezoneStop()) {
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext())
                        .setContentTitle("물 마실 시간입니다.")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("Hello World!")
                        .setAutoCancel(true)
                        .setContentIntent(PendingIntent.getActivity(getContext(), CALL_MAIN_ACTIVITY_CODE, new Intent(getContext(), MaterialActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                        .setDefaults(Notification.DEFAULT_ALL);
                ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(ALARM_CODE, mBuilder.build());

                alarmTime.setTimeInMillis(time.getTimeInMillis() + mWaterAlarmInfoModel.getWaterAlarmPeriod() * 60 * 1000);
            } else {
                if (nowHour > mWaterAlarmInfoModel.getAlarmTimezoneStop()) {
                    //다음날 시작시간에 알람 등록
                    alarmTime.setTimeInMillis(time.getTimeInMillis() + 60 * 60 * 24 * 1000);
                }
                alarmTime.set(Calendar.HOUR_OF_DAY, mWaterAlarmInfoModel.getAlarmTimezoneStart());
                alarmTime.set(Calendar.MINUTE, 0);
                alarmTime.set(Calendar.SECOND, 0);
            }

            mAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), mPendingIntentAlarmService);
        }

        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private Context getContext() {
        return this;
    }

    public static class DeviceStartBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context, AlarmService.class));
        }
    }

}
