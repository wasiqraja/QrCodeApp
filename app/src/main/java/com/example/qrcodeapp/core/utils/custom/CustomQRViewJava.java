package com.example.qrcodeapp.core.utils.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewGroup;

import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.StyleRes;

import com.example.qrcodeapp.R;


public final class CustomQRViewJava extends ViewGroup {

    private static final int DEFAULT_MASK_COLOR = 0x77000000;
    //    private static final int DEFAULT_FRAME_COLOR = 0xFFC99D00;
    private static final int DEFAULT_FRAME_COLOR = 0xFFFFFFFF;
    private static final float DEFAULT_FRAME_THICKNESS_DP = 2f;
    private float DEFAULT_FRAME_ASPECT_RATIO_WIDTH = 1f;
    private float DEFAULT_FRAME_ASPECT_RATIO_HEIGHT = 0.80f;
    private static final float DEFAULT_FRAME_CORNER_SIZE_DP = 50f;
    private static final float DEFAULT_FRAME_CORNERS_RADIUS_DP = 0f;
    private float DEFAULT_FRAME_SIZE = 0.75f;
    private static final float FOCUS_AREA_SIZE_DP = 20f;
    public ViewFinderView mViewFinderView;

    public ScaleGesture scaleGesture = null;

    /**/
    public CustomQRViewJava(Context context) {
        super(context);
        initialize(context, null, 0, 0);
        setFocusable(true);
    }

