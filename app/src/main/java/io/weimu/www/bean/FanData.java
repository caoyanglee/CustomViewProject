package io.weimu.www.bean;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/8/4 上午9:59
 * @description
 */

public class FanData {
    private int Color;
    private String title;
    private float share;


    public FanData(int color, String title, float share) {
        Color = color;
        this.title = title;
        this.share = share;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getShare() {
        return share;
    }

    public void setShare(float share) {
        this.share = share;
    }
}
