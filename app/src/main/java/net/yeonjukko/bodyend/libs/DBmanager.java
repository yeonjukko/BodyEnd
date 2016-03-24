package net.yeonjukko.bodyend.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import net.yeonjukko.bodyend.model.ExerciseSpotInfoModel;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;
import net.yeonjukko.bodyend.model.WaterAlarmInfoModel;

import java.util.ArrayList;

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

    private static final int DATABASE_VERSION = 2;

    // DB관련 객체 선언
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    //생성테이블
    private static final String DATABASE_CREATE_1 = "CREATE TABLE " + DATABASE_TABLE_1 +
            " (USER_NAME TEXT, USER_SEX INTEGER, USER_HEIGHT FLOAT, USER_CURR_WEIGHT FLOAT, USER_GOAL_WEIGHT FLOAT," +
            " GOAL_DATE INTEGER, STIMULUS_WORD TEXT, STIMULUS_PICTURE TEXT)";
    //EXERCISE_ALARM_STATUS 0:OFF 1:ON

    private static final String DATABASE_CREATE_2 = "CREATE TABLE " + DATABASE_TABLE_2 +
            " (RECORD_DATE INTEGER PRIMARY KEY,  PICTURE_RECORD TEXT, WEIGHT_RECORD INTEGER ," +
            " WATER_RECORD INTEGER, WATER_VOLUME FLOAT, MEAL_BREAKFAST TEXT, MEAL_LUNCH TEXT, MEAL_DINNER TEXT, MEAL_REFRESHMENTS TEXT)";

    private static final String DATABASE_CREATE_3 = "CREATE TABLE " + DATABASE_TABLE_3 +
            " (RECORD_DATE INTEGER, SPOT_ID INTEGER)";

    private static final String DATABASE_CREATE_4 = "CREATE TABLE " + DATABASE_TABLE_4 +
            " (RECORD_DATE INTEGER, SORT_ID INTEGER)";

    private static final String DATABASE_CREATE_5 = "CREATE TABLE " + DATABASE_TABLE_5 +
            " (WATER_ALARM_STATUS INTEGER, WATER_ALARM_PERIOD INTEGER, ALARM_TIMEZONE_START INTEGER, ALARM_TIMEZONE_STOP INTEGER)";

    //WATER_ALARM_STATUS 0:OFF 1:ON
    private static final String DATABASE_CREATE_6 = "CREATE TABLE " + DATABASE_TABLE_6 +
            " (SPOT_ID INTEGER PRIMARY KEY, SPOT_X DOUBLE, SPOT_Y DOUBLE, SPOT_NAME TEXT, ATTENDANCE_DAY INTEGER)";

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
            super(context, Environment.getExternalStorageDirectory().getPath() + "/" + DATABASE_NAME, null, DATABASE_VERSION);
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

    public void deleteSpot(int id) {
        String DELETE_SPOT = "DELETE FROM " + DATABASE_TABLE_6 + " WHERE SPOT_ID=" + id;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DELETE_SPOT);
        db.close();
    }

    public void updatePictureRecord(String imgPath, int date) {
        String UPDATE_IMAGE = "UPDATE " + DATABASE_TABLE_2 + " SET PICTURE_RECORD=" + "'" + imgPath + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_IMAGE);
        Log.d("mox", selectUserRecordDB(20160323).getPictureRecord() + "db");
        db.close();
    }

    public void updatePictureRecordTest(int date) {
        String UPDATE_IMAGE = "UPDATE " + DATABASE_TABLE_2 + " SET PICTURE_RECORD=" + "''" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_IMAGE);
        Log.d("mox", selectUserRecordDB(20160323).getPictureRecord() + "db");
        db.close();
    }

    public void updateBreakfast(String breakfast, int date) {
        String UPDATE_MEAL_BREAKFAST = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_BREAKFAST=" + "'" + breakfast + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_BREAKFAST);
        Log.d("mox", selectUserRecordDB(20160322).getMealBreakfast() + "db");
        db.close();
    }

    public void updateLunch(String lunch, int date) {
        String UPDATE_MEAL_LUNCH = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_LUNCH=" + "'" + lunch + "'" + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_LUNCH);
        Log.d("mox", selectUserRecordDB(20160322).getMealLunch() + "db");
        db.close();
    }

    public void updateDinner(String dinner, int date) {
        String UPDATE_MEAL_DINNER = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_DINNER=" + "'" + dinner + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_DINNER);
        Log.d("mox", selectUserRecordDB(20160322).getMealDinner() + "db");

        db.close();
    }

    public void updateRefreshment(String refreshment, int date) {
        String UPDATE_MEAL_REFRESHMENT = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_REFRESHMENTS=" + "'" + refreshment + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_REFRESHMENT);
        Log.d("mox", selectUserRecordDB(20160322).getMealRefreshments() + "db");

        db.close();
    }

    public void updateCurrWeight(float weight, int date) {
        String UPDATE_CURR_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_1 + " SET USER_CURR_WEIGHT=" + weight;
        String UPDATE_TODAY_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_2 + " SET WEIGHT_RECORD=" + weight + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_CURR_WEIGHT_RECORD);
        db.execSQL(UPDATE_TODAY_WEIGHT_RECORD);
        db.close();
    }

    public void updateWaterAlarmTime(int start, int stop) {
        String UPDATE_WATER_TIME = "UPDATE " + DATABASE_TABLE_5 + " SET ALARM_TIMEZONE_START=" + start + ", ALARM_TIMEZONE_STOP=" + stop;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_TIME);
        db.close();
    }

    public void updateWaterAlarmPeriod(int period) {
        String UPDATE_WATER_PERIOD = "UPDATE " + DATABASE_TABLE_5 + " SET WATER_ALARM_PERIOD=" + period;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_PERIOD);
        db.close();
    }

    /**
     * status 1: on 0:off
     */
    public void updateWaterAlarmStatus(int status) {
        String UPDATE_WATER_STATUS = "UPDATE " + DATABASE_TABLE_5 + " SET WATER_ALARM_STATUS=" + status;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_STATUS);
        PrintData();
        db.close();
    }

    public void updateWaterRecord(int drinkWater, int date) {
        String UPDATE_WATER_RECORD = "UPDATE " + DATABASE_TABLE_2 + " SET WATER_RECORD=" + drinkWater + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_RECORD);
        PrintData();
        db.close();
    }

    public void insertExerciseSpotInfo(ExerciseSpotInfoModel model) {

        String INSERT_EXERCISE_SPOT = "INSERT INTO " + DATABASE_TABLE_6 + "(SPOT_X,SPOT_Y,SPOT_NAME,ATTENDANCE_DAY)" + " VALUES" +
                "(" + model.getSpotX() + "," + model.getSpotY()
                + "," + "'" + model.getSpotName() + "'" + "," + null + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_EXERCISE_SPOT);
        db.close();
    }

    public void insertWaterAlarmInfoDB(WaterAlarmInfoModel model) {
        String INSERT_WATER_ALARM_INFO = "INSERT INTO " + DATABASE_TABLE_5 + " VALUES" +
                "(" + model.getWaterAlarmStatus() + "," + model.getWaterAlarmPeriod() + "," + model.getAlarmTimezoneStart()
                + "," + model.getAlarmTimezoneStop() + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_WATER_ALARM_INFO);
        PrintData();
        db.close();
    }

    public void insertUserInfoDB(UserInfoModel model) {

        String INSERT_BODY_INFO = "INSERT INTO " + DATABASE_TABLE_1 + " VALUES" +
                "(" + "'" + model.getUserName() + "'" + "," + model.getUserSex() + "," + model.getUserHeight()
                + "," + model.getUserCurrWeight() + "," + model.getUserGoalWeight() + "," + model.getGoalDate()
                + "," + "'" + model.getStimulusWord() + "'" + "," + "'" + model.getStimulusPicture() + "'" + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //db.execSQL("delete from " + DATABASE_TABLE_1);
        db.execSQL(INSERT_BODY_INFO);
        PrintData();
        db.close();
    }

    public void insertUserRecordDB(UserRecordModel model) {

        String INSERT_RECORD_INFO = "INSERT INTO " + DATABASE_TABLE_2 + " VALUES" +
                "(" + model.getRecordDate() + "," + "'" + model.getPictureRecord() + "'" + "," + model.getWeightRecord()
                + "," + model.getWaterRecord() + "," + model.getWaterVolume() + "," + "'" + model.getMealBreakfast() + "'"
                + "," + "'" + model.getMealLunch() + "'" + "," + "'" + model.getMealDinner() + "'" + "," + "'" + model.getMealRefreshments() + "'" + ")";

        this.mDbHelper = new DatabaseHelper(context);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //db.execSQL("delete from " + DATABASE_TABLE_2);
        try {
            db.execSQL(INSERT_RECORD_INFO);
        } catch (SQLiteConstraintException e) {
            e.printStackTrace();
        }

        PrintData();
        db.close();

    }

    //설정창, 출석장소정할때
    public ExerciseSpotInfoModel selectExerciseSpotInfo() {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_6;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_BODY_INFO, null);
        ExerciseSpotInfoModel spotInfoModel = new ExerciseSpotInfoModel();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            spotInfoModel.setSpotId(result.getInt(0));
            spotInfoModel.setSpotX(result.getDouble(1));
            spotInfoModel.setSpotY(result.getDouble(2));
            spotInfoModel.setSpotName(result.getString(3));
            spotInfoModel.setAttendanceDay(result.getInt(4));
        }
        result.close();
        return spotInfoModel;
    }

    //모든 spot을 가져올때
    public ArrayList<ExerciseSpotInfoModel> selectExerciseSpotsInfo() {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_6;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_BODY_INFO, null);

        ArrayList<ExerciseSpotInfoModel> spotInfoModels = new ArrayList<>();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        while (result.moveToNext()) {
            ExerciseSpotInfoModel spotInfoModel = new ExerciseSpotInfoModel();
            spotInfoModel.setSpotId(result.getInt(0));
            spotInfoModel.setSpotX(result.getDouble(1));
            spotInfoModel.setSpotY(result.getDouble(2));
            spotInfoModel.setSpotName(result.getString(3));
            spotInfoModel.setAttendanceDay(result.getInt(4));
            spotInfoModels.add(spotInfoModel);
        }

        result.close();
        return spotInfoModels;
    }

    public UserInfoModel selectUserInfoDB() {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_1;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_BODY_INFO, null);

        UserInfoModel userInfoModel = new UserInfoModel();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToFirst()) {
            userInfoModel.setUserName(result.getString(0));
            userInfoModel.setUserSex(result.getInt(1));
            userInfoModel.setUserHeight(result.getFloat(2));
            userInfoModel.setUserCurrWeight(result.getFloat(3));
            userInfoModel.setUserGoalWeight(result.getFloat(4));
            userInfoModel.setGoalDate(result.getInt(5));
            userInfoModel.setStimulusWord(result.getString(6));
            userInfoModel.setStimulusPicture(result.getString(7));
        }
        if (userInfoModel.getUserName() == null) {
            userInfoModel = null;
        }
        result.close();

        return userInfoModel;
    }

    
    public UserRecordModel selectUserRecordDB(int date) {
        String SELECT_RECORD_INFO = "SELECT * FROM " + DATABASE_TABLE_2 + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_RECORD_INFO, null);

        UserRecordModel userRecordModel = new UserRecordModel();
        if (result.moveToFirst()) {
            userRecordModel.setRecordDate(result.getInt(0));
            userRecordModel.setPictureRecord(result.getString(1));
            userRecordModel.setWeightRecord(result.getFloat(2));
            userRecordModel.setWaterRecord(result.getInt(3));
            userRecordModel.setWaterVolume(result.getInt(4));
            userRecordModel.setMealBreakfast(result.getString(5));
            userRecordModel.setMealLunch(result.getString(6));
            userRecordModel.setMealDinner(result.getString(7));
            userRecordModel.setMealRefreshments(result.getString(8));
        }
        Log.d("mox", userRecordModel.getRecordDate() + "");
        Log.d("mox", userRecordModel.toString());
        result.close();
        return userRecordModel;
    }

    public HashMap<Integer, UserRecordModel> selectUserRecordDB() {
        String SELECT_RECORD_INFO_ALL = "SELECT * FROM " + DATABASE_TABLE_2;

        HashMap<Integer, UserRecordModel> mUserRecordModels = new HashMap<>();
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_RECORD_INFO_ALL, null);

        while (result.moveToNext()) {
            UserRecordModel userRecordModel = new UserRecordModel();
            userRecordModel.setRecordDate(result.getInt(0));
            userRecordModel.setPictureRecord(result.getString(1));
            userRecordModel.setWeightRecord(result.getFloat(2));
            userRecordModel.setWaterRecord(result.getInt(3));
            userRecordModel.setWaterVolume(result.getInt(4));
            userRecordModel.setMealBreakfast(result.getString(5));
            userRecordModel.setMealLunch(result.getString(6));
            userRecordModel.setMealDinner(result.getString(7));
            userRecordModel.setMealRefreshments(result.getString(8));
            mUserRecordModels.put(userRecordModel.getRecordDate(), userRecordModel);
        }
        result.close();
        return mUserRecordModels;
    }

    public WaterAlarmInfoModel selectWaterAlarmInfoDB() {
        String SELECT_WATER_ALARM_INFO = "SELECT * FROM " + DATABASE_TABLE_5;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_WATER_ALARM_INFO, null);

        WaterAlarmInfoModel waterAlarmInfoModel = new WaterAlarmInfoModel();
        if (result.moveToFirst()) {
            waterAlarmInfoModel.setWaterAlarmStatus(result.getInt(0));
            waterAlarmInfoModel.setWaterAlarmPeriod(result.getInt(1));
            waterAlarmInfoModel.setAlarmTimezoneStart(result.getInt(2));
            waterAlarmInfoModel.setAlarmTimezoneStop(result.getInt(3));
        }
        result.close();
        return waterAlarmInfoModel;
    }

    public String PrintData() {
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String str = "";

        Cursor cursor = db.rawQuery("select * from " + DATABASE_TABLE_5, null);
        while (cursor.moveToNext()) {
            str += " DB:  "
                    + cursor
                    + "\n";
        }

        return str;
    }


}
