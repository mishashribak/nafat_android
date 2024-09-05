package com.nafaz.android.ui.view.button;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;
import com.nafaz.android.R;

import org.jetbrains.annotations.NotNull;

public class LoadingMaterialButton extends MaterialButton {

    //Custom values
    private boolean isLoading = false;
    //Native values
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;

    private CircularAnimatedDrawable mAnimatedDrawable;
    private int mPaddingProgress = 15;
    private int mStrokeWidth = 10;
    private String text = "";
    private Canvas mCanvas;

    @SuppressLint("ClickableViewAccessibility")
    public LoadingMaterialButton(Context context) {
        super(context);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    public LoadingMaterialButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        parseAttrs(context, attrs);
    }

    @SuppressLint("ClickableViewAccessibility")
    public LoadingMaterialButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        parseAttrs(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        refresh();
    }

    private void init() {
        //Init default values
        Resources resources = getResources();
        if (resources == null) return;
        text = getText().toString();
    }

    @SuppressLint("ResourceAsColor")
    private void parseAttrs(@NotNull Context context, AttributeSet attrs) {
        //Load from custom attributes
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingMaterialButton);

        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.LoadingMaterialButton_lb_isLoading) {
                isLoading = typedArray.getBoolean(attr, false);
            } else if (attr == R.styleable.LoadingMaterialButton_lb_loaderMargin) {
                mPaddingProgress = typedArray.getDimensionPixelSize(attr, R.dimen.fbutton_default_progress_margin);
            } else if (attr == R.styleable.LoadingMaterialButton_lb_loaderWidth) {
                mStrokeWidth = typedArray.getDimensionPixelSize(attr, R.dimen.fbutton_default_progress_width);

            }
        }
        typedArray.recycle();

        //Get paddingLeft, paddingRight
        int[] attrsArray = new int[]{
                android.R.attr.paddingLeft,  // 0
                android.R.attr.paddingRight, // 1
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);

        mPaddingLeft = ta.getDimensionPixelSize(0, 0);
        mPaddingRight = ta.getDimensionPixelSize(1, 0);
        ta.recycle();

        //Get paddingTop, paddingBottom
        int[] attrsArray2 = new int[]{
                android.R.attr.paddingTop,   // 0
                android.R.attr.paddingBottom,// 1
        };
        TypedArray ta1 = context.obtainStyledAttributes(attrs, attrsArray2);

        mPaddingTop = ta1.getDimensionPixelSize(0, 0);
        mPaddingBottom = ta1.getDimensionPixelSize(1, 0);
        ta1.recycle();
    }


    public void refresh() {
        //Set padding
        this.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        refresh();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCanvas = canvas;
        if (isLoading) {
            drawIndeterminateProgress(canvas);
            setText("");
        } else {
            if (text.length() != 0)
                setText(text);
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null) {
            int offset = (getWidth() - getHeight()) / 2;
            int mColorIndicator = getResources().getColor(android.R.color.white);
            mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, mStrokeWidth);
            int left = offset + mPaddingProgress;
            int right = getWidth() - offset - mPaddingProgress;
            int bottom = getHeight() - mPaddingProgress;
            int top = mPaddingProgress;
            mAnimatedDrawable.setBounds(left, top, right, bottom);
            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
        } else {
            mAnimatedDrawable.draw(canvas);
        }
    }

    private void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getButtonText() {
        return text;
    }

    public void setButtonText(String text) {
        this.text = text;
    }

    public void showLoading() {
        setLoading(true);
        setClickable(false);
    }

    public void hideLoading() {
        setLoading(false);
        setClickable(true);
    }
}