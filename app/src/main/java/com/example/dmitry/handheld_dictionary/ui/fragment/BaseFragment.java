package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.ui.activity.BaseActivity;

import butterknife.ButterKnife;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseFragment extends Fragment {

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
        view.setBackgroundResource(R.color.common_background);
    }

    @Override public void onStart() {
        super.onStart();
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            final Integer titleResId = getActionBarTitle();
            if (actionBar != null && titleResId != null) {
                actionBar.setTitle(titleResId);
            }
        }
    }

    /**
     * You may override this method and return not null string resources id,
     * that will display as action bar title.
     *
     * @return actual title resource id
     */
    public Integer getActionBarTitle() {
        return null;
    }

    public void setActionBarTitle(String title) {
        Activity activity = getActivity();
        if (activity instanceof ActionBarActivity) {
            ActionBar actionBar = ((ActionBarActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    public void invalidateOptionsMenu() {

        final Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).supportInvalidateOptionsMenu();
        }
    }
}
