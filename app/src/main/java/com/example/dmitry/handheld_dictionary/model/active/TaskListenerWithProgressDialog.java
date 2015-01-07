package com.example.dmitry.handheld_dictionary.model.active;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.example.dmitry.handheld_dictionary.util.Loggi;

/**
 * @author Artem Zinnatullin [artem.zinnatullin@gmail.com]
 */
public abstract class TaskListenerWithProgressDialog<Result> extends TaskListener<Result> {

    @NonNull final ProgressDialog mProgressDialog;

    protected TaskListenerWithProgressDialog(@NonNull Activity activity) {
        mProgressDialog = new ProgressDialog(activity);
    }

    protected abstract void configureProgressDialog(@NonNull ProgressDialog progressDialog);

    @Override public void onPreExecute() {
        configureProgressDialog(mProgressDialog);
        mProgressDialog.show();
    }

    protected void dismissProgressDialog() {
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
            Loggi.e(((Object) this).getClass().getSimpleName(), e);
        }
    }
}
