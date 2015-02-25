package com.zengye.weixin;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by colour on 2015/1/15.
 */
public class ChangeColorView extends View {

    private int mColor = 0xFF45C01A;
    private Bitmap mIcon;
    private String mText = "WeChat";
    private int mTextSize;
    private Paint mPaint;
    private Paint mTextPaint;
    private float mAlpha;

    private Rect mTextRect;
    private Rect mIconRect;

    private Canvas mCanvas;
    private Bitmap mBitmap;

    private static final String PARENT_STATUS = "parent_status";
    private static final String CHILD_STATUS = "child_status";


    /**
     *
     */
    private int mTextColor = 0xff333333;


    public ChangeColorView(Context context) {
        super(context, null, 0);
    }

    public ChangeColorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChangeColorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ChangeColorView, defStyleAttr, 0);

        int count = a.getIndexCount();

        for (int i = 0; i < count; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ChangeColorView_view_color:
                    mColor = a.getColor(attr, 0x45C01A);
                    break;
                case R.styleable.ChangeColorView_icon_image:
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) a.getDrawable(attr);
                    mIcon = bitmapDrawable.getBitmap();
                    break;
                case R.styleable.ChangeColorView_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.ChangeColorView_text_size:
                    int dp = (int) a.getDimension(attr, 14);
                    mTextSize = (int) a.getDimension(attr, TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_SP, 12,
                                    getResources().getDisplayMetrics()));
                    break;
            }
        }

        a.recycle();
        mTextRect = new Rect();

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(0xff555555);
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int iconWidth = Math.min(getMeasuredWidth() - getPaddingLeft() - getPaddingRight(), getMeasuredHeight() - getPaddingBottom() - getPaddingTop() - mTextRect.height());

        int left = getMeasuredWidth() / 2 - iconWidth / 2;
        int top = getMeasuredHeight() / 2 - (iconWidth + mTextRect.height()) / 2;
        mIconRect = new Rect(left, top, left + iconWidth, top + iconWidth);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawBitmap(mIcon,null,mIconRect,null);
        int alpha = (int) Math.ceil(255 * mAlpha);

        setupIcon(alpha);

        drawSoucreText(canvas,alpha);
        drawTargetText(canvas,alpha);

        canvas.drawBitmap(mBitmap,0,0,null);
    }

    private void setupIcon(int alpha) {
        mBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPaint = new Paint();
        mPaint.setColor(mColor);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(alpha);
        mCanvas.drawRect(mIconRect,mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(mIcon,null,mIconRect,mPaint);
    }

    private void drawTargetText(Canvas canvas, int alpha) {
        mTextPaint.setColor(mColor);
        mTextPaint.setAlpha(alpha);
        int x = getMeasuredWidth() / 2 - mTextRect.width() / 2;
        int y = mIconRect.bottom + mTextRect.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    private void drawSoucreText(Canvas canvas, int alpha) {
        mTextPaint.setColor(0xff333333);
        mTextPaint.setAlpha(255 - alpha);
        mTextPaint.setAntiAlias(true);
        int x = getMeasuredWidth() / 2 - mTextRect.width() / 2;
        int y = mIconRect.bottom + mTextRect.height();
        canvas.drawText(mText, x, y, mTextPaint);
    }

    public void setIconAlpha(float mAlpha) {
        this.mAlpha = mAlpha;
        if(Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {


        Parcelable parent =  super.onSaveInstanceState();

        Bundle bundle = new Bundle();
        bundle.putParcelable(PARENT_STATUS, parent);
        bundle.putFloat(CHILD_STATUS, mAlpha);

        return bundle;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState( bundle.getParcelable(PARENT_STATUS));
            mAlpha = bundle.getFloat(CHILD_STATUS);
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public float getIconAlpha(){
        return mAlpha;
    }
}
