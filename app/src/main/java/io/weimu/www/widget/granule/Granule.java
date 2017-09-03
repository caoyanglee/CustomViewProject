package io.weimu.www.widget.granule;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

/**
 * @author 艹羊
 * @project CustomViewProject
 * @date 2017/9/3 下午8:50
 * @description
 */

public class Granule {
    private int w;
    private int h;

    private PointF point = new PointF();
    private float speed;
    private int granuleColor = Color.rgb(241, 219, 132);
    private int lineColor = Color.rgb(241, 219, 132);
    private float radius;
    private float angle;
    private float addX;
    private float addY;


    public Granule(int w, int h) {
        this.w = w;
        this.h = h;
        point.x = (int) (Math.random() * w);
        point.y = (int) (Math.random() * h);
        speed = (float) (GranuleGridView.GranuleConfig.defaultSpeed + Math.random() * GranuleGridView.GranuleConfig.variantSpeed);
        angle = (float) Math.floor(Math.random() * 360);
        radius = (float) (GranuleGridView.GranuleConfig.defaultRadius + Math.random() * GranuleGridView.GranuleConfig.variantRadius);
        addX = (float) (speed * Math.cos(angle));
        addY = (float) (speed * Math.sin(angle));
    }

    public void border() {
        if (point.x >= w || point.x <= 0) {      //如果到达左右边界，就让x轴的速度变为原来的负数
            this.addX *= -1;
        }
        if (point.y >= h || point.y <= 0) {     //如果到达上下边界，就让y轴的速度变为原来的负数
            this.addY *= -1;
        }
        if (point.x > w) {                     //下面是改变浏览器窗口大小时的操作，改变窗口大小后有的粒子会被隐藏，让他显示出来即可
            point.x = w;
        }
        if (point.y > h) {
            point.y = h;
        }
        if (point.x < 0) {
            point.x = 0;
        }
        if (point.y < 0) {
            point.y = 0;
        }
    }

    public void update() {
        this.border();
        point.x += addX;                //粒子下一时刻在x轴的坐标
        point.y += addY;
    }


    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        this.point = point;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public int getGranuleColor() {
        return granuleColor;
    }

    public void setGranuleColor(int granuleColor) {
        this.granuleColor = granuleColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public float getAddX() {
        return addX;
    }

    public void setAddX(float addX) {
        this.addX = addX;
    }

    public float getAddY() {
        return addY;
    }

    public void setAddY(float addY) {
        this.addY = addY;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
