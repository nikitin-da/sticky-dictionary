package com.github.nikitin_da.sticky_dictionary.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.util.Loggi;

import java.util.List;

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

    public void slideActivityForResult(final Intent intent, final int requestCode) {
        final ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.activity_slide_in_right,
                android.R.anim.fade_out
        );
        ActivityCompat.startActivityForResult(this, intent, requestCode, options.toBundle());
    }

    protected boolean finishWithAnimation() {
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (notifyAllFragmentsAboutActivityResult()) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();

            if (fragments == null) {
                return;
            }

            for (Fragment fragment : fragments) {
                if (fragment == null) {
                    continue;
                }

                fragment.onActivityResult(requestCode, resultCode, data);

                FragmentManager childFragmentManager = fragment.getChildFragmentManager();

                if (childFragmentManager == null) {
                    continue;
                }

                List<Fragment> childFragments = childFragmentManager.getFragments();

                if (childFragments == null) {
                    continue;
                }

                for (Fragment childFragment : childFragments) {
                    if (childFragment != null) {
                        childFragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
    }

    protected boolean notifyAllFragmentsAboutActivityResult() {
        return false;
    }
}
