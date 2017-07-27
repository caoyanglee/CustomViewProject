package io.weimu.www.bean;

import android.support.annotation.NonNull;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/7/26 上午11:42
 * @description
 */

public class BarData implements Comparable<BarData> {
    private int timeX;//1~240
    private double valueY;

    public BarData(int timeX, double valueY) {
        this.timeX = timeX;
        this.valueY = valueY;
    }

    public int getTimeX() {
        return timeX;
    }

    public void setTimeX(int timeX) {
        this.timeX = timeX;
    }

    public double getValueY() {
        return valueY;
    }

    public void setValueY(double valueY) {
        this.valueY = valueY;
    }


    @Override
    public int compareTo(@NonNull BarData o) {
        //升序
        if (this.valueY > o.valueY) return 1;
        if (this.valueY < o.valueY) return -1;
        return 0;
    }
}
