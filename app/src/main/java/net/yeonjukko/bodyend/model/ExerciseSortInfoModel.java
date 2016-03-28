package net.yeonjukko.bodyend.model;

/**
 * Created by yeonjukko on 16. 3. 25..
 */
public class ExerciseSortInfoModel {
    private int sortId;
    private String exerciseName;
    private int exerciseDay;

    public int getSortId() {
        return sortId;
    }

    public void setSortId(int sortId) {
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
}
