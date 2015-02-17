package com.example.dmitry.handheld_dictionary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Word;
import com.example.dmitry.handheld_dictionary.ui.fragment.WordCreateFragment;
import com.example.dmitry.handheld_dictionary.ui.fragment.WordEditFragment;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class WordSubmitActivity extends BaseActivity {

    public static final String EXTRA_WORD = "EXTRA_WORD";
    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_submit);

        setResult(RESULT_CANCELED);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        if (savedInstanceState == null) {
            final Fragment fragment;
            if (intent.hasExtra(EXTRA_WORD)) {
                final Word word = intent.getParcelableExtra(EXTRA_WORD);
                fragment = WordEditFragment.newInstance(word);

            } else {
                final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, -1);
                fragment = WordCreateFragment.newInstance(groupId);

            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_word_edit, fragment).commit();
        }
    }
}
