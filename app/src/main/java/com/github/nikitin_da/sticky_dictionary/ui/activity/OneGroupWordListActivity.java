package com.github.nikitin_da.sticky_dictionary.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.OneGroupWordListFragment;

public class OneGroupWordListActivity extends BaseActivity {

    public static final String EXTRA_GROUP_ID = "EXTRA_GROUP_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        final Intent intent = getIntent();
        if (savedInstanceState == null && intent.hasExtra(EXTRA_GROUP_ID)) {
            final Long groupId = intent.getLongExtra(EXTRA_GROUP_ID, -1);
            final Fragment fragment = OneGroupWordListFragment.newInstance(groupId);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, fragment).commit();
        }
    }

    @Override protected boolean notifyAllFragmentsAboutActivityResult() {
        return true;
    }
}