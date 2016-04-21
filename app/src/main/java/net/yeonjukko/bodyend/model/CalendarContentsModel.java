package net.yeonjukko.bodyend.model;

/**
 * Created by MoonJongRak on 2016. 4. 20..
 */
public class CalendarContentsModel {
    private int waterCount;
    private int myExerciseCount;
    private int youtubeExerciseCount;
    private boolean isAttendance;
    private int recordDate;


    public int getWaterCount() {
        return waterCount;
    }

    public void setWaterCount(int waterCount) {
        this.waterCount = waterCount;
    }

    public int getMyExerciseCount() {
        return myExerciseCount;
    }

    public void setMyExerciseCount(int myExerciseCount) {
        this.myExerciseCount = myExerciseCount;
    }

    public int getYoutubeExerciseCount() {
        return youtubeExerciseCount;
    }

    public void setYoutubeExerciseCount(int youtubeExerciseCount) {
        this.youtubeExerciseCount = youtubeExerciseCount;
    }

    public boolean isAttendance() {
        return isAttendance;
    }

    public void setAttendance(boolean attendance) {
        isAttendance = attendance;
    }

    public int getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(int recordDate) {
        this.recordDate = recordDate;
    }
}
