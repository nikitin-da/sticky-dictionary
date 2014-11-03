package com.example.dmitry.handheld_dictionary.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.example.dmitry.handheld_dictionary.BuildConfig;
import com.example.dmitry.handheld_dictionary.R;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public final class ViewUtil {

    private ViewUtil() {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void makeCircle(final View view, final int dimenResId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    Context context = view.getContext();
                    int size = context.getResources().getDimensionPixelSize(dimenResId);
                    outline.setOval(0, 0, size, size);
                }
            };

            view.setOutlineProvider(viewOutlineProvider);
        }
    }
}