package com.ruilin.arrowrenderer.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.ArrayList;

/**
 * Created by Ruilin on 2018/4/9.
 */

public class ArrowRenderer extends CurveLineRenderer {
    public final static byte ARROW_SIZE_SMALL = 1;
    public final static byte ARROW_SIZE_MIDDLE = 2;
    public final static byte ARROW_SIZE_LARGE = 3;

    private Paint mArrowPaint;
    private Path mArrowPath;
    private float mDegrees;

    private double mArrowHight;
    private double mArrowWidth;

    private byte mArrowSize = ARROW_SIZE_SMALL;

    private float mBaseX;
    private float mBaseY;


    public ArrowRenderer(int lineWidth, int color, byte arrowSize) {
        super(lineWidth, color);
        mArrowSize = arrowSize;
    }

    @Override
    public void resetPoints(ArrayList<float[]> points) {
        mArrowPath = new Path();
        switch (mArrowSize) {
            case ARROW_SIZE_MIDDLE:
                mArrowHight = mLineWidth * 4;
                mArrowWidth = mArrowHight * 0.5;
                break;
            case ARROW_SIZE_LARGE:
                mArrowHight = mLineWidth * 5;
                mArrowWidth = mArrowHight * 0.6;
                break;
            default:
            case ARROW_SIZE_SMALL:
                mArrowHight = mLineWidth * 2.0;
                mArrowWidth = mArrowHight * 0.5;
                break;
        }
        mArrowPaint = new Paint();
        mArrowPaint.setColor(mPaint.getColor());
        mArrowPaint.setStrokeWidth(10);
        mArrowPaint.setStyle(Paint.Style.FILL);
        mArrowPaint.setAntiAlias(true);
        super.resetPoints(points);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mArrowPath != null) {
            canvas.save();
            canvas.rotate(mDegrees, mBaseX, mBaseY);
            canvas.drawPath(mArrowPath, mArrowPaint);

            /*
            // Debug:
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(mPaint.getStrokeWidth() / 2);
            canvas.drawPoint(testx, testy, mPaint);
            mPaint.setColor(Color.YELLOW);
            canvas.drawPoint(testx2, testy2, mPaint);
            canvas.drawPoint(testx3, testy3, mPaint);
            */
            canvas.restore();
        }
    }

    @Override
    protected void connectToEndPoint(ArrayList<float[]> points) {
        super.connectToEndPoint(points);
        if (points.size() > 1) {
            generateArrow(points.get(points.size() - 2)[0], points.get(points.size() - 2)[1], points.get(points.size() - 1)[0], points.get(points.size() - 1)[1]);
            mBaseX = points.get(points.size() - 1)[0];
            mBaseY = points.get(points.size() - 1)[1];
        }
    }

    float testx, testy, testx2, testy2, testx3, testy3;

    /**
     * generate path of arrow shape.
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     */
    public void generateArrow(float sx, float sy, float ex, float ey) {
        double rad = Math.atan2(ex - sx, ey - sy);
        mDegrees = (float) (180 - Math.toDegrees(rad));

        float tx = ex;
        float ty = (float) (ey - mArrowHight);

        float rx = (float) (ex + mArrowWidth);
        float ry = ey;

        float lx = (float) (ex - mArrowWidth);
        float ly = ey;

        mArrowPath.moveTo(rx, ry);
        mArrowPath.lineTo(tx, ty);
        mArrowPath.lineTo(lx, ly);
        mArrowPath.close();

        testx = tx;
        testy = ty;
        testx2 = rx;
        testy2 = ry;
        testx3 = lx;
        testy3 = ly;
    }

}
