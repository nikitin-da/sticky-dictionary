package com.example.dmitry.handheld_dictionary.ui.activity;

import android.os.Bundle;

import com.example.dmitry.handheld_dictionary.R;

/**
 * @author Dmitry Nikitin [nikitin.da.90@gmail.com]
 */
public class GroupListForAddWordActivity extends BaseActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list_for_add_word);
    }

    @Override protected boolean notifyAllFragmentsAboutActivityResult() {
        return true;
    }
}
