package net.yeonjukko.bodyend.model;

/**
 * Created by yeonjukko on 16. 3. 25..
 */
public class ExerciseJoinSortInfoModel {
    private int recordDate;
    private String sortId;
    private String exerciseName;
    private int exerciseDay;
    private int exerciseType;

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public int getExerciseDay() {
        return exerciseDay;
    }

    public void setExerciseDay(int exerciseDay) {
        this.exerciseDay = exerciseDay;
    }



    public int getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(int recordDate) {
        this.recordDate = recordDate;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }
}
