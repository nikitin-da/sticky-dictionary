package com.github.nikitin_da.sticky_dictionary.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Outline;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ListView;

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

    public static void setVisibility(View view, boolean visible) {

        int targetVisibility = visible ? View.VISIBLE : View.GONE;

        if (targetVisibility != view.getVisibility()) {
            view.setVisibility(targetVisibility);
        }
    }

    public static View getItemFromListViewByPosition(final int pos, @NonNull final ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
