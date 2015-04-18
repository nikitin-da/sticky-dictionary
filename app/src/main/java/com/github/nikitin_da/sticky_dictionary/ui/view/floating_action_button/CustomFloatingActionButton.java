package com.github.nikitin_da.sticky_dictionary.ui.view.floating_action_button;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.AbsListView;

import com.github.nikitin_da.sticky_dictionary.R;
import com.melnykov.fab.FloatingActionButton;
import com.melnykov.fab.ScrollDirectionListener;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class CustomFloatingActionButton extends FloatingActionButton {
    public CustomFloatingActionButton(Context context) {
        super(context);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFloatingActionButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void attachToListView(@NonNull StickyListHeadersListView listView) {
        AbsListViewScrollDetectorImpl scrollDetector = new AbsListViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(null);
        scrollDetector.setOnScrollListener(null);
        scrollDetector.setListView(listView.getWrappedList());
        scrollDetector.setScrollThreshold(getResources().getDimensionPixelOffset(R.dimen.fab_scroll_threshold));
        listView.setOnScrollListener(scrollDetector);
    }

    /**
     * Copy from https://github.com/makovkastar/FloatingActionButton
     * to integrate with {@link se.emilsjolander.stickylistheaders.StickyListHeadersListView}.
     */
    private class AbsListViewScrollDetectorImpl extends AbsListViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;
        private AbsListView.OnScrollListener mOnScrollListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }

            super.onScrollStateChanged(view, scrollState);
        }
    }
}
