package net.yeonjukko.bodyend.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by yeonjukko on 16. 3. 17..
 */
public class UserRecordModel implements Serializable {
    private int recordDate;
    private String pictureRecord;
    private float weightRecord;
    private int waterRecord;
    private int waterVolume;
    private String mealBreakfast;
    private String mealLunch;
    private String mealDinner;
    private String mealRefreshments;


    public int getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(int recordDate) {
        this.recordDate = recordDate;
    }

    public String getPictureRecord() {
        return pictureRecord;
    }

    public void setPictureRecord(String pictureRecord) {
        this.pictureRecord = pictureRecord;
    }

    public float getWeightRecord() {
        return weightRecord;
    }

    public void setWeightRecord(float weightRecord) {
        this.weightRecord = weightRecord;
    }

    public int getWaterRecord() {
        return waterRecord;
    }

    public void setWaterRecord(int waterRecord) {
        this.waterRecord = waterRecord;
    }

    public int getWaterVolume() {
        return waterVolume;
    }

    public void setWaterVolume(int waterVolume) {
        this.waterVolume = waterVolume;
    }

    public String getMealBreakfast() {
        return mealBreakfast;
    }

    public void setMealBreakfast(String mealBreakfast) {
        this.mealBreakfast = mealBreakfast;
    }

    public String getMealLunch() {
        return mealLunch;
    }

    public void setMealLunch(String mealLunch) {
        this.mealLunch = mealLunch;
    }

    public String getMealDinner() {
        return mealDinner;
    }

    public void setMealDinner(String mealDinner) {
        this.mealDinner = mealDinner;
    }

    public String getMealRefreshments() {
        return mealRefreshments;
    }

    public void setMealRefreshments(String mealRefreshments) {
        this.mealRefreshments = mealRefreshments;
    }

    @Override
    public String toString() {
        HashMap<String, Object> tmp = new HashMap<>();
        tmp.put("recordDate", recordDate);
        tmp.put("pictureRecord", pictureRecord);
        tmp.put("weightRecord", weightRecord);
        tmp.put("waterRecord", waterRecord);
        tmp.put("waterVolume", waterVolume);
        tmp.put("mealBreakfast", mealBreakfast);
        tmp.put("mealLunch", mealLunch);
        tmp.put("mealDinner", mealDinner);
        tmp.put("mealRefreshments", mealRefreshments);
        return tmp.toString();
    }
}
