package net.yeonjukko.bodyend.libs;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import net.yeonjukko.bodyend.model.ExerciseAttendanceInfoModel;
import net.yeonjukko.bodyend.model.ExerciseJoinSortInfoModel;
import net.yeonjukko.bodyend.model.ExerciseSortInfoModel;
import net.yeonjukko.bodyend.model.ExerciseSpotInfoModel;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;
import net.yeonjukko.bodyend.model.WaterAlarmInfoModel;

import java.util.ArrayList;
import java.util.HashMap;

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
    private static final String DATABASE_TABLE_8 = "EXERCISE_SORT_INFO_CLONE";

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
            " (RECORD_DATE INTEGER, SORT_ID TEXT)";

    private static final String DATABASE_CREATE_5 = "CREATE TABLE " + DATABASE_TABLE_5 +
            " (WATER_ALARM_STATUS INTEGER, WATER_ALARM_PERIOD INTEGER, ALARM_TIMEZONE_START INTEGER, ALARM_TIMEZONE_STOP INTEGER)";

    //WATER_ALARM_STATUS 0:OFF 1:ON
    private static final String DATABASE_CREATE_6 = "CREATE TABLE " + DATABASE_TABLE_6 +
            " (SPOT_ID INTEGER PRIMARY KEY, SPOT_X DOUBLE, SPOT_Y DOUBLE, SPOT_NAME TEXT, ATTENDANCE_DAY INTEGER)";

    private static final String DATABASE_CREATE_7 = "CREATE TABLE " + DATABASE_TABLE_7 +
            " (SORT_ID TEXT PRIMARY KEY, EXERCISE_NAME TEXT, EXERCISE_DAY INTEGER, EXERCISE_TYPE INTEGER, EXERCISE_ADD_DATE INTEGER)";

    private static final String DATABASE_CREATE_8 = "CREATE TABLE " + DATABASE_TABLE_8 +
            " (SORT_ID TEXT PRIMARY KEY, EXERCISE_NAME TEXT, EXERCISE_DAY INTEGER, EXERCISE_TYPE INTEGER, EXERCISE_ADD_DATE INTEGER)";

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
            db.execSQL(DATABASE_CREATE_8);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

    public void deleteSort(String id) {
        String DELETE_SORT = "DELETE FROM " + DATABASE_TABLE_7 + " WHERE SORT_ID=" + id;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DELETE_SORT);

        db.close();
    }

    public void deleteSpot(int id) {
        String DELETE_SPOT = "DELETE FROM " + DATABASE_TABLE_6 + " WHERE SPOT_ID=" + id;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DELETE_SPOT);
        db.close();
    }

    public void updateCheckStatus(String id, int date, boolean isChecked) {
        String INSERT_EXERCISE_SORT_RECORD = "INSERT INTO " + DATABASE_TABLE_4 + " VALUES" + " (" + date + "," + id + ")";
        String DELETE_EXERCISE_SORT_RECORD = "DELETE FROM " + DATABASE_TABLE_4 + " WHERE RECORD_DATE = " + date + " AND SORT_ID =" + id;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        if (isChecked) {
            db.execSQL(INSERT_EXERCISE_SORT_RECORD);
            Log.d("mox", "insert");
        } else {
            db.execSQL(DELETE_EXERCISE_SORT_RECORD);
            Log.d("mox", "delete");
        }
        db.close();
    }

    public void updatePictureRecord(String imgPath, int date) {
        String UPDATE_IMAGE = "UPDATE " + DATABASE_TABLE_2 + " SET PICTURE_RECORD=" + "'" + imgPath + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_IMAGE);
        db.close();
    }

    public void updatePictureRecordTest(int date) {
        String UPDATE_IMAGE = "UPDATE " + DATABASE_TABLE_2 + " SET PICTURE_RECORD=" + "''" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_IMAGE);
        db.close();
    }

    public void updateBreakfast(String breakfast, int date) {
        String UPDATE_MEAL_BREAKFAST = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_BREAKFAST=" + "'" + breakfast + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_BREAKFAST);
        db.close();
    }

    public void updateLunch(String lunch, int date) {
        String UPDATE_MEAL_LUNCH = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_LUNCH=" + "'" + lunch + "'" + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_LUNCH);
        db.close();
    }

    public void updateDinner(String dinner, int date) {
        String UPDATE_MEAL_DINNER = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_DINNER=" + "'" + dinner + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_DINNER);

        db.close();
    }

    public void updateRefreshment(String refreshment, int date) {
        String UPDATE_MEAL_REFRESHMENT = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_REFRESHMENTS=" + "'" + refreshment + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_REFRESHMENT);

        db.close();
    }

    public void updateCurrWeight(float weight, int date) {
        String UPDATE_CURR_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_1 + " SET USER_CURR_WEIGHT=" + weight;
        String UPDATE_TODAY_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_2 + " SET WEIGHT_RECORD=" + weight +",WATER_VOLUME="+(int)(weight*33/300)+ " WHERE RECORD_DATE=" + date;
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

    //1:유산소 0:무산소 2:유투브
    public void insertExerciseSortInfo(ExerciseSortInfoModel model) {
        String INSERT_EXERCISE_SORT = "INSERT INTO " + DATABASE_TABLE_7 + " VALUES " +
                "(" + "'" + model.getSortId() + "'" + ","+ "'" + model.getExerciseName() + "'" + "," + model.getExerciseDay() + "," + model.getExerciseType() + "," + model.getExerciseAddDate() + ")";
        String INSERT_EXERCISE_SORT_CLONE = "INSERT INTO " + DATABASE_TABLE_8 + " VALUES " +
                "(" + "'" + model.getSortId() + "'" + ","+ "'" + model.getExerciseName() + "'" + "," + model.getExerciseDay() + "," + model.getExerciseType() + "," + model.getExerciseAddDate() + ")";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(INSERT_EXERCISE_SORT);
        db.execSQL(INSERT_EXERCISE_SORT_CLONE);

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

    public void insertExerciseSpotAttendance(ExerciseAttendanceInfoModel model) {

        String INSERT_EXERCISE_ATTEND = "INSERT INTO " + DATABASE_TABLE_3 + " VALUES" +
                "(" + model.getRecordDate() + "," + model.getSpotId() + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_EXERCISE_ATTEND);
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

    //모든 sort을 가져올때

    /**
     * oxygen은 0일때 무산소운동 1일때 유산소운동
     */
    //오늘의 데이터를 가져올 때
    public ArrayList<ExerciseSortInfoModel> selectExerciseSortsInfo() {
        String SELECT_EXERCISE_SORT = "SELECT * FROM " + DATABASE_TABLE_7;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_EXERCISE_SORT, null);

        ArrayList<ExerciseSortInfoModel> sortInfoModels = new ArrayList<>();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        while (result.moveToNext()) {
            ExerciseSortInfoModel sortInfoModel = new ExerciseSortInfoModel();
            sortInfoModel.setSortId(result.getString(0));
            sortInfoModel.setExerciseName(result.getString(1));
            sortInfoModel.setExerciseDay(result.getInt(2));
            sortInfoModel.setExerciseType(result.getInt(3));
            sortInfoModels.add(sortInfoModel);
        }

        result.close();
        return sortInfoModels;
    }


    //과거의 데이터를 가져올때
    public ArrayList<ExerciseJoinSortInfoModel> selectRecordExerciseSortsInfo(boolean isToday, int date, int day) {
        String SELECT_EXERCISE_SORT_TODAY = "SELECT EXERCISE_SORT_INFO.SORT_ID, EXERCISE_SORT_INFO.EXERCISE_NAME,EXERCISE_SORT_INFO.EXERCISE_DAY," +
                "EXERCISE_SORT_INFO.EXERCISE_OXYGEN,EXERCISE_SORT_RECORD.RECORD_DATE FROM " + DATABASE_TABLE_7 + " LEFT OUTER JOIN " + DATABASE_TABLE_4 + " ON "
                + DATABASE_TABLE_4 + ".SORT_ID = " + DATABASE_TABLE_7 + ".SORT_ID AND EXERCISE_SORT_RECORD.RECORD_DATE =" + date +
                " WHERE EXERCISE_DAY &" + day + "!=0";
        String SELECT_EXERCISE_SORT = "SELECT EXERCISE_SORT_INFO_CLONE.SORT_ID, EXERCISE_SORT_INFO_CLONE.EXERCISE_NAME,EXERCISE_SORT_INFO_CLONE.EXERCISE_DAY," +
                "EXERCISE_SORT_INFO_CLONE.EXERCISE_OXYGEN,EXERCISE_SORT_RECORD.RECORD_DATE FROM " + DATABASE_TABLE_8 + " LEFT OUTER JOIN " + DATABASE_TABLE_4 + " ON "
                + DATABASE_TABLE_4 + ".SORT_ID = " + DATABASE_TABLE_8 + ".SORT_ID AND EXERCISE_SORT_RECORD.RECORD_DATE =" + date +
                " WHERE EXERCISE_DAY &" + day + "!=0";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result;
        if (isToday)
            result = db.rawQuery(SELECT_EXERCISE_SORT_TODAY, null);
        else
            result = db.rawQuery(SELECT_EXERCISE_SORT, null);

        ArrayList<ExerciseJoinSortInfoModel> sortJoinInfoModels = new ArrayList<>();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        while (result.moveToNext()) {

            ExerciseJoinSortInfoModel sortJoinInfoModel = new ExerciseJoinSortInfoModel();
            sortJoinInfoModel.setSortId(result.getString(0));
            sortJoinInfoModel.setExerciseName(result.getString(1));
            sortJoinInfoModel.setExerciseDay(result.getInt(2));
            sortJoinInfoModel.setExerciseType(result.getInt(3));
            sortJoinInfoModel.setRecordDate(result.getInt(4));
            sortJoinInfoModels.add(sortJoinInfoModel);
        }

        result.close();
        return sortJoinInfoModels;
    }


    //출석장소의 좌표, 이름을 가져올 때
    public ExerciseSpotInfoModel selectExerciseSpotInfo(int id) {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_6 + " WHERE SPOT_ID =" + id;
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

    //출석한적이 있는지 판단
    public ExerciseAttendanceInfoModel selectExerciseAttendance(int date) {
        String SELECT_EXERCISE_ATTEND = "SELECT * FROM " + DATABASE_TABLE_3 + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_EXERCISE_ATTEND, null);

        ExerciseAttendanceInfoModel attendInfoModel = new ExerciseAttendanceInfoModel();
        if (result.moveToNext()) {
            attendInfoModel.setRecordDate(result.getInt(0));
            attendInfoModel.setSpotId(result.getInt(1));
        } else {
            attendInfoModel = null;
        }
        result.close();
        return attendInfoModel;
    }


    public UserInfoModel selectUserInfoDB() {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_1;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_BODY_INFO, null);

        UserInfoModel userInfoModel = null;
        // result(Cursor 객체)가 비어 있으면 false 리턴
        if (result.moveToNext()) {
            userInfoModel = new UserInfoModel();
            userInfoModel.setUserName(result.getString(0));
            userInfoModel.setUserSex(result.getInt(1));
            userInfoModel.setUserHeight(result.getFloat(2));
            userInfoModel.setUserCurrWeight(result.getFloat(3));
            userInfoModel.setUserGoalWeight(result.getFloat(4));
            userInfoModel.setGoalDate(result.getInt(5));
            userInfoModel.setStimulusWord(result.getString(6));
            userInfoModel.setStimulusPicture(result.getString(7));
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
