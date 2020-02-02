package com.example.myapplication.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PieChartView extends View {

    private int mMarginLeft, mMarginRight, mMarginTop, mMarginBottom;

    private Paint mPaint;

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mPaint= new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        mMarginTop = mMarginBottom = mMarginLeft = mMarginRight = ComonUtils.dip2px(getContext(),4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth() - mMarginLeft - mMarginRight;
        float h = getHeight() - mMarginBottom - mMarginTop;

        float minWidth = w > h ? h : w;

        float centerX = getWidth() / 2f;
        float centerY = getHeight() / 2f;
        float radius = minWidth / 2;

        RectF rectF = new RectF(centerX - radius, centerY - radius,
                centerX + radius, centerY + radius);

        int cnt = 7;
        float angle = 360f / cnt;
        for (int i = 0; i < cnt; ++i) {
            if (i % 2 == 0) {
                mPaint.setColor(Color.GREEN);
            } else {
                mPaint.setColor(Color.GRAY);
            }
            if (i == 6) {
                mPaint.setColor(Color.GRAY);
            }
            canvas.drawArc(rectF, i * angle, angle, true, mPaint);
        }

    }

}
