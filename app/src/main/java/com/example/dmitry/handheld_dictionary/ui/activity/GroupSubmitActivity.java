package com.example.dmitry.handheld_dictionary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import com.example.dmitry.handheld_dictionary.R;
import com.example.dmitry.handheld_dictionary.model.Group;
import com.example.dmitry.handheld_dictionary.ui.fragment.GroupCreateFragment;
import com.example.dmitry.handheld_dictionary.ui.fragment.GroupEditFragment;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupSubmitActivity extends BaseActivity {

    public static final String EXTRA_GROUP = "EXTRA_GROUP";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_submit);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final Intent intent = getIntent();
        if (savedInstanceState == null) {
            final Fragment fragment;
            if (intent.hasExtra(EXTRA_GROUP)) {
                final Group group = intent.getParcelableExtra(EXTRA_GROUP);
                fragment = GroupEditFragment.newInstance(group);
            } else {
                fragment = new GroupCreateFragment();
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_group_edit, fragment).commit();
        }
    }
}