    public CustomQRViewJava(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context, attrs, 0, 0);
        setFocusable(true);
    }

    public CustomQRViewJava(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize(context, attrs, defStyleAttr, 0);
    }

    public CustomQRViewJava(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize(@NonNull final Context context, @Nullable final AttributeSet attrs,
                            @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        mViewFinderView = new ViewFinderView(context);
        /*mViewFinderView.setLayoutParams(
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));*/
        final float density = context.getResources().getDisplayMetrics().density;

        if (attrs == null) {
            mViewFinderView.setFrameAspectRatio(DEFAULT_FRAME_ASPECT_RATIO_WIDTH, DEFAULT_FRAME_ASPECT_RATIO_HEIGHT);
            mViewFinderView.setMaskColor(DEFAULT_MASK_COLOR);
            mViewFinderView.setFrameColor(DEFAULT_FRAME_COLOR);
            mViewFinderView.setFrameThickness(Math.round(DEFAULT_FRAME_THICKNESS_DP * density));
            mViewFinderView.setFrameCornersSize(Math.round(DEFAULT_FRAME_CORNER_SIZE_DP * density));
            mViewFinderView
                    .setFrameCornersRadius(Math.round(DEFAULT_FRAME_CORNERS_RADIUS_DP * density));
            mViewFinderView.setFrameSize(DEFAULT_FRAME_SIZE);

        } else {
            TypedArray a = null;
            try {
                a = context.getTheme()
                        .obtainStyledAttributes(attrs, R.styleable.CodeScannerView, defStyleAttr,
                                defStyleRes);
                setMaskColor(a.getColor(R.styleable.CodeScannerView_maskColor, DEFAULT_MASK_COLOR));
                setFrameColor(
                        a.getColor(R.styleable.CodeScannerView_frameColor, DEFAULT_FRAME_COLOR));
                setFrameThickness(
                        a.getDimensionPixelOffset(R.styleable.CodeScannerView_frameThickness,
                                Math.round(DEFAULT_FRAME_THICKNESS_DP * density)));
                setFrameCornersSize(
                        a.getDimensionPixelOffset(R.styleable.CodeScannerView_frameCornersSize,
                                Math.round(DEFAULT_FRAME_CORNER_SIZE_DP * density)));
                setFrameCornersRadius(
                        a.getDimensionPixelOffset(R.styleable.CodeScannerView_frameCornersRadius,
                                Math.round(DEFAULT_FRAME_CORNERS_RADIUS_DP * density)));
                setFrameAspectRatio(a.getFloat(R.styleable.CodeScannerView_frameAspectRatioWidth,
                                DEFAULT_FRAME_ASPECT_RATIO_WIDTH),
                        a.getFloat(R.styleable.CodeScannerView_frameAspectRatioHeight,
                                DEFAULT_FRAME_ASPECT_RATIO_HEIGHT));
                setFrameSize(a.getFloat(R.styleable.CodeScannerView_frameSize, DEFAULT_FRAME_SIZE));
            } finally {
                if (a != null) {
                    a.recycle();
                }
            }
        }
        addView(mViewFinderView);
    }

    float previousX = 0f;
    float previousY = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        Log.i("MyTestingTag", "x: " + x + " -> y: " + y + " -> frameBottom: " + mViewFinderView.frameBottom + " -> normalizeRadius: " + mViewFinderView.frameNormalizedRadius);

        Log.i("GESTURE", "x: " + x);
        Log.i("GESTURE", "y: " + y);

        if (event.getAction() == MotionEvent.ACTION_MOVE && mViewFinderView.isTouchHandlePressed) {
            float dx = x - previousX;
            float dy = y - previousY;

            Log.i("GESTURE", "dx: " + dx);
            Log.i("GESTURE", "dy: " + dy);

            float updatedDx = dx / 400;
            float updatedDy = dy / 400;

            if (dy < 0 && DEFAULT_FRAME_ASPECT_RATIO_WIDTH < 1.7 && DEFAULT_FRAME_SIZE < 0.9) {
                //Gesture up
                float temp = DEFAULT_FRAME_ASPECT_RATIO_WIDTH - updatedDy;
                if (temp < 1.7)
                    DEFAULT_FRAME_ASPECT_RATIO_WIDTH -= updatedDy;
            } else if (dy > 0 && DEFAULT_FRAME_ASPECT_RATIO_WIDTH > 0.8 && DEFAULT_FRAME_SIZE > 0.4) {
                //Gesture down
                float temp = DEFAULT_FRAME_ASPECT_RATIO_WIDTH - updatedDy;
                if (temp > 0.8)
                    DEFAULT_FRAME_ASPECT_RATIO_WIDTH -= updatedDy;
            }

            if (dx > 0 && DEFAULT_FRAME_SIZE > 0.4 && DEFAULT_FRAME_SIZE < 0.9) {
                //Gesture left
                float temp = DEFAULT_FRAME_SIZE + updatedDx;
                if (temp > 0.4 && temp < 0.9)
                    DEFAULT_FRAME_SIZE += updatedDx;
            } else if (dx < 0 && DEFAULT_FRAME_SIZE < 0.9 && DEFAULT_FRAME_SIZE > 0.4) {
                //Gesture right
                float temp = DEFAULT_FRAME_SIZE + updatedDx;
                if (temp > 0.4 && temp < 0.9)
                    DEFAULT_FRAME_SIZE += updatedDx;
            }


//            mViewFinderView.setFrameAspectRatio(DEFAULT_FRAME_ASPECT_RATIO_WIDTH,
//                    DEFAULT_FRAME_ASPECT_RATIO_HEIGHT);


            mViewFinderView.setFrameSize(DEFAULT_FRAME_SIZE);
            mViewFinderView.setFrameAspectRatio(DEFAULT_FRAME_ASPECT_RATIO_WIDTH,
                    DEFAULT_FRAME_ASPECT_RATIO_HEIGHT);


            Log.i("ASPECT", "ASPECT: " + DEFAULT_FRAME_ASPECT_RATIO_WIDTH);
            Log.i("ASPECT", "ASPECTFRAME: " + DEFAULT_FRAME_SIZE);

        } else {
            scaleGestureDetector.onTouchEvent(event);
        }

        previousX = x;
        previousY = y;
        return true;
    }

    ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(getContext(),
            new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {

                    if (scaleGesture != null) {
                        scaleGesture.scaleGesture(detector);
                    }
                    Log.i("TAG", "onScale: called");
                    return true;
                }
            });

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        performLayout(right - left, bottom - top);
    }

    private void performLayout(final int width, final int height) {
        mViewFinderView.layout(0, 0, width, height);
    }

    /**
     * Get current mask color
     *
     * @see #setMaskColor
     */
    @ColorInt
    public int getMaskColor() {
        return mViewFinderView.getMaskColor();
    }

    /**
     * Set color of the space outside of the framing rect
     *
     * @param color Mask color
     */
    public void setMaskColor(@ColorInt final int color) {
        mViewFinderView.setMaskColor(color);
    }

    /**
     * Get current frame color
     *
     * @see #setFrameColor
     */
    @ColorInt
    public int getFrameColor() {
        return mViewFinderView.getFrameColor();
    }

    /**
     * Set color of the frame
     *
     * @param color Frame color
     */
    public void setFrameColor(@ColorInt final int color) {
        mViewFinderView.setFrameColor(color);
    }

    /**
     * Get current frame thickness
     *
     * @see #setFrameThickness
     */
    @Px
    public int getFrameThickness() {
        return mViewFinderView.getFrameThickness();
    }

    /**
     * Set frame thickness
     *
     * @param thickness Frame thickness in pixels
     */
    public void setFrameThickness(@Px final int thickness) {
        if (thickness < 0) {
            throw new IllegalArgumentException("Frame thickness can't be negative");
        }
        mViewFinderView.setFrameThickness(thickness);
    }

    /**
     * Get current frame corners size
     *
     * @see #setFrameCornersSize
     */
    @Px
    public int getFrameCornersSize() {
        return mViewFinderView.getFrameCornersSize();
    }

    /**
     * Set size of the frame corners
     *
     * @param size Size in pixels
     */
    public void setFrameCornersSize(@Px final int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Frame corners size can't be negative");
        }
        mViewFinderView.setFrameCornersSize(size);
    }

    /**
     * Get current frame corners radius
     *
     * @see #setFrameCornersRadius
     */
    @Px
    public int getFrameCornersRadius() {
        return mViewFinderView.getFrameCornersRadius();
    }

    /**
     * Set current frame corners radius
     *
     * @param radius Frame corners radius in pixels
     */
    public void setFrameCornersRadius(@Px final int radius) {
        if (radius < 0) {
            throw new IllegalArgumentException("Frame corners radius can't be negative");
        }
        mViewFinderView.setFrameCornersRadius(radius);
    }

    /**
     * Get current frame size
     *
     * @see #setFrameSize
     */
    @FloatRange(from = 0.1, to = 1.0)
    public float getFrameSize() {
        return mViewFinderView.getFrameSize();
    }

    /**
     * Set relative frame size where 1.0 means full size
     *
     * @param size Relative frame size between 0.1 and 1.0
     */
    public void setFrameSize(@FloatRange(from = 0.1, to = 1) final float size) {
        if (size < 0.1 || size > 1) {
            throw new IllegalArgumentException(
                    "Max frame size value should be between 0.1 and 1, inclusive");
        }
        mViewFinderView.setFrameSize(size);
    }

    /**
     * Get current frame aspect ratio width
     *
     * @see #setFrameAspectRatioWidth
     * @see #setFrameAspectRatio
     */
    @FloatRange(from = 0, fromInclusive = false)
    public float getFrameAspectRatioWidth() {
        return mViewFinderView.getFrameAspectRatioWidth();
    }

    /**
     * Set frame aspect ratio width
     *
     * @param ratioWidth Frame aspect ratio width
     * @see #setFrameAspectRatio
     */
    public void setFrameAspectRatioWidth(
            @FloatRange(from = 0, fromInclusive = false) final float ratioWidth) {
        if (ratioWidth <= 0) {
            throw new IllegalArgumentException(
                    "Frame aspect ratio values should be greater than zero");
        }
        mViewFinderView.setFrameAspectRatioWidth(ratioWidth);
    }

    /**
     * Get current frame aspect ratio height
     *
     * @see #setFrameAspectRatioHeight
     * @see #setFrameAspectRatio
     */
    @FloatRange(from = 0, fromInclusive = false)
    public float getFrameAspectRatioHeight() {
        return mViewFinderView.getFrameAspectRatioHeight();
    }

    /**
     * Set frame aspect ratio height
     *
     * @param ratioHeight Frame aspect ratio width
     * @see #setFrameAspectRatio
     */
    public void setFrameAspectRatioHeight(
            @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        if (ratioHeight <= 0) {
            throw new IllegalArgumentException(
                    "Frame aspect ratio values should be greater than zero");
        }
        mViewFinderView.setFrameAspectRatioHeight(ratioHeight);
    }

    /**
     * Set frame aspect ratio (ex. 1:1, 15:10, 16:9, 4:3)
     *
     * @param ratioWidth  Frame aspect ratio width
     * @param ratioHeight Frame aspect ratio height
     */
    public void setFrameAspectRatio(
            @FloatRange(from = 0, fromInclusive = false) final float ratioWidth,
            @FloatRange(from = 0, fromInclusive = false) final float ratioHeight) {
        if (ratioWidth <= 0 || ratioHeight <= 0) {
            throw new IllegalArgumentException(
                    "Frame aspect ratio values should be greater than zero");
        }
        mViewFinderView.setFrameAspectRatio(ratioWidth, ratioHeight);
    }


    @NonNull
    ViewFinderView getViewFinderView() {
        return mViewFinderView;
    }

    @Nullable
    public Rect getFrameRect() {
        return mViewFinderView.getFrameRect();
    }

    public interface ScaleGesture {
        void scaleGesture(ScaleGestureDetector detector);
    }

    /*public Bitmap newBitmap() {
        Bitmap fullBitmap = Bitmap.createBitmap(mPreviewView.getWidth(),mPreviewView.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap croppedBmp = Bitmap.createBitmap(fullBitmap, mViewFinderView.getFrameRect().getLeft(), mViewFinderView.getFrameRect().getTop(), mViewFinderView.getWidth(), mViewFinderView.getHeight());
        Bitmap b = Bitmap.createBitmap(mViewFinderView.getWidth(), mViewFinderView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        mPreviewView.draw(c);
        return croppedBmp;
    }*/

}
