package com.github.nikitin_da.sticky_dictionary.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.github.nikitin_da.sticky_dictionary.R;
import com.github.nikitin_da.sticky_dictionary.ui.fragment.GroupListForAddWordFragment;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListForAddWordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, new GroupListForAddWordFragment());
            ft.commit();
        }
    }

    @Override
    protected boolean notifyAllFragmentsAboutActivityResult() {
        return true;
    }
}
