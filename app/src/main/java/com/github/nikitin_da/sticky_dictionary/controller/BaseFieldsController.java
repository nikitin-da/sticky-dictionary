package com.github.nikitin_da.sticky_dictionary.controller;

import android.support.annotation.NonNull;
import android.view.View;

import butterknife.ButterKnife;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseFieldsController {

    public BaseFieldsController(@NonNull final View view) {
        ButterKnife.inject(this, view);
        onValuesChanged();
    }

    protected abstract void onValuesChanged();
}
