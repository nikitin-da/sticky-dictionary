package com.example.dmitry.handheld_dictionary.ui.navigation_drawer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.ui.fragment.BaseFragment;
import com.example.dmitry.handheld_dictionary.ui.view.TypefaceTextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragment for navigation drawer.
 *
 * @author Dmitry Nikitin on 10/13/2014.
 */
public class NavigationDrawerFragment extends BaseFragment {

    private static final String BUNDLE_SELECTED_POS_STATE =
            NavigationDrawerFragment.class.getCanonicalName() + ".mLastSelectedItemPos";

    private static final NavigationDrawerItem DEFAULT_SELECTED_ITEM = NavigationDrawerItem.GROUP;

    private static final String COMMON_ITEM_TYPEFACE = "fonts/Roboto-Light.ttf";
    private static final String CHECKED_ITEM_TYPEFACE = "fonts/Roboto-MediumItalic.ttf";

    private Map<NavigationDrawerItem, ItemViewHolder> mItemViewMap;

    private Listener mListener;

    private NavigationDrawerItem mLastSelectedItem;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavigationDrawerItem[] items = NavigationDrawerItem.values();
        mItemViewMap = new HashMap<>(items.length);
        for (final NavigationDrawerItem item : items) {
            View itemView = view.findViewById(item.containerId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    selectItem(item);
                }
            });

            TypefaceTextView title = (TypefaceTextView) view.findViewById(item.titleId);
            mItemViewMap.put(item, new ItemViewHolder(itemView, title));
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        if (mLastSelectedItem != null) {
            outState.putSerializable(BUNDLE_SELECTED_POS_STATE, mLastSelectedItem);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mLastSelectedItem = (NavigationDrawerItem) savedInstanceState
                    .getSerializable(BUNDLE_SELECTED_POS_STATE);
        }

        selectItem(mLastSelectedItem != null ? mLastSelectedItem : DEFAULT_SELECTED_ITEM);
    }

    private void selectItem(NavigationDrawerItem item) {
        setItemAsSelected(item);
        notifyOnNavigationItemSelected(item);
        mLastSelectedItem = item;
    }

    private void setItemAsSelected(NavigationDrawerItem newItem) {
        if (mLastSelectedItem != null) {
            ItemViewHolder oldViewHolder = mItemViewMap.get(mLastSelectedItem);
            oldViewHolder.title.setTypefaceFromAssets(COMMON_ITEM_TYPEFACE);
            oldViewHolder.container.setSelected(false);
        }
        ItemViewHolder newViewHolder = mItemViewMap.get(newItem);
        newViewHolder.title.setTypefaceFromAssets(CHECKED_ITEM_TYPEFACE);
        newViewHolder.container.setSelected(true);
    }

    private void notifyOnNavigationItemSelected(NavigationDrawerItem item) {

        if (mListener != null) {
            mListener.onNavigationItemSelected(item);
        }
    }

    public void setListener(final Listener listener) {
        mListener = listener;
    }

    public NavigationDrawerItem getLastSelectedItem() {
        return mLastSelectedItem;
    }

    /**
     * Listener for navigation.
     */
    public interface Listener {
        /**
         * Callback on selected item.
         *
         * @param selectedItem The selected item.
         */
        void onNavigationItemSelected(NavigationDrawerItem selectedItem);
    }

    /**
     * Represents ViewHolder pattern.
     */
    class ItemViewHolder {
        public View container;
        public TypefaceTextView title;

        /**
         * Constructor.
         *
         * @param viewContainer The container {@link android.view.View}.
         * @param itemTitle The title.
         */
        ItemViewHolder(final View viewContainer, final TypefaceTextView itemTitle) {
            this.container = viewContainer;
            this.title = itemTitle;
        }
    }
}
