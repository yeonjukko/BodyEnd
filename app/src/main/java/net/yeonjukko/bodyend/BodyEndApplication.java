package net.yeonjukko.bodyend;

import android.app.Application;

import net.yeonjukko.bodyend.libs.DBmanager;

/**
 * Created by yeonjukko on 16. 3. 9..
 */
public class BodyEndApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        DBmanager bodyend_db = new DBmanager(this);

    }
}


