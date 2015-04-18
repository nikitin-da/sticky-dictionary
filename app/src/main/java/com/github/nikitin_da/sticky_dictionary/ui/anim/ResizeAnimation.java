package com.github.nikitin_da.sticky_dictionary.ui.anim;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import java.lang.ref.WeakReference;
import java.util.AbstractMap;
import java.util.Map;

/**
 * An animation for real resizing the view.
 *
 * @author Dmitry Nikitin on 1/30/2015.
 */
public class ResizeAnimation extends Animation {

    public static enum ResizeType {
        VERTICAL,
        HORIZONTAL,
        BOTH
    }

    protected final WeakReference<View> mViewRef;
    private final ResizeType mResizeType;

    final boolean mAppearance;  // show - true, hide - false

    protected float mFromWidth;
    protected float mFromHeight;
    protected float mToWidth;
    protected float mToHeight;

    // Initial size to restore it after animation
    private final int mLPWidth;
    private final int mLPHeight;

    // Store to wrap callbacks.
    private AnimationListener mExternalAnimationListener;

    public ResizeAnimation(@NonNull final View view, @NonNull final ResizeType resizeType, final boolean appearance, final long duration) {
        mViewRef = new WeakReference<View>(view);
        mResizeType = resizeType;
        mAppearance = appearance;

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        // Store initial values to restore it after animation
        mLPWidth = layoutParams.width;
        mLPHeight = layoutParams.height;

        Map.Entry<Integer, Integer> viewSize = getViewSize(view);
        initSizeLimits(viewSize.getKey(), viewSize.getValue());

        setDuration(duration);

        super.setAnimationListener(createAnimationListenerWrapper());
    }

    protected Map.Entry<Integer, Integer> getViewSize(@NonNull final View view) {
        ViewParent viewParent = view.getParent();

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        // If view visibility gone, we need to measure it, to identify it's size.
        if (view.getVisibility() == View.GONE && (viewParent instanceof ViewGroup)) {
            ViewGroup parent = ((ViewGroup) viewParent);

            view.measure(
                    createMeasureSpec(layoutParams.width, parent.getWidth()),
                    createMeasureSpec(layoutParams.height, parent.getHeight())
            );

            return new AbstractMap.SimpleEntry<Integer, Integer>(view.getMeasuredWidth(), view.getMeasuredHeight());
        } else {
            return new AbstractMap.SimpleEntry<Integer, Integer>(view.getWidth(), view.getHeight());
        }
    }

    private int createMeasureSpec(final int dimension, final int realParentDimension) {

        int parentDimensionForSpec;
        int measureSpec;

        switch (dimension) {
            case ViewGroup.LayoutParams.MATCH_PARENT:
                parentDimensionForSpec = realParentDimension;
                measureSpec = View.MeasureSpec.EXACTLY;
                break;
            case ViewGroup.LayoutParams.WRAP_CONTENT:
                parentDimensionForSpec = Integer.MAX_VALUE;
                measureSpec = View.MeasureSpec.AT_MOST;
                break;
            default:
                parentDimensionForSpec = realParentDimension;
                measureSpec = View.MeasureSpec.AT_MOST;
        }
        return View.MeasureSpec.makeMeasureSpec(parentDimensionForSpec, measureSpec);
    }

    private void initSizeLimits(final int originalWidth, final int originalHeight) {
        if (isHorizontalAnimationNeed()) {
            mFromWidth = mAppearance ? 0 : originalWidth;
            mToWidth = mAppearance ? originalWidth : 0;
        }
        if (isVerticalAnimationNeed()) {
            mFromHeight = mAppearance ? 0 : originalHeight;
            mToHeight = mAppearance ? originalHeight : 0;
        }
    }

    protected boolean isHorizontalAnimationNeed() {
        return mResizeType == ResizeType.HORIZONTAL || mResizeType == ResizeType.BOTH;
    }

    protected boolean isVerticalAnimationNeed() {
        return mResizeType == ResizeType.VERTICAL || mResizeType == ResizeType.BOTH;
    }

    @Override
    public void setAnimationListener(final AnimationListener listener) {
        mExternalAnimationListener = listener;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, @NonNull Transformation t) {

        final View view = mViewRef.get();
        if (view == null) {
            return;
        }

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if (isHorizontalAnimationNeed()) {
            float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
            layoutParams.width = (int) width;
        }

        if (isVerticalAnimationNeed()) {
            float height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
            layoutParams.height = (int) height;
        }

        view.requestLayout();
    }

    private AnimationListener createAnimationListenerWrapper() {

        return new AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                if (mExternalAnimationListener != null) {
                    mExternalAnimationListener.onAnimationStart(animation);
                }

                final View view = mViewRef.get();
                if (view != null) {
                    view.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                if (mExternalAnimationListener != null) {
                    mExternalAnimationListener.onAnimationEnd(animation);
                }

                onAnimationFinish();
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {
                if (mExternalAnimationListener != null) {
                    mExternalAnimationListener.onAnimationRepeat(animation);
                }
            }
        };
    }

    protected void onAnimationFinish() {

        final View view = mViewRef.get();
        if (view != null) {

            if (!mAppearance) {
                view.setVisibility(View.GONE);
            }

            restoreViewSize(view);
        }
    }

    /**
     * Restore view size to it's initial state.
     * I use handler, because otherwise
     * {@link android.view.animation.Animation#applyTransformation(float, android.view.animation.Transformation)} will call after this.
     */
    private void restoreViewSize(final @NonNull View view) {
        view.post(
                new Runnable() {
                    @Override
                    public void run() {
                        ViewGroup.LayoutParams p = view.getLayoutParams();
                        p.width = mLPWidth;
                        p.height = mLPHeight;
                        view.requestLayout();
                    }
                }
        );
    }
}