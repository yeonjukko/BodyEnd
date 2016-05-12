package net.yeonjukko.bodyend;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDexApplication;

import net.yeonjukko.bodyend.libs.DBmanager;
import net.yeonjukko.bodyend.model.UserRecordModel;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by yeonjukko on 16. 3. 9..
 */
public class BodyEndApplication extends MultiDexApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        DBmanager bodyend_db = new DBmanager(this);


    }
}


