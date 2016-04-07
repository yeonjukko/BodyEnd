package net.yeonjukko.bodyend.model;

/**
 * Created by yeonjukko on 16. 4. 7..
 */
public class YoutubeRecordModel {
    private String youtubeId;
    private String youtubeTitle;
    private int exerciseDate;


    public String getYoutubeId() {
        return youtubeId;
    }

    public void setYoutubeId(String youtubeId) {
        this.youtubeId = youtubeId;
    }

    public String getYoutubeTitle() {
        return youtubeTitle;
    }

    public void setYoutubeTitle(String youtubeTitle) {
        this.youtubeTitle = youtubeTitle;
    }

    public int getExerciseDate() {
        return exerciseDate;
    }

    public void setExerciseDate(int exerciseDate) {
        this.exerciseDate = exerciseDate;
    }
}
