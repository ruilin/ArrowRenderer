package com.ruilin.arrowrenderer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;

import java.util.ArrayList;

/**
 * Created by Ruilin on 2018/4/8.
 */

public class CurveLineRenderer {

    protected float mX;
    protected float mY;
    protected float mCX;
    protected float mCY;

    protected Paint mPaint = new Paint();
    private final Path mPath = new Path();

    private boolean needToDrawPoints = false;

    private float[][] mPoints;

    private float[] mOffect = new float[2];

    protected int mLineWidth;


    protected CurveLineRenderer(int lineWidth, @ColorInt int color) {
        mLineWidth = lineWidth;

        mPaint.setColor(color);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);

        //init(points);
    }

    public void draw(Canvas canvas) {
        if (mOffect[0] != 0 || mOffect[1] != 0) {
            canvas.save();
            canvas.translate(mOffect[0], mOffect[1]);
            canvas.restore();
        }
        canvas.drawPath(mPath, mPaint);

        if (needToDrawPoints) {
            Paint paint = new Paint(mPaint);
            paint.setColor(Color.RED);
            paint.setStrokeWidth(10);
            float[] points = new float[mPoints.length * 2];
            int i = 0;
            for (float[] point : mPoints) {
                points[i] = point[0];
                points[i+1] = point[1];
                i += 2;
            }
            canvas.drawPoints(points, paint);
        }
    }

    public void resetPoints(ArrayList<float[]> points) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        generatePath(points);
    }

    protected void generatePath(ArrayList<float[]> points) {
        if (points.size() == 0) {
            return;
        }
        setStartPoint(points.get(0)[0], points.get(0)[1]);
        for (int i = 1; i < points.size(); i++) {
            toNextPoint(points.get(i)[0], points.get(i)[1]);
        }
        connectToEndPoint(points);
    }

    protected void connectToEndPoint(ArrayList<float[]> points) {
        mPath.lineTo(points.get(points.size() - 1)[0], points.get(points.size() - 1)[1]);
    }

    public float[][] getPoints() {
        return mPoints;
    }

    /**
     * 设置初始点(手指点下屏幕时调用)
     */
    private void setStartPoint(float startX, float startY) {

        //mPath.rewind();
        mPath.reset();

        mX = startX;
        mY = startY;

        //mPath绘制的绘制起点
        mPath.moveTo(startX, startY);
    }

    /**
     * 平滑连接到下个点
     * @param x
     * @param y
     */
    private void toNextPoint(final float x, final float y) {
        final float previousX = mX;
        final float previousY = mY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        //两点之间的距离大于等于3时，连接连接两点形成直线
        if (dx >= 3 || dy >= 3) {
            //设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;

            //二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mCX = cX;
            mCY = cY;
            mPath.quadTo(previousX, previousY, mCX, mCY);

            //两点连成直线
            //mPath.lineTo(x, y);

            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
        }
    }

    public void setDasPathEffect(boolean yes) {
        if (yes) {
            DashPathEffect effects = new DashPathEffect(new float[]{30, 160},0);
            mPaint.setPathEffect(effects);
        }
    }

    public void setDasPathEffect(float[] intervals) {
        DashPathEffect effects = new DashPathEffect(intervals,0);
        mPaint.setPathEffect(effects);
    }


    public void setOffect(float x, float y) {
        mOffect[0] = x;
        mOffect[1] = y;
    }

    public void showPoints(boolean yes) {
        needToDrawPoints = yes;
    }

}
