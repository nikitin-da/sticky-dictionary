package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;

import com.example.dmitry.handheld_dictionary.R;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseWordListFragment extends BaseFragment {

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override public void onStart() {
        super.onStart();
        loadWords();
    }

    protected abstract void loadWords();

    @Override public Integer getActionBarTitle() {
        return R.string.navigation_drawer_item_all;
    }
}
