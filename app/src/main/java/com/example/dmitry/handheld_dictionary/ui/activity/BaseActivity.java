package com.example.dmitry.handheld_dictionary.ui.activity;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
