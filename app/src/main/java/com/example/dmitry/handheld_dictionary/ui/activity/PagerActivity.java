package com.example.dmitry.handheld_dictionary.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.ui.fragment.PagerFragment;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class PagerActivity extends BaseActivity {

    public static final String EXTRA_GROUPS = "EXTRA_GROUPS";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            PagerFragment fragment = new PagerFragment();

            if (getIntent().hasExtra(EXTRA_GROUPS)) {
                Bundle arguments = new Bundle(1);
                arguments.putSerializable(PagerFragment.ARG_GROUPS, getIntent().getSerializableExtra(EXTRA_GROUPS));
                fragment.setArguments(arguments);
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_pager_container, fragment).commit();
        }
    }
}
