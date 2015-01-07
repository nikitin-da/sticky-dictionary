package com.example.dmitry.handheld_dictionary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.example.dmitry.handheld_dictionary.R;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
