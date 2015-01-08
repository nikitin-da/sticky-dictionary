package com.example.dmitry.handheld_dictionary.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.util.Loggi;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseActivity extends ActionBarActivity {

    protected Toolbar toolbar;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupActionBar();
    }

    private void setupActionBar() {
        toolbar = (Toolbar) findViewById(R.id.activity_toolbar_common);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void slideActivity(final Intent intent) {
        final ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.activity_slide_in_right,
                android.R.anim.fade_out
        );
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

    protected boolean finishWithAnimation() {
        return false;
    }

    @Override public void finish() {
        super.finish();
        if (finishWithAnimation()) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.slide_out_right);
        }
    }

    /**
     * Method, that hides soft keyboard if it is shown.
     */
    public void hideKeyboardIfOpened() {
        // Hide keyboard if opened
        try {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(),
                    0);
        } catch (Exception e) {
            Loggi.e(BaseActivity.class.getSimpleName(), "Can't toggle keyboard visibility");
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
