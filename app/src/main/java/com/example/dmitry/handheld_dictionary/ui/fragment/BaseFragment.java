package com.example.dmitry.handheld_dictionary.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseFragment extends Fragment {

    @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
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
}
