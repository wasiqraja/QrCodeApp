package com.example.qrcodeapp.core.utils.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

import com.example.qrcodeapp.R;
import com.example.qrcodeapp.core.utils.Constants;


public class ViewFinderView extends View {

    private Paint mMaskPaint;
    private Paint mFramePaint;
    private Path mPath;
    public Rect mFrameRect;
    private Bitmap mFrameBitmap;
    private int mFrameCornersSize = 0;
    private int mFrameCornersRadius = 0;
    private float mFrameRatioWidth = 1f;
    private float mFrameRatioHeight = 0.80f;
    private float mFrameSize = 0.85f;
    private float mArrowSize = 20f;

    public float frameBottom = 0;
    public float frameNormalizedRadius = 0;

    public Rect touchHandlerRect = new Rect(0, 0, 0, 0);

    public Boolean isTouchHandlePressed = false;

    public ViewFinderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initialize();
    }

    public ViewFinderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public ViewFinderView(@NonNull final Context context) {
        super(context);
        initialize();
    }

    private void initialize() {

        mMaskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskPaint.setStyle(Paint.Style.FILL);
        mFramePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mFramePaint.setStyle(Paint.Style.STROKE);
        final Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        mPath = path;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (touchHandlerRect.isPointInside((int) x, (int) y)) {
                isTouchHandlePressed = true;
                Constants.INSTANCE.getCanUserChangeScreen().postValue(false);
            } else {
                isTouchHandlePressed = false;
                Constants.INSTANCE.getCanUserChangeScreen().postValue(true);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            isTouchHandlePressed = false;
            Constants.INSTANCE.getCanUserChangeScreen().postValue(true);
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        final Rect frame = mFrameRect;
        if (frame == null) {
            return;
        }
        final int width = getWidth();
        final int height = getHeight();
        final float top = frame.getTop();
        final float left = frame.getLeft();
        final float right = frame.getRight();
        final float bottom = frame.getBottom();
        final float frameCornersSize = mFrameCornersSize;
        final float frameCornersRadius = mFrameCornersRadius;
        final Path path = mPath;
        frameBottom = bottom;

        Drawable drawable = getResources().getDrawable(R.drawable.ic_movement_arrow, null);
        float aspectRatio = drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
        int desiredWidthInPx = 50;
        int derivedHeightInPx = (int) (desiredWidthInPx / aspectRatio);
        drawable.setBounds(0, 0, desiredWidthInPx, derivedHeightInPx);

        if (frameCornersRadius > 0) {
            final float normalizedRadius =
                    Math.min(frameCornersRadius, Math.max(frameCornersSize - 1, 0));
            path.reset();
            path.moveTo(left, top + normalizedRadius);
            path.quadTo(left, top, left + normalizedRadius, top);
            path.lineTo(right - normalizedRadius, top);
            path.quadTo(right, top, right, top + normalizedRadius);
            path.lineTo(right, bottom - normalizedRadius);
            path.quadTo(right, bottom, right - normalizedRadius, bottom);
            path.lineTo(left + normalizedRadius, bottom);
            path.quadTo(left, bottom, left, bottom - normalizedRadius);
            path.lineTo(left, top + normalizedRadius);
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            canvas.drawPath(path, mMaskPaint);
            path.reset();
            path.moveTo(left, top + frameCornersSize);
            path.lineTo(left, top + normalizedRadius);
            path.quadTo(left, top, left + normalizedRadius, top);
            path.lineTo(left + frameCornersSize, top);
            path.moveTo(right - frameCornersSize, top);
            path.lineTo(right - normalizedRadius, top);
            path.quadTo(right, top, right, top + normalizedRadius);
            path.lineTo(right, top + frameCornersSize);
            path.moveTo(right, bottom - frameCornersSize);
            path.lineTo(right, bottom - normalizedRadius);
            path.quadTo(right, bottom, right - normalizedRadius, bottom);
            path.lineTo(right - frameCornersSize, bottom);
            path.moveTo(left + frameCornersSize, bottom);
            path.lineTo(left + normalizedRadius, bottom);
            path.quadTo(left, bottom, left, bottom - normalizedRadius);
            path.lineTo(left, bottom - frameCornersSize);
            canvas.drawPath(path, mFramePaint);

            canvas.translate(right - (desiredWidthInPx >> 1), bottom - (derivedHeightInPx >> 1));
            drawable.draw(canvas);

//            drawArrow(mFramePaint, canvas, right - 10, bottom - 10, right - normalizedRadius, bottom - normalizedRadius);
//            drawArrow1(mFramePaint, canvas, right - 10, bottom - 10, right - normalizedRadius, bottom - normalizedRadius);
            frameNormalizedRadius = normalizedRadius;
        } else {
            path.reset();
            path.moveTo(left, top);
            path.lineTo(right, top); //Top Line
            path.lineTo(right, bottom); //Right Line
            path.lineTo(left, bottom); //Bottom Line
            path.lineTo(left, top); //Left Line
            path.moveTo(0, 0);
            path.lineTo(width, 0);
            path.lineTo(width, height);
            path.lineTo(0, height);
            path.lineTo(0, 0);
            canvas.drawPath(path, mMaskPaint);
            path.reset();
            path.moveTo(left, top + frameCornersSize);
            path.lineTo(left, top); //Left Left Line
            path.lineTo(left + frameCornersSize, top); //Left Top Line
            path.moveTo(right - frameCornersSize, top);
            path.lineTo(right, top); //Right Top Line
            path.lineTo(right, top + frameCornersSize); //Right Right Line
            path.moveTo(right, bottom - frameCornersSize);
            path.lineTo(right, bottom - derivedHeightInPx); //Right Bottom Line
            path.moveTo(right, bottom);
            path.moveTo(right - desiredWidthInPx, bottom);
            path.lineTo(right - frameCornersSize, bottom); //Bottom Bottom Line
            path.moveTo(left + frameCornersSize, bottom);
            path.lineTo(left, bottom); //Left Bottom Line
            path.lineTo(left, bottom - frameCornersSize); //Left Left Line
            canvas.drawPath(path, mFramePaint);

            canvas.translate(right - (desiredWidthInPx >> 1), bottom - (derivedHeightInPx >> 1));
            drawable.draw(canvas);

//            drawArrow(mFramePaint, canvas, right - 20, bottom - 20, right - frameCornersSize, bottom - frameCornersSize);
//            drawArrow1(mFramePaint, canvas, right - 20, bottom - 20, right - frameCornersSize, bottom - frameCornersSize);
            frameNormalizedRadius = frameCornersRadius;
        }

        int leftHandler = (int) (right - (desiredWidthInPx >> 1));
        int topHandler = (int) (bottom - (derivedHeightInPx >> 1));
        int rightHandler = leftHandler + desiredWidthInPx;
        int bottomHandler = topHandler + derivedHeightInPx;
        touchHandlerRect = new Rect(leftHandler - 50, topHandler - 50, rightHandler + 50, bottomHandler + 50);
    }

    /*private void drawArrow(Paint paint, Canvas canvas, float from_x, float from_y, float to_x, float to_y)
    {
        float angle,anglerad, radius, lineangle;

        //values to change for other appearance *CHANGE THESE FOR OTHER SIZE ARROWHEADS*
        radius=50;
        angle=30;

        //some angle calculations
        anglerad= (float) (PI*angle/180.0f);
        lineangle= (float) (atan2(to_y-from_y,to_x-from_x));

        //tha line
        canvas.drawLine(from_x,from_y,to_x,to_y,paint);

        //tha triangle
        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(to_x, to_y);
        path.lineTo((float)(to_x-radius*cos(lineangle - (anglerad / 2.0))),
                (float)(to_y-radius*sin(lineangle - (anglerad / 2.0))));
        path.lineTo((float)(to_x-radius*cos(lineangle + (anglerad / 2.0))),
                (float)(to_y-radius*sin(lineangle + (anglerad / 2.0))));
        path.close();

        canvas.drawPath(path, paint);
    }*/

    private void drawArrow1(Paint paint, Canvas canvas, float x, float y, float x1, float y1) {
        double degree = calculateDegree(x, x1, y, y1);
        float endX1 = (float) (x1 + ((mArrowSize) * Math.cos(Math.toRadians((degree - 30) + 90))));
        float endY1 = (float) (y1 + ((mArrowSize) * Math.sin(Math.toRadians(((degree - 30) + 90)))));

        float endX2 = (float) (x1 + ((mArrowSize) * Math.cos(Math.toRadians((degree - 60) + 180))));
        float endY2 = (float) (y1 + ((mArrowSize) * Math.sin(Math.toRadians(((degree - 60) + 180)))));

        canvas.drawLine(x1 + 60, y1 + 60, endX1 + 60, endY1 + 60, paint);
        canvas.drawLine(x1 + 60, y1 + 60, endX2 + 60, endY2 + 60, paint);
        canvas.drawLine(x, y, x1 + 60, y1 + 60, paint);
    }

    private void drawArrow(Paint paint, Canvas canvas, float x, float y, float x1, float y1) {

        double degree1 = calculateDegree(x1, x, y1, y);
        float endX11 = (float) (x + ((mArrowSize) * Math.cos(Math.toRadians((degree1 - 30) + 90))));
        float endY11 = (float) (y + ((mArrowSize) * Math.sin(Math.toRadians(((degree1 - 30) + 90)))));

        float endX22 = (float) (x + ((mArrowSize) * Math.cos(Math.toRadians((degree1 - 60) + 180))));
        float endY22 = (float) (y + ((mArrowSize) * Math.sin(Math.toRadians(((degree1 - 60) + 180)))));

        canvas.drawLine(x, y, endX11, endY11, paint);
        canvas.drawLine(x, y, endX22, endY22, paint);
    }

    public double calculateDegree(float x1, float x2, float y1, float y2) {
        float startRadians = (float) Math.atan((y2 - y1) / (x2 - x1));
        System.out.println("radian=====" + Math.toDegrees(startRadians));
        startRadians += ((x2 >= x1) ? 90 : -90) * Math.PI / 180;
        return Math.toDegrees(startRadians);
    }

    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right,
                            final int bottom) {
        invalidateFrameRect(right - left, bottom - top);
    }

    @Nullable
    public Rect getFrameRect() {
        return mFrameRect;
    }

    void setFrameAspectRatio(@FloatRange(from = 0, fromInclusive = false) final float ratioWidth,
                             @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        mFrameRatioWidth = ratioWidth;
        mFrameRatioHeight = ratioHeight;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    @FloatRange(from = 0, fromInclusive = false)
    float getFrameAspectRatioWidth() {
        return mFrameRatioWidth;
    }

    void setFrameAspectRatioWidth(
            @FloatRange(from = 0, fromInclusive = false) final float ratioWidth) {
        mFrameRatioWidth = ratioWidth;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    @FloatRange(from = 0, fromInclusive = false)
    float getFrameAspectRatioHeight() {
        return mFrameRatioHeight;
    }

    void setFrameAspectRatioHeight(
            @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        mFrameRatioHeight = ratioHeight;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    @ColorInt
    int getMaskColor() {
        return mMaskPaint.getColor();
    }

    void setMaskColor(@ColorInt final int color) {
        mMaskPaint.setColor(color);
        if (isLaidOut()) {
            invalidate();
        }
    }

    @ColorInt
    int getFrameColor() {
        return mFramePaint.getColor();
    }

    void setFrameColor(@ColorInt final int color) {
        mFramePaint.setColor(color);
        if (isLaidOut()) {
            invalidate();
        }
    }

    @Px
    int getFrameThickness() {
        return (int) mFramePaint.getStrokeWidth();
    }

    void setFrameThickness(@Px final int thickness) {
        mFramePaint.setStrokeWidth(thickness);
        if (isLaidOut()) {
            invalidate();
        }
    }

    @Px
    int getFrameCornersSize() {
        return mFrameCornersSize;
    }

    void setFrameCornersSize(@Px final int size) {
        mFrameCornersSize = size;
        if (isLaidOut()) {
            invalidate();
        }
    }

    @Px
    int getFrameCornersRadius() {
        return mFrameCornersRadius;
    }

    void setFrameCornersRadius(@Px final int radius) {
        mFrameCornersRadius = radius;
        if (isLaidOut()) {
            invalidate();
        }
    }

    @FloatRange(from = 0.1, to = 1.0)
    public float getFrameSize() {
        return mFrameSize;
    }

    void setFrameSize(@FloatRange(from = 0.1, to = 1.0) final float size) {
        mFrameSize = size;
        invalidateFrameRect();
        if (isLaidOut()) {
            invalidate();
        }
    }

    private void invalidateFrameRect() {
        invalidateFrameRect(getWidth(), getHeight());
    }

    private void invalidateFrameRect(final int width, final int height) {
        if (width > 0 && height > 0) {
            final float viewAR = (float) width / (float) height;
            final float frameAR = mFrameRatioWidth / mFrameRatioHeight;
            final int frameWidth;
            final int frameHeight;
            if (viewAR <= frameAR) {
                frameWidth = Math.round(width * mFrameSize);
                frameHeight = Math.round(frameWidth / frameAR);
            } else {
                frameHeight = Math.round(height * mFrameSize);
                frameWidth = Math.round(frameHeight * frameAR);
            }
            final int frameLeft = (width - frameWidth) / 2;
            final int frameTop = (height - frameHeight) / 2;
            mFrameRect = new Rect(frameLeft, frameTop, frameLeft + frameWidth, frameTop + frameHeight);
//            mFrameBitmap = new Rect(frameLeft, frameTop, frameLeft + frameWidth, frameTop + frameHeight);
//            /mFrameBitmap = Bitmap.createBitmap(bitmap, rect.left, rect.top, rect.width(), rect.height());
        }
    }
}