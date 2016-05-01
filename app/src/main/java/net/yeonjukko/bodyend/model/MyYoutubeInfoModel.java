package net.yeonjukko.bodyend.model;

/**
 * Created by yeonjukko on 16. 5. 1..
 */
public class MyYoutubeInfoModel {

    private String ytId;
    private String ytTitle;
    private int ytDuration;
    private String ytThumbs;
    private int ytViewCount;

    public String getYtId() {
        return ytId;
    }

    public void setYtId(String ytId) {
        this.ytId = ytId;
    }

    public String getYtTitle() {
        return ytTitle;
    }

    public void setYtTitle(String ytTitle) {
        this.ytTitle = ytTitle;
    }

    public int getYtDuration() {
        return ytDuration;
    }

    public void setYtDuration(int ytDuration) {
        this.ytDuration = ytDuration;
    }

    public String getYtThumbs() {
        return ytThumbs;
    }

    public void setYtThumbs(String ytThumbs) {
        this.ytThumbs = ytThumbs;
    }

    public int getYtViewCount() {
        return ytViewCount;
    }

    public void setYtViewCount(int ytViewCount) {
        this.ytViewCount = ytViewCount;
    }
}
