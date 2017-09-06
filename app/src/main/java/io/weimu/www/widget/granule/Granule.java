package io.weimu.www.widget.granule;

import android.graphics.PointF;

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
    private PointF acceleratedSpeed = new PointF();
    private float radius;
    private float angle;


    public Granule(int w, int h) {
        this.w = w;
        this.h = h;
        point.x = (int) (Math.random() * w);
        point.y = (int) (Math.random() * h);
        speed = (float) (GranuleGridView.GranuleConfig.defaultSpeed + Math.random() * GranuleGridView.GranuleConfig.variantSpeed);
        angle = (float) Math.floor(Math.random() * 360);
        if (angle % 90 == 0) {
            angle += 45;
        }
        radius = (float) (GranuleGridView.GranuleConfig.defaultRadius + Math.random() * GranuleGridView.GranuleConfig.variantRadius);
        acceleratedSpeed.x = (float) (speed * Math.cos(angle));
        acceleratedSpeed.y = (float) (speed * Math.sin(angle));
    }

    //展示方式1-从四周发射粒子
    private void reCreateGranuleByAround() {
        if (point.x >= w || point.x <= 0 || point.y >= h || point.y <= 0) {
            switch ((int) (Math.floor(Math.random() * 4))) {
                case 0://上
                    point.x = (int) (Math.random() * w);
                    point.y = 0;
                    angle = (float) Math.floor(Math.random() * 180);
                    acceleratedSpeed.x = (float) (speed * Math.cos(angle));
                    acceleratedSpeed.y = -(float) (speed * Math.sin(angle));
                    break;
                case 1://下
                    point.x = (int) (Math.random() * w);
                    point.y = h;
                    angle = (float) Math.floor(Math.random() * 180);
                    acceleratedSpeed.x = (float) (speed * Math.cos(angle));
                    acceleratedSpeed.y = (float) (speed * Math.sin(angle));
                    break;
                case 2://左
                    point.x = 0;
                    point.y = (int) (Math.random() * h);

                    angle = (float) Math.floor(Math.random() * 180) - 90;
                    acceleratedSpeed.x = (float) (speed * Math.cos(angle));
                    acceleratedSpeed.y = (float) (speed * Math.sin(angle));
                    break;
                case 3://右
                    point.x = w;
                    point.y = (int) (Math.random() * h);

                    angle = (float) Math.floor(Math.random() * 180) + 90;
                    acceleratedSpeed.x = -(float) (speed * Math.cos(angle));
                    acceleratedSpeed.y = (float) (speed * Math.sin(angle));
                    break;
            }
        }

    }

    //展示方式2-粒子可遇壁反弹
    private void reCreateGranuleByReflect() {
        if (point.x >= w || point.x <= 0) {      //如果到达左右边界，就让x轴的速度变为原来的负数
            acceleratedSpeed.x *= -1;

        }
        if (point.y >= h || point.y <= 0) {     //如果到达上下边界，就让y轴的速度变为原来的负数
            this.acceleratedSpeed.y *= -1;
        }
    }

    public void update() {
        this.reCreateGranuleByReflect();
        point.x += acceleratedSpeed.x;                //粒子下一时刻在x轴的坐标
        point.y += acceleratedSpeed.y;
    }


    public PointF getPoint() {
        return point;
    }


    public float getRadius() {
        return radius;
    }


}
