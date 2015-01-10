package com.example.dmitry.handheld_dictionary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.ui.fragment.WordEditFragment;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordEditActivity extends BaseActivity {

    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_edit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        if (savedInstanceState == null && intent.hasExtra(EXTRA_GROUP_ID)) {
            final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, -1);
            final Fragment fragment = WordEditFragment.newInstance(groupId);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_word_edit, fragment).commit();
        }
    }
}
