package com.ms.behavior;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

public class SimpleViewBehavior extends PercentageViewBehavior<View> {

    private int mStartX;
    private int mStartY;
    private int mStartWidth;
    private int mStartHeight;
    private int mStartBackgroundColor;
    private float mStartAlpha;
    private float mStartRotateX;
    private float mStartRotateY;

    private int targetX;
    private int targetY;
    private int targetWidth;
    private int targetHeight;
    private int targetBackgroundColor;
    private float targetAlpha;
    private float targetRotateX;
    private float targetRotateY;

    public SimpleViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        // setting values
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SimpleViewBehavior);
        targetX = a.getDimensionPixelOffset(R.styleable.SimpleViewBehavior_svb_targetX, UNSPECIFIED_INT);
        targetY = a.getDimensionPixelOffset(R.styleable.SimpleViewBehavior_svb_targetY, UNSPECIFIED_INT);
        targetWidth = a.getDimensionPixelOffset(R.styleable.SimpleViewBehavior_svb_targetWidth, UNSPECIFIED_INT);
        targetHeight = a.getDimensionPixelOffset(R.styleable.SimpleViewBehavior_svb_targetHeight, UNSPECIFIED_INT);
        targetBackgroundColor = a.getColor(R.styleable.SimpleViewBehavior_svb_targetBackgroundColor, UNSPECIFIED_INT);
        targetAlpha = a.getFloat(R.styleable.SimpleViewBehavior_svb_targetAlpha, UNSPECIFIED_FLOAT);
        targetRotateX = a.getFloat(R.styleable.SimpleViewBehavior_svb_targetRotateX, UNSPECIFIED_FLOAT);
        targetRotateY = a.getFloat(R.styleable.SimpleViewBehavior_svb_targetRotateY, UNSPECIFIED_FLOAT);
        a.recycle();
    }

    private SimpleViewBehavior(Builder builder) {
        super(builder);
        targetX = builder.targetX;
        targetY = builder.targetY;
        targetWidth = builder.targetWidth;
        targetHeight = builder.targetHeight;
        targetBackgroundColor = builder.targetBackgroundColor;
        targetAlpha = builder.targetAlpha;
        targetRotateX = builder.targetRotateX;
        targetRotateY = builder.targetRotateY;
    }

    @Override
    void prepare(CoordinatorLayout parent, View child, View dependency) {
        super.prepare(parent, child, dependency);

        mStartX = (int) child.getX();
        mStartY = (int) child.getY();
        mStartWidth = child.getWidth();
        mStartHeight = child.getHeight();
        mStartAlpha = child.getAlpha();
        mStartRotateX = child.getRotationX();
        mStartRotateY = child.getRotationY();

        // only set the start background color when the background is color drawable
        Drawable background = child.getBackground();
        if (background instanceof ColorDrawable) {
            mStartBackgroundColor = ((ColorDrawable) background).getColor();
        }

        // if parent fitsSystemWindows is true, add status bar height to target y if specified
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (parent.getFitsSystemWindows() && targetY != UNSPECIFIED_INT) {
                int result = 0;
                Resources resources = parent.getContext().getResources();
                int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    result = resources.getDimensionPixelSize(resourceId);
                }
                targetY += result;
            }
        }
    }

    @Override
    void updateViewWithPercent(View child, float percent) {
        super.updateViewWithPercent(child, percent);

        float newX = targetX == UNSPECIFIED_INT ? 0 : (targetX - mStartX) * percent;
        float newY = targetY == UNSPECIFIED_INT ? 0 : (targetY - mStartY) * percent;

        // set scale
        if (targetWidth != UNSPECIFIED_INT || targetHeight != UNSPECIFIED_INT) {
            float newWidth = mStartWidth + ((targetWidth - mStartWidth) * percent);
            float newHeight = mStartHeight + ((targetHeight - mStartHeight) * percent);

            child.setScaleX(newWidth / mStartWidth);
            child.setScaleY(newHeight / mStartHeight);
            // make up position for scale change
            newX -= (mStartWidth - newWidth) / 2;
            newY -= (mStartHeight - newHeight) / 2;
        }

        // set new position
        child.setTranslationX(newX);
        child.setTranslationY(newY);

        // set alpha
        if (targetAlpha != UNSPECIFIED_FLOAT) {
            child.setAlpha(mStartAlpha + (targetAlpha - mStartAlpha) * percent);
        }

        // set background color
        if (targetBackgroundColor != UNSPECIFIED_INT && mStartBackgroundColor != 0) {
            ArgbEvaluator evaluator = new ArgbEvaluator();
            int color = (int) evaluator.evaluate(percent, mStartBackgroundColor, targetBackgroundColor);
            child.setBackgroundColor(color);
        }

        // set rotation
        if (targetRotateX != UNSPECIFIED_FLOAT) {
            child.setRotationX(mStartRotateX + (targetRotateX - mStartRotateX) * percent);
        }
        if (targetRotateY != UNSPECIFIED_FLOAT) {
            child.setRotationY(mStartRotateY + (targetRotateY - mStartRotateY) * percent);
        }

        child.requestLayout();
    }

    public static class Builder extends PercentageViewBehavior.Builder<Builder> {

        private int targetX = UNSPECIFIED_INT;
        private int targetY = UNSPECIFIED_INT;
        private int targetWidth = UNSPECIFIED_INT;
        private int targetHeight = UNSPECIFIED_INT;
        private int targetBackgroundColor = UNSPECIFIED_INT;
        private float targetAlpha = UNSPECIFIED_FLOAT;
        private float targetRotateX = UNSPECIFIED_FLOAT;
        private float targetRotateY = UNSPECIFIED_FLOAT;

        @Override
        Builder getThis() {
            return this;
        }

        public Builder targetX(int targetX) {
            this.targetX = targetX;
            return this;
        }

        public Builder targetY(int targetY) {
            this.targetY = targetY;
            return this;
        }

        public Builder targetWidth(int targetWidth) {
            this.targetWidth = targetWidth;
            return this;
        }

        public Builder targetHeight(int targetHeight) {
            this.targetHeight = targetHeight;
            return this;
        }

        public Builder targetBackgroundColor(int targetBackgroundColor) {
            this.targetBackgroundColor = targetBackgroundColor;
            return this;
        }

        public Builder targetAlpha(int targetAlpha) {
            this.targetAlpha = targetAlpha;
            return this;
        }

        public Builder targetRotateX(int targetRotateX) {
            this.targetRotateX = targetRotateX;
            return this;
        }

        public Builder targetRotateY(int targetRotateY) {
            this.targetRotateY = targetRotateY;
            return this;
        }

        public SimpleViewBehavior build() {
            return new SimpleViewBehavior(this);
        }
    }
}
