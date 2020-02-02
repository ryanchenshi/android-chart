package com.example.myapplication.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProfitView extends View {
    public static final String TAG = "ProfitView";

    private Paint mPaint;
    private List<ProfitData> mProfitDatas = new ArrayList<>();
    private float minY, maxY, minX, maxX;
    private PointF mCurrPosition = new PointF();

    public ProfitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initPaint();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCurrPosition.x = event.getX();
        mCurrPosition.y = event.getY();
        postInvalidate();
        return true;
    }

    public void setProfitDataList(List<ProfitData> data) {
        mProfitDatas.clear();
        mProfitDatas.addAll(data);
        invalidate();
    }

    private void initPaint() {
        mPaint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float height = getHeight();
        float width = getWidth();

        float bottomHeight = 120;
        float leftWidth = 120;

        // calculate range
        calculateRange();

        // draw left rect
        RectF leftRect = new RectF(0, 0, leftWidth, height - bottomHeight);
        drawLeftRect(leftRect, canvas);

        RectF bottomRect = new RectF(leftRect.right, leftRect.height(), width, height);
        drawBottomRect(bottomRect, canvas);

        RectF centerRect = new RectF(leftRect.right, 0, width, leftRect.bottom);
        drawCenterRect(centerRect, canvas);

        drawMarkView(centerRect, canvas);
    }

    private void drawMarkView(RectF centerRect, Canvas canvas) {
        if (null == mCurrPosition) {
            Log.e(TAG, "drawMarkView: is null");
            return;
        }

        if (centerRect.left > mCurrPosition.x
                || centerRect.right < mCurrPosition.x
                || centerRect.bottom < mCurrPosition.y
                || centerRect.top > mCurrPosition.y){
            return;
        }

        // draw vertical line
        mPaint.setColor(Color.GRAY);
        canvas.drawLine(mCurrPosition.x, centerRect.bottom, mCurrPosition.x, centerRect.top, mPaint);

        // draw label for mark view
        float xCoef = centerRect.width() / (maxX - minX);
        float index = (mCurrPosition.x - centerRect.left) / xCoef;
        int pos = -1;
        for (int i = 0; i < mProfitDatas.size(); ++i) {
            ProfitData data = mProfitDatas.get(i);
            if (data.getFrom() <= index && index <= data.getTo()) {
                pos = i;
                break;
            }
        }

        if (pos >= 0) {
            mPaint.setTextSize(ComonUtils.dip2px(getContext(),12));
            String title = mProfitDatas.get(pos).getTitle();
            float textWidth = mPaint.measureText(title) + 8;
            float x = mCurrPosition.x + ComonUtils.dip2px(getContext(),2);
            float x1 = x + textWidth + ComonUtils.dip2px(getContext(),16);
            if (mCurrPosition.x < (centerRect.width() / 2 + centerRect.left)) {
                RectF markViewRect = new RectF(x, mCurrPosition.y - ComonUtils.dip2px(getContext(),20),
                        x1, mCurrPosition.y + ComonUtils.dip2px(getContext(),20));
                mPaint.setColor(getResources().getColor(R.color.colorDark50));
                canvas.drawRect(markViewRect,mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawText(title, markViewRect.left + ComonUtils.dip2px(getContext(),4),
                        markViewRect.top + markViewRect.height() / 2, mPaint);
            } else {
                x1 = mCurrPosition.x - ComonUtils.dip2px(getContext(),2);
                x = x - textWidth -ComonUtils.dip2px(getContext(),16);
                RectF markViewRect = new RectF(x, mCurrPosition.y - ComonUtils.dip2px(getContext(),20),
                        x1, mCurrPosition.y + ComonUtils.dip2px(getContext(),20));
                mPaint.setColor(getResources().getColor(R.color.colorDark50));
                canvas.drawRect(markViewRect,mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawText(title, markViewRect.right - ComonUtils.dip2px(getContext(),4) - textWidth,
                        markViewRect.top + markViewRect.height() / 2, mPaint);
            }


        }

    }

    private void drawCenterRect(RectF centerRect, Canvas canvas) {

        if (maxX - minX <= 0) {
            return;
        }

        if (maxY - minY <= 0) {
            return;
        }

        float xCoef = centerRect.width() / (maxX - minX);
        float yCoef = centerRect.height() / (maxY - minY);

        for (ProfitData d : mProfitDatas) {
            float x = d.getFrom() * xCoef + centerRect.left;
            float x1 = d.getTo() * xCoef + centerRect.left;
            float y = 0 + centerRect.bottom;
            float y1 = centerRect.bottom - d.getValue() * yCoef;
            mPaint.setColor(d.getColor());
            canvas.drawRect(x, y1, x1, y, mPaint);
        }

    }

    private void calculateRange() {
        maxX = maxY = - Float.MAX_VALUE;
        minY = minX = Float.MAX_VALUE;

        for (ProfitData data : mProfitDatas) {
            if (data.getFrom() < minX) {
                minX = data.getFrom();
            }

            if (data.getTo() > maxX) {
                maxX = data.getTo();
            }

            if (data.getValue() < minY) {
                minY = data.getValue();
            }

            if (data.getValue() > maxY) {
                maxY = data.getValue();
            }
        }

        if (maxY < 5) maxY = 5;
    }

    private void drawBottomRect(RectF bottomRect, Canvas canvas) {
        mPaint.setColor(getResources().getColor(android.R.color.darker_gray));

        mPaint.setStrokeWidth(2);
        canvas.drawLine(bottomRect.left, bottomRect.top, bottomRect.right, bottomRect.top, mPaint);

        int range = (int)(maxX - minX);
        int step = range / 5;
        float xCoef = bottomRect.width() / (maxX - minX);
        mPaint.setTextSize(ComonUtils.dip2px(getContext(),12));
        float height = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top + 3;

        for (int i = 0; i <= 5; ++i) {
            float x = bottomRect.left + i * step * xCoef;

            // draw horizontal line
            canvas.drawLine(x, bottomRect.top, x, bottomRect.top + height / 2, mPaint);

            // label
            String lab = String.format(Locale.ENGLISH, "%d", (int)(i * step + minX));
            if (i == 5) {
                canvas.drawText(lab, x - mPaint.measureText(lab), bottomRect.top + height + 2, mPaint);
            } else {
                canvas.drawText(lab, x - mPaint.measureText(lab) / 2, bottomRect.top + height + 2, mPaint);
            }
        }
    }

    private void drawLeftRect(RectF leftRect, Canvas canvas) {
        mPaint.setColor(Color.DKGRAY);

        // draw vertical line
        canvas.drawLine(leftRect.right, leftRect.top, leftRect.right - 2, leftRect.bottom, mPaint);

        // draw cursor
        float ycoef = leftRect.height() / (maxY - minY);
        int step = (int)((maxY - minY) / 5);
        if (step < 1) {
            step = 1;
        }

        mPaint.setTextSize(ComonUtils.dip2px(getContext(),12));
        float fontHeight = mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top + 3;
        for (int i = 0; i <= 5; ++i) {
            float y = leftRect.bottom - (i * step * ycoef);

            canvas.drawLine(leftRect.right, y, leftRect.right - ComonUtils.dip2px(getContext(),4), y, mPaint);

            String lab = String.format(Locale.ENGLISH, "%d", (int)(step * i));
            if (i == 5) {
                canvas.drawText(lab, leftRect.right - mPaint.measureText(lab) - ComonUtils.dip2px(getContext(),5), leftRect.top - fontHeight, mPaint);
            } else {
                canvas.drawText(lab, leftRect.right - mPaint.measureText(lab) - ComonUtils.dip2px(getContext(),5), y, mPaint);
            }
        }

    }


}
