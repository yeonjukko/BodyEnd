package net.yeonjukko.bodyend.model;

import android.util.Log;

import net.yeonjukko.bodyend.InitInfoActivity;

/**
 * Created by yeonjukko on 16. 3. 12..
 */
public class UserInfoModel {

    private String userName;
    private int userSex;
    private float userHeight;
    private float userCurrWeight;
    private float userGoalWeight;
    private long goalDate;
    private String stimulusWord;
    private String stimulusPicture;
    private int exerciseAlarmStatus;

    public UserInfoModel() {
        userSex = InitInfoActivity.FLAG_SEX_FEMALE;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

    public float getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(float userHeight) {
        this.userHeight = userHeight;
    }

    public float getUserCurrWeight() {
        return userCurrWeight;
    }

    public void setUserCurrWeight(float userCurrWeight) {
        this.userCurrWeight = userCurrWeight;
    }

    public float getUserGoalWeight() {
        return userGoalWeight;
    }

    public void setUserGoalWeight(float userGoalWeight) {
        this.userGoalWeight = userGoalWeight;
    }

    public long getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(long goalDate) {
        this.goalDate = goalDate;
    }

    public String getStimulusWord() {
        return stimulusWord;
    }

    public void setStimulusWord(String stimulusWord) {
        this.stimulusWord = stimulusWord;
    }

    public String getStimulusPicture() {
        return stimulusPicture;
    }

    public void setStimulusPicture(String stimulusPicture) {
        this.stimulusPicture = stimulusPicture;

    }

    public int getExerciseAlarmStatus() {
        return exerciseAlarmStatus;
    }

    public void setExerciseAlarmStatus(int exerciseAlarmStatus) {
        this.exerciseAlarmStatus = exerciseAlarmStatus;
    }
}
