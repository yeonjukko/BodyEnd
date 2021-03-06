package net.yeonjukko.bodyend.libs;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import net.yeonjukko.bodyend.model.CalendarContentsModel;
import net.yeonjukko.bodyend.model.ExerciseAttendanceInfoModel;
import net.yeonjukko.bodyend.model.ExerciseJoinSortInfoModel;
import net.yeonjukko.bodyend.model.ExerciseSortInfoModel;
import net.yeonjukko.bodyend.model.ExerciseSpotInfoModel;
import net.yeonjukko.bodyend.model.MyYoutubeInfoModel;
import net.yeonjukko.bodyend.model.UserInfoModel;
import net.yeonjukko.bodyend.model.UserRecordModel;
import net.yeonjukko.bodyend.model.WaterAlarmInfoModel;
import net.yeonjukko.bodyend.model.YoutubeRecordModel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
    private static final String DATABASE_TABLE_9 = "EXERCISE_YOUTUBE_RECORD";
    private static final String DATABASE_TABLE_10 = "MY_YOUTUBE_INFO";

    private static final int DATABASE_VERSION = 5;

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
            " (SORT_ID TEXT , EXERCISE_NAME TEXT, EXERCISE_DAY INTEGER, EXERCISE_TYPE INTEGER, EXERCISE_ADD_DATE INTEGER)";

    private static final String DATABASE_CREATE_8 = "CREATE TABLE " + DATABASE_TABLE_8 +
            " (SORT_ID TEXT , EXERCISE_NAME TEXT, EXERCISE_DAY INTEGER, EXERCISE_TYPE INTEGER, EXERCISE_ADD_DATE INTEGER)";

    private static final String DATABASE_CREATE_9 = "CREATE TABLE " + DATABASE_TABLE_9 +
            " (YOUTUBE_TITLE TEXT , YOUTUBE_ID TEXT, EXERCISE_DATE INTEGER)";

    private static final String DATABASE_CREATE_10 = "CREATE TABLE " + DATABASE_TABLE_10 +
            " (YOUTUBE_ID TEXT, YOUTUBE_TITLE TEXT, VIDEO_DURA INTEGER , VIDEO_THUMBS TEXT, VIDEO_VIEW_COUNT INTEGER)";

    private static final String DATABASE_ALTER_1_1 = "ALTER TABLE " + DATABASE_TABLE_10 + " ADD COLUMN YOUTUBE_TITLE TEXT";
    private static final String DATABASE_ALTER_1_2 = "ALTER TABLE " + DATABASE_TABLE_10 + " ADD COLUMN VIDEO_DURA INTEGER";
    private static final String DATABASE_ALTER_1_3 = "ALTER TABLE " + DATABASE_TABLE_10 + " ADD COLUMN VIDEO_THUMBS TEXT";
    private static final String DATABASE_ALTER_1_4 = "ALTER TABLE " + DATABASE_TABLE_10 + " ADD COLUMN VIDEO_VIEW_COUNT INTEGER";


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
            super(context, context.getExternalFilesDir(null) + "/" + DATABASE_NAME, null, DATABASE_VERSION);
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
            db.execSQL(DATABASE_CREATE_9);
            db.execSQL(DATABASE_CREATE_10);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            switch (oldVersion) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                    try {
                        db.execSQL(DATABASE_CREATE_10);
                        db.execSQL(DATABASE_ALTER_1_1);
                        db.execSQL(DATABASE_ALTER_1_2);
                        db.execSQL(DATABASE_ALTER_1_3);
                        db.execSQL(DATABASE_ALTER_1_4);
                    } catch (Exception ignored) {
                    }

            }

        }

    }

    public void deleteYoutube(String id) {
        String DELETE_YT = "DELETE FROM " + DATABASE_TABLE_10 + " WHERE YOUTUBE_ID='" + id + "'";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DELETE_YT);
        mDbHelper.close();
        db.close();
    }

    public void deleteSort(String id) {
        String DELETE_SORT = "DELETE FROM " + DATABASE_TABLE_7 + " WHERE SORT_ID='" + id + "'";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DELETE_SORT);
        mDbHelper.close();
        db.close();
    }

    public void deleteSpot(int id) {
        String DELETE_SPOT = "DELETE FROM " + DATABASE_TABLE_6 + " WHERE SPOT_ID=" + id;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(DELETE_SPOT);
        db.close();
        mDbHelper.close();

    }

    public void insertMyYoutube(MyYoutubeInfoModel model) {

        String INSERT_YOUTUBE_ID = "INSERT INTO " + DATABASE_TABLE_10 + " VALUES" + " ('" + model.getYtId() + "','"
                + model.getYtTitle() + "'," + model.getYtDuration() + ",'"
                + model.getYtThumbs() + "'," + model.getYtViewCount() + ")";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_YOUTUBE_ID);
        mDbHelper.close();
        db.close();

    }

    public void insertVideoCheck(YoutubeRecordModel model) {
        String INSERT_YOUTUBE_RECORD = "INSERT INTO " + DATABASE_TABLE_9 + " VALUES" + " ('" + model.getYoutubeTitle() + "','" + model.getYoutubeId() + "'," + model.getExerciseDate() + ")";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_YOUTUBE_RECORD);
        db.close();
        mDbHelper.close();

    }

    public void updateCheckStatus(String id, int date, boolean isChecked) {
        String INSERT_EXERCISE_SORT_RECORD = "INSERT INTO " + DATABASE_TABLE_4 + " VALUES" + " (" + date + ",'" + id + "')";
        String DELETE_EXERCISE_SORT_RECORD = "DELETE FROM " + DATABASE_TABLE_4 + " WHERE RECORD_DATE = " + date + " AND SORT_ID ='" + id + "'";

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
        mDbHelper.close();

    }

    public void updatePictureRecord(String imgPath, int date) {
        String UPDATE_IMAGE = "UPDATE " + DATABASE_TABLE_2 + " SET PICTURE_RECORD=" + "'" + imgPath + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_IMAGE);
        db.close();
        mDbHelper.close();

    }

    public void updateBreakfast(String breakfast, int date) {
        String UPDATE_MEAL_BREAKFAST = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_BREAKFAST=" + "'" + breakfast + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_BREAKFAST);
        db.close();
        mDbHelper.close();

    }

    public void updateLunch(String lunch, int date) {
        String UPDATE_MEAL_LUNCH = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_LUNCH=" + "'" + lunch + "'" + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_LUNCH);
        db.close();
        mDbHelper.close();

    }

    public void updateDinner(String dinner, int date) {
        String UPDATE_MEAL_DINNER = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_DINNER=" + "'" + dinner + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_DINNER);

        db.close();
        mDbHelper.close();

    }

    public void updateRefreshment(String refreshment, int date) {
        String UPDATE_MEAL_REFRESHMENT = "UPDATE " + DATABASE_TABLE_2 + " SET MEAL_REFRESHMENTS=" + "'" + refreshment + "'" + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_MEAL_REFRESHMENT);

        db.close();
        mDbHelper.close();

    }

    public void updateStimulusPic(String pic) {
        String UPDATE_STIMULUS_PICTURE = "UPDATE " + DATABASE_TABLE_1 + " SET STIMULUS_PICTURE='" + pic + "'";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_STIMULUS_PICTURE);
        db.close();
        mDbHelper.close();

    }

    public void updateStimulusWord(String word) {
        String UPDATE_STIMULUS_WORD = "UPDATE " + DATABASE_TABLE_1 + " SET STIMULUS_WORD='" + word + "'";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_STIMULUS_WORD);
        db.close();
        mDbHelper.close();

    }

    public void updateGoalWeight(float weight, int date) {
        String UPDATE_GOAL_WEIGHT = "UPDATE " + DATABASE_TABLE_1 + " SET USER_GOAL_WEIGHT=" + weight;
        String UPDATE_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_2 + " SET WATER_VOLUME=" + (int) (weight * 33 / 300) + " WHERE RECORD_DATE=" + date;

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_GOAL_WEIGHT);
        db.execSQL(UPDATE_WEIGHT_RECORD);
        db.close();
        mDbHelper.close();

    }

    public void updateGoalDate(int date) {
        String UPDATE_GOAL_DATE = "UPDATE " + DATABASE_TABLE_1 + " SET GOAL_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_GOAL_DATE);
        db.close();
        mDbHelper.close();

    }

    public void updateCurrWeight(float weight, int date) {
        String UPDATE_CURR_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_1 + " SET USER_CURR_WEIGHT=" + weight;
        String UPDATE_TODAY_WEIGHT_RECORD = "UPDATE " + DATABASE_TABLE_2 + " SET WEIGHT_RECORD=" + weight + ",WATER_VOLUME=" + (int) (weight * 33 / 300) + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_CURR_WEIGHT_RECORD);
        db.execSQL(UPDATE_TODAY_WEIGHT_RECORD);
        db.close();
        mDbHelper.close();

    }

    public void updateWaterAlarmTime(int start, int stop) {
        String UPDATE_WATER_TIME = "UPDATE " + DATABASE_TABLE_5 + " SET ALARM_TIMEZONE_START=" + start + ", ALARM_TIMEZONE_STOP=" + stop;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_TIME);
        db.close();
        mDbHelper.close();

    }

    public void updateWaterAlarmPeriod(int period) {
        String UPDATE_WATER_PERIOD = "UPDATE " + DATABASE_TABLE_5 + " SET WATER_ALARM_PERIOD=" + period;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_PERIOD);
        db.close();
        mDbHelper.close();

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
        mDbHelper.close();

    }

    public void updateWaterRecord(int drinkWater, int date) {
        String UPDATE_WATER_RECORD = "UPDATE " + DATABASE_TABLE_2 + " SET WATER_RECORD=" + drinkWater + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(UPDATE_WATER_RECORD);
        PrintData();

        db.close();
        mDbHelper.close();

    }

    //1:유산소 0:무산소 2:유투브
    public void insertExerciseSortInfo(ExerciseSortInfoModel model) {
        String INSERT_EXERCISE_SORT = "INSERT INTO " + DATABASE_TABLE_7 + " VALUES " +
                "(" + "'" + model.getSortId() + "'" + "," + "'" + model.getExerciseName() + "'" + "," + model.getExerciseDay() + "," + model.getExerciseType() + "," + model.getExerciseAddDate() + ")";
        String INSERT_EXERCISE_SORT_CLONE = "INSERT INTO " + DATABASE_TABLE_8 + " VALUES " +
                "(" + "'" + model.getSortId() + "'" + "," + "'" + model.getExerciseName() + "'" + "," + model.getExerciseDay() + "," + model.getExerciseType() + "," + model.getExerciseAddDate() + ")";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.execSQL(INSERT_EXERCISE_SORT);
        db.execSQL(INSERT_EXERCISE_SORT_CLONE);

        mDbHelper.close();
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
        mDbHelper.close();
    }

    public void insertExerciseSpotAttendance(ExerciseAttendanceInfoModel model) {

        String INSERT_EXERCISE_ATTEND = "INSERT INTO " + DATABASE_TABLE_3 + " VALUES" +
                "(" + model.getRecordDate() + "," + model.getSpotId() + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_EXERCISE_ATTEND);
        db.close();
        mDbHelper.close();
    }

    public void insertWaterAlarmInfoDB(WaterAlarmInfoModel model) {
        String INSERT_WATER_ALARM_INFO = "INSERT INTO " + DATABASE_TABLE_5 + " VALUES" +
                "(" + model.getWaterAlarmStatus() + "," + model.getWaterAlarmPeriod() + "," + model.getAlarmTimezoneStart()
                + "," + model.getAlarmTimezoneStop() + ")";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(INSERT_WATER_ALARM_INFO);
        db.close();
        mDbHelper.close();
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
        db.close();
        mDbHelper.close();
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
        mDbHelper.close();
        db.close();

    }

    public boolean hasSameYoutubeId(String id) {
        String SELECT_YOUTUBE_INFO = "SELECT * FROM " + DATABASE_TABLE_10 + " WHERE YOUTUBE_ID ='" + id + "'";
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_YOUTUBE_INFO, null);

        if (!result.moveToNext()) {     //같은 유투브가 없을때
            result.close();
            db.close();
            mDbHelper.close();
            return false;
        } else {
            result.close();
            db.close();
            mDbHelper.close();
            return true;
        }

    }

    public JSONArray selectMyYoutube() {
        String SELECT_YOUTUBE_INFO = "SELECT * FROM " + DATABASE_TABLE_10;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_YOUTUBE_INFO, null);
        JSONArray youtubeLists = new JSONArray();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        while (result.moveToNext()) {
            JSONObject data = new JSONObject();
            data.put("video_id", result.getString(0));
            data.put("video_title", result.getString(1));
            data.put("video_duration", result.getLong(2));
            data.put("video_thumbs", result.getString(3));
            data.put("video_view_count", result.getLong(4));
            youtubeLists.add(data);
        }
        result.close();
        db.close();
        mDbHelper.close();
        return youtubeLists;
    }

    public ArrayList<YoutubeRecordModel> selectYoutubeInfo(int date) {
        String SELECT_BODY_INFO = "SELECT * FROM " + DATABASE_TABLE_9 + " WHERE EXERCISE_DATE =" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_BODY_INFO, null);
        ArrayList<YoutubeRecordModel> youtubeRecordModels = new ArrayList<>();
        // result(Cursor 객체)가 비어 있으면 false 리턴
        while (result.moveToNext()) {
            YoutubeRecordModel youtubeRecordModel = new YoutubeRecordModel();
            youtubeRecordModel.setYoutubeTitle(result.getString(0));
            youtubeRecordModel.setYoutubeId(result.getString(1));
            youtubeRecordModel.setExerciseDate(result.getInt(2));
            youtubeRecordModels.add(youtubeRecordModel);
        }
        result.close();
        db.close();
        mDbHelper.close();
        return youtubeRecordModels;
    }

    /**
     * oxygen은 0일때 무산소운동 1일때 유산소운동
     */
    //단순 운동 검색
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
        db.close();
        mDbHelper.close();
        return sortInfoModels;
    }


    //과거, 오늘
    // 의 데이터를 가져올때
    public ArrayList<ExerciseJoinSortInfoModel> selectRecordExerciseSortsInfo(boolean isToday, int date, int day) {
        String SELECT_EXERCISE_SORT_TODAY = "SELECT EXERCISE_SORT_INFO.SORT_ID, EXERCISE_SORT_INFO.EXERCISE_NAME,EXERCISE_SORT_INFO.EXERCISE_DAY," +
                "EXERCISE_SORT_INFO.EXERCISE_TYPE,EXERCISE_SORT_RECORD.RECORD_DATE FROM " + DATABASE_TABLE_7 + " LEFT OUTER JOIN " + DATABASE_TABLE_4 + " ON "
                + DATABASE_TABLE_4 + ".SORT_ID = " + DATABASE_TABLE_7 + ".SORT_ID AND EXERCISE_SORT_RECORD.RECORD_DATE =" + date +
                " WHERE EXERCISE_DAY &" + day + "!=0";
        String SELECT_EXERCISE_SORT = "SELECT EXERCISE_SORT_INFO_CLONE.SORT_ID, EXERCISE_SORT_INFO_CLONE.EXERCISE_NAME,EXERCISE_SORT_INFO_CLONE.EXERCISE_DAY," +
                "EXERCISE_SORT_INFO_CLONE.EXERCISE_TYPE,EXERCISE_SORT_RECORD.RECORD_DATE FROM " + DATABASE_TABLE_8 + " LEFT OUTER JOIN " + DATABASE_TABLE_4 + " ON "
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
        db.close();
        mDbHelper.close();
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
        db.close();
        mDbHelper.close();
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
        db.close();
        mDbHelper.close();
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
        db.close();
        mDbHelper.close();
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
        db.close();
        mDbHelper.close();
        return userInfoModel;
    }

    public UserRecordModel selectDescUserRecordDB(boolean isPrev, int date) {
        String SELECT_RECORD_INFO_DESC = "SELECT * FROM " + DATABASE_TABLE_2 + " WHERE RECORD_DATE<=" + date + " ORDER BY RECORD_DATE DESC LIMIT 1";
        String SELECT_RECORD_INFO_ASEN = "SELECT * FROM " + DATABASE_TABLE_2 + " WHERE RECORD_DATE>=" + date + " ORDER BY RECORD_DATE LIMIT 1";

        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result;
        if (isPrev)
            result = db.rawQuery(SELECT_RECORD_INFO_DESC, null);
        else
            result = db.rawQuery(SELECT_RECORD_INFO_ASEN, null);

        UserRecordModel userRecordModel = null;
        if (result.moveToFirst()) {
            userRecordModel = new UserRecordModel();
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
        db.close();
        mDbHelper.close();
        return userRecordModel;

    }

    public UserRecordModel selectUserRecordDB(int date) {
        String SELECT_RECORD_INFO = "SELECT * FROM " + DATABASE_TABLE_2 + " WHERE RECORD_DATE=" + date;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_RECORD_INFO, null);

        UserRecordModel userRecordModel = null;
        if (result.moveToFirst()) {
            userRecordModel = new UserRecordModel();
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
        db.close();
        mDbHelper.close();
        return userRecordModel;
    }

    public HashMap<Integer, CalendarContentsModel> selectCalendarContents() {
        HashMap<Integer, CalendarContentsModel> mCalendarContentsModels = new HashMap<>();
        String query = "SELECT (SELECT COUNT(*) FROM EXERCISE_SORT_RECORD WHERE EXERCISE_SORT_RECORD.RECORD_DATE = USER_RECORD.RECORD_DATE) AS MY_EXERCISE_COUNT, (SELECT COUNT(*) FROM EXERCISE_YOUTUBE_RECORD WHERE EXERCISE_YOUTUBE_RECORD.EXERCISE_DATE=USER_RECORD.RECORD_DATE) AS YOUTUBE_EXERCISE_COUNT, (SELECT COUNT(*) FROM EXERCISE_SPOT_RECORD WHERE EXERCISE_SPOT_RECORD.RECORD_DATE=USER_RECORD.RECORD_DATE) AS ATTENDANCE,USER_RECORD.WATER_RECORD,USER_RECORD.RECORD_DATE FROM USER_RECORD";
        mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(query, null);

        while (result.moveToNext()) {

            CalendarContentsModel mCalendarContentsModel = new CalendarContentsModel();
            mCalendarContentsModel.setMyExerciseCount(result.getInt(0));
            mCalendarContentsModel.setYoutubeExerciseCount(result.getInt(1));
            if (result.getInt(2) > 0) {
                mCalendarContentsModel.setAttendance(true);
            } else {
                mCalendarContentsModel.setAttendance(false);
            }
            mCalendarContentsModel.setWaterCount(result.getInt(3));
            mCalendarContentsModel.setRecordDate(result.getInt(4));
            mCalendarContentsModels.put(mCalendarContentsModel.getRecordDate(), mCalendarContentsModel);
        }

        result.close();
        db.close();
        mDbHelper.close();
        return mCalendarContentsModels;
    }

    public ArrayList<UserRecordModel> selectGraphData() {
        String SELECT_RECORD_INFO_ALL = "SELECT * FROM " + DATABASE_TABLE_2;

        ArrayList<UserRecordModel> mUserRecordModels = new ArrayList<>();
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_RECORD_INFO_ALL, null);

        while (result.moveToNext()) {
            String imagePath = result.getString(1);
            UserRecordModel userRecordModel = new UserRecordModel();
            userRecordModel.setRecordDate(result.getInt(0));
            userRecordModel.setPictureRecord(imagePath);
            userRecordModel.setWeightRecord(result.getFloat(2));
            userRecordModel.setWaterRecord(result.getInt(3));
            userRecordModel.setWaterVolume(result.getInt(4));
            userRecordModel.setMealBreakfast(result.getString(5));
            userRecordModel.setMealLunch(result.getString(6));
            userRecordModel.setMealDinner(result.getString(7));
            userRecordModel.setMealRefreshments(result.getString(8));
            mUserRecordModels.add(userRecordModel);
        }
        result.close();
        db.close();
        return mUserRecordModels;
    }

    public ArrayList<UserRecordModel> selectUserRecordImage() {
        String SELECT_RECORD_INFO_ALL = "SELECT * FROM " + DATABASE_TABLE_2;

        ArrayList<UserRecordModel> mUserRecordModels = new ArrayList<>();
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_RECORD_INFO_ALL, null);

        while (result.moveToNext()) {
            String imagePath = result.getString(1);
            if (imagePath == null || imagePath.equals("")) {
                continue;
            }
            UserRecordModel userRecordModel = new UserRecordModel();
            userRecordModel.setRecordDate(result.getInt(0));
            userRecordModel.setPictureRecord(imagePath);
            userRecordModel.setWeightRecord(result.getFloat(2));
            userRecordModel.setWaterRecord(result.getInt(3));
            userRecordModel.setWaterVolume(result.getInt(4));
            userRecordModel.setMealBreakfast(result.getString(5));
            userRecordModel.setMealLunch(result.getString(6));
            userRecordModel.setMealDinner(result.getString(7));
            userRecordModel.setMealRefreshments(result.getString(8));
            mUserRecordModels.add(userRecordModel);
        }
        result.close();
        db.close();
        mDbHelper.close();
        return mUserRecordModels;
    }

    public WaterAlarmInfoModel selectWaterAlarmInfoDB() {
        String SELECT_WATER_ALARM_INFO = "SELECT * FROM " + DATABASE_TABLE_5;
        this.mDbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor result = db.rawQuery(SELECT_WATER_ALARM_INFO, null);

        WaterAlarmInfoModel waterAlarmInfoModel = null;
        if (result.moveToNext()) {
            waterAlarmInfoModel = new WaterAlarmInfoModel();
            waterAlarmInfoModel.setWaterAlarmStatus(result.getInt(0));
            waterAlarmInfoModel.setWaterAlarmPeriod(result.getInt(1));
            waterAlarmInfoModel.setAlarmTimezoneStart(result.getInt(2));
            waterAlarmInfoModel.setAlarmTimezoneStop(result.getInt(3));
        }
        result.close();
        db.close();
        mDbHelper.close();
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
