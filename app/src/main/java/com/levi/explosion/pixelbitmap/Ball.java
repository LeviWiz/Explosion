package com.levi.explosion.pixelbitmap;

/**
 * 作者：张风捷特烈<br/>
 * 时间：2018/11/16 0016:21:51<br/>
 * 邮箱：1981462002@qq.com<br/>
 * 说明：粒子对象
 */
public class Ball implements Cloneable {
    /**
     * 加速度X
     */
    public float aX;
    /**
     * 加速度Y
     */
    public float aY;
    /**
     * 速度X
     */
    public float vX;
    /**
     * 速度Y
     */
    public float vY;
    /**
     * 点位X
     */
    public float x;
    /**
     * 点位Y
     */
    public float y;
    /**
     * 颜色
     */
    public int color;
    /**
     * 半径
     */
    public float r;
    /**
     * 诞生时间
     */
    public long born;

    @Override
    public Ball clone() {
        Ball clone = null;
        try {
            clone = (Ball) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}