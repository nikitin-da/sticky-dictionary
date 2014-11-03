package com.example.dmitry.handheld_dictionary.ui.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.example.dmitry.handheld_dictionary.R;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class EditActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
