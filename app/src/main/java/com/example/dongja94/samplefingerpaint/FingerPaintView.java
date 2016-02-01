package com.example.dongja94.samplefingerpaint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by dongja94 on 2016-02-01.
 */
public class FingerPaintView extends View {
    public FingerPaintView(Context context) {
        super(context);
        init();
    }

    public FingerPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mOffPaint = new Paint();
        mOffPaint.setStyle(Paint.Style.STROKE);
        mOffPaint.setStrokeWidth(5);
        mOffPaint.setColor(Color.RED);

        mPaint = new Paint();
    }

    Bitmap mOffBitmap;
    Canvas mOffCanvas;
    Paint mOffPaint;
    Paint mPaint;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        int height = getPaddingTop() + getPaddingBottom() + getSuggestedMinimumHeight();

        setMeasuredDimension(
                getDefaultSize(width, widthMeasureSpec),
                getDefaultSize(height, heightMeasureSpec));

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int bitmapWidth = right - left - getPaddingLeft() - getPaddingRight();
        int bitmapHeight = bottom - top - getPaddingTop() - getPaddingBottom();

        if (mOffBitmap == null) {
            mOffBitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            mOffCanvas = new Canvas(mOffBitmap);
        }

        if (bitmapWidth != mOffBitmap.getWidth() ||
                bitmapHeight != mOffBitmap.getHeight()) {
            Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mOffBitmap, 0, 0, mPaint);
            mOffBitmap.recycle();
            mOffBitmap = bitmap;
            mOffCanvas = canvas;
        }
    }

    float oldX, oldY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        switch (action) {
            case MotionEvent.ACTION_DOWN :
                oldX = event.getX();
                oldY = event.getY();
                return true;
            case MotionEvent.ACTION_MOVE :
                float x = event.getX();
                float y = event.getY();
                mOffCanvas.drawLine(oldX, oldY, x, y, mOffPaint);
                oldX = x;
                oldY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP :
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mOffBitmap, getPaddingLeft(), getPaddingTop(), mPaint);
    }
}
