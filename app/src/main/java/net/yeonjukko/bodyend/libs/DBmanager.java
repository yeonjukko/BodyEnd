package net.yeonjukko.bodyend.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import net.yeonjukko.bodyend.model.UserInfoModel;

/**
 * Created by yeonjukko on 16. 3. 9..
 */
public class DBmanager {
    private static final String DATABASE_NAME = "BODYEND";
    private static final String DATABASE_TABLE_1 = "USER_INFO";
    private static final String DATABASE_TABLE_2 = "USER_RECORD";

    private static final String DATABASE_TABLE_3 = "EXERCISE_SPOT_RECORD";
    private static final String DATABASE_TABLE_4 = "EXERCISE_SORT_RECORD";

    private static final String DATABASE_TABLE_5 = "WATER_ALARM_INFO";
    private static final String DATABASE_TABLE_6 = "EXERCISE_SPOT_INFO";
    private static final String DATABASE_TABLE_7 = "EXERCISE_SORT_INFO";

    private static final int DATABASE_VERSION = 1;

    // DB관련 객체 선언
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //생성테이블
    private static final String DATABASE_CREATE_1 = "CREATE TABLE " + DATABASE_TABLE_1 +
            " (USER_NAME TEXT, USER_SEX INTEGER, USER_HEIGHT FLOAT, USER_CURR_WEIGHT FLOAT, USER_GOAL_WEIGHT FLOAT," +
            " GOAL_DATE LONG, STIMULUS_WORD TEXT, STIMULUS_PICTURE TEXT)";

    private static final String DATABASE_CREATE_2 = "CREATE TABLE " + DATABASE_TABLE_2 + "" +
            " (RECORD_DATE INTEGER, EXERCISE_ALARM_STATUS INTEGER, PICTURE_RECORD TEXT, WEIGHT_RECORD FLOAT ," +
            " WATER_RECORD FLOAT, WATER_VOLUME FLOAT, MEAL_BREAKFAST TEXT, MEAL_LUNCH TEXT, MEAL_DINNER TEXT, MEAL_REFRESHMENTS TEXT)";

    private static final String DATABASE_CREATE_3 = "CREATE TABLE " + DATABASE_TABLE_3 +
            " (RECORD_DATE INTEGER, SPOT_ID)";

    private static final String DATABASE_CREATE_4 = "CREATE TABLE " + DATABASE_TABLE_4 +
            " (RECORD_DATE INTEGER, SORT_ID)";

    private static final String DATABASE_CREATE_5 = "CREATE TABLE " + DATABASE_TABLE_5 +
            " (WATER_ALARM_PERIOD INTEGER, ALARM_TIMEZONE_START INTEGER, ALARM_TIMEZONE_STOP INTEGER)";

    private static final String DATABASE_CREATE_6 = "CREATE TABLE " + DATABASE_TABLE_6 +
            " (SPOT_ID INTEGER, SPOT_X FLOAT, SPOT_Y FLOAT, SPOT_NAME TEXT, ATTENDANCE_DAY INTEGER)";

    private static final String DATABASE_CREATE_7 = "CREATE TABLE " + DATABASE_TABLE_7 +
            " (SORT_ID INTEGER, EXERCISE_NAME TEXT, EXERCISE_DAY INTEGER)";

    // 부가적인 객체들
    private Context context;

    // 생성자
    public DBmanager(Context context) {
        this.context = context;
        this.mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();


    }

    // helper
    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, Environment.getExternalStorageDirectory().getPath()+"/" +DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            //데이터베이스 최초 생성될때 실행 디비가 생성될때 실행된다
            db.execSQL(DATABASE_CREATE_1);
            db.execSQL(DATABASE_CREATE_2);
            db.execSQL(DATABASE_CREATE_3);
            db.execSQL(DATABASE_CREATE_4);
            db.execSQL(DATABASE_CREATE_5);
            db.execSQL(DATABASE_CREATE_6);
            db.execSQL(DATABASE_CREATE_7);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }


    public void insertUserInfoDB(UserInfoModel model) {
        String INSERT_BODY_INFO = "INSERT INTO " + DATABASE_TABLE_1 + " VALUES" +
                "(" + "'" + model.getUserName() + "'" + "," + model.getUserSex() + "," + model.getUserHeight()
                + "," + model.getUserCurrWeight() + "," + model.getUserGoalWeight() + "," + model.getGoalDate()
                + "," + "'" + model.getStimulusWord() + "'" + "," + "'" + model.getStimulusPicture() + "'" + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_BODY_INFO);
        PrintData();
        db.close();
    }

    public UserInfoModel selectUserInfoDB() {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_1;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor result = db.rawQuery(SELECT_BODY_INFO, null);

        UserInfoModel userInfoModel = new UserInfoModel();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            userInfoModel.setUserName(result.getString(0));
            userInfoModel.setUserSex(result.getInt(1));
            userInfoModel.setUserHeight(result.getFloat(2));
            userInfoModel.setUserCurrWeight(result.getFloat(3));
            userInfoModel.setUserGoalWeight(result.getFloat(4));
            userInfoModel.setGoalDate(result.getLong(5));
            userInfoModel.setStimulusWord(result.getString(6));
            userInfoModel.setStimulusPicture(result.getString(7));

        }
        result.close();

        return userInfoModel;

    }

    public String PrintData() {
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from "+DATABASE_TABLE_1, null);
        while(cursor.moveToNext()) {
            str += " userName:  "
                    + cursor.getString(0)
                    + "\n";
        }

        return str;
    }




}
