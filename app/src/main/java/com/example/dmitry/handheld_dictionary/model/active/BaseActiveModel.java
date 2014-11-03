package com.example.dmitry.handheld_dictionary.model.active;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.App;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class BaseActiveModel {

    protected final Context context;

    public BaseActiveModel(@NonNull Context context) {
        this.context = context;

        if (shouldInject()) {
            ((App) context.getApplicationContext()).inject(this);
        }
    }

    protected boolean shouldInject() {
        return false;
    }
}
