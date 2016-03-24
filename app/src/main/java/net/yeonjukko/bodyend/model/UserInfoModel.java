package net.yeonjukko.bodyend.model;

import net.yeonjukko.bodyend.activity.InitInfoActivity;

/**
 * Created by yeonjukko on 16. 3. 12..
 */
public class UserInfoModel {

    private String userName;
    private int userSex;
    private float userHeight;
    private float userCurrWeight;
    private float userGoalWeight;
    private int goalDate;
    private String stimulusWord;
    private String stimulusPicture;

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

    public int getGoalDate() {
        return goalDate;
    }

    public void setGoalDate(int goalDate) {
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

}
