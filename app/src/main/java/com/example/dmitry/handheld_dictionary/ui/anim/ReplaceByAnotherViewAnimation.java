package com.example.dmitry.handheld_dictionary.ui.anim;

import android.support.annotation.NonNull;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @author Dmitry Nikitin on 1/30/2015.
 */
public class ReplaceByAnotherViewAnimation extends ResizeAnimation {

    private final WeakReference<View> mShowingViewRef;

    public ReplaceByAnotherViewAnimation(@NonNull final View hidingView,
                                         @NonNull final View showingView,
                                         final long duration) {
        super(hidingView, ResizeType.BOTH, false, duration);

        mShowingViewRef = new WeakReference<View>(showingView);

        calcTargetSize(showingView);
    }

    private void calcTargetSize(@NonNull final View showingView) {

        Map.Entry<Integer, Integer> targetSize = getViewSize(showingView);

        if (isHorizontalAnimationNeed()) {
            mToWidth = targetSize.getKey();
        }
        if (isVerticalAnimationNeed()) {
            mToHeight = targetSize.getValue();
        }
    }

    @Override
    protected void onAnimationFinish() {

        final View hidingView = mViewRef.get();
        if (hidingView != null) {
            hidingView.setVisibility(View.GONE);
        }

        final View showingView = mShowingViewRef.get();
        if (showingView != null) {
            showingView.setVisibility(View.VISIBLE);
        }

        super.onAnimationFinish();
    }
}
