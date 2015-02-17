package com.example.dmitry.handheld_dictionary.ui.adapters;

import android.os.Bundle;
import android.widget.BaseAdapter;

import java.util.HashSet;
import java.util.Set;

public abstract class BaseMultiChoiceAdapter extends BaseAdapter {

    private static final String STATE_SELECTED_KEYS = "STATE_SELECTED_KEYS";

    private Set<Long> mCheckedItems = new HashSet<Long>();

    private ItemCheckChangeListener mItemCheckChangeListener;

    public void restoreSelectionFromSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }
        long[] array = savedInstanceState.getLongArray(STATE_SELECTED_KEYS);
        mCheckedItems.clear();
        if (array != null) {
            for (long id : array) {
                mCheckedItems.add(id);
            }
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        long[] array = new long[mCheckedItems.size()];
        int i = 0;
        for (Long id : mCheckedItems) {
            array[i++] = id;
        }
        outState.putLongArray(STATE_SELECTED_KEYS, array);
    }

    public boolean toggleItemChecked(long id) {

        boolean wasChecked = isChecked(id);

        if (wasChecked) {
            mCheckedItems.remove(id);
            if (!isSomethingChecked(mCheckedItems)) {
                setUIStateNothingChecked();
            }
        } else {
            mCheckedItems.add(id);
            if (isSomethingChecked(mCheckedItems)) {
                setUIStateHasChecked();
            }
        }
        notifyDataSetChanged();

        return !wasChecked;
    }

    public HashSet<Long> getCheckedItems() {
        // Return a copy to prevent concurrent modification problems
        return new HashSet<Long>(mCheckedItems);
    }

    protected abstract boolean isSomethingChecked(Set<Long> checkedItems);

    public boolean isChecked(long id) {
        return mCheckedItems.contains(id);
    }

    private void setUIStateHasChecked() {
        if (mItemCheckChangeListener != null) {
            mItemCheckChangeListener.setUIStateHasChecked();
        }
    }

    private void setUIStateNothingChecked() {
        if (mItemCheckChangeListener != null) {
            mItemCheckChangeListener.setUIStateNothingChecked();
        }
    }

    public void setItemCheckChangeListener(ItemCheckChangeListener itemCheckChangeListener) {
        mItemCheckChangeListener = itemCheckChangeListener;
    }

    public interface ItemCheckChangeListener {
        public void setUIStateNothingChecked();
        public void setUIStateHasChecked();
    }
}