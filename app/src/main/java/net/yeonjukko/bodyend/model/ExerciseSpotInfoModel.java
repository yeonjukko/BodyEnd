package net.yeonjukko.bodyend.model;

/**
 * Created by yeonjukko on 16. 3. 23..
 */
public class ExerciseSpotInfoModel {
    private int spotId;
    private double spotX;
    private double spotY;
    private String spotName;
    private Integer attendanceDay;

    public int getSpotId() {
        return spotId;
    }

    public void setSpotId(int spotId) {
        this.spotId = spotId;
    }

    public double getSpotX() {
        return spotX;
    }

    public void setSpotX(double spotX) {
        this.spotX = spotX;
    }

    public double getSpotY() {
        return spotY;
    }

    public void setSpotY(double spotY) {
        this.spotY = spotY;
    }

    public String getSpotName() {
        return spotName;
    }

    public void setSpotName(String spotName) {
        this.spotName = spotName;
    }

    public Integer getAttendanceDay() {
        return attendanceDay;
    }

    public void setAttendanceDay(Integer attendanceDay) {
        this.attendanceDay = attendanceDay;
    }
}
